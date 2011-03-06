package vdrm.disp.alg;

import java.util.ArrayList;

import vdrm.base.common.IAlgorithm;
import vdrm.base.common.IPredictor;
import vdrm.base.data.IPrediction;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public class Algorithm1 implements IAlgorithm{

	ArrayList<IServer> emptyServers;
	ArrayList<IServer> inUseServers;
	ArrayList<IServer> fullServers;
	ArrayList<ITask> unassignedTasks;
	ArrayList<ITask> predictedTasks;
	IPrediction prediction;
	IPredictor predictor;
	
	@Override
	public void initialize(ArrayList<IServer> servers) {
		emptyServers = servers;
		//inUseServers = new ArrayList<Server>();
		//fullServers = new ArrayList<Server>();
		//unassignedTasks = new ArrayList<Task>();
		//predictedTasks = new ArrayList<Task>();
		//initialize prediction and predictor
	}

	@Override
	public void newTask(ITask newTask) {
		//we use an OPTIMISTIC approach (regarding the prediction)
		//if predicted tasks list has tasks in it, than we need to deal with prediction
		int i;
		if(predictedTasks.size()>0) {
			if(newTask.equals(predictedTasks.get(0))) { 
					//prediction is correct
					newTask = predictedTasks.get(0);
					newTask.setPredicted(false);
					
					// TODO dispatch(task); -- OpenNebula (task already knows the server)
					
					predictedTasks.remove(0);
					if(predictedTasks.isEmpty())
						prediction.increaseCredibility();
					
					//since the prediction was correct, we exit algorithm and wait for next task
					return;					
				}
			else { 	
				//prediction was not correct
				prediction.decreaseCredibility();
				
				//since we used an optimistic approach, we have to remove the tasks from the servers
				IServer tempServer;
				ITask tempTask;
				for(i=0;i<predictedTasks.size();i++) {
					tempTask = predictedTasks.get(i);
					tempServer = tempTask.getServer();
					tempServer.removeTask(tempTask);
				}
				
				//clear list of predicted tasks
				predictedTasks = new ArrayList<ITask>();
				
				//add this task to unassigned tasks list
				unassignedTasks.add(newTask);
			}
		}
		else
			unassignedTasks.add(newTask);

		//run prediction and add predicted tasks to unassignedTasks list
		prediction = predictor.predict(newTask); 
		ArrayList<ITask> temp = prediction.getPredictedTasks();
		for(i=0;i<temp.size();i++)
			unassignedTasks.add(temp.get(i));
		
		//run consolidation algorithm (distribute tasks from unassignedTasks list to servers
		//this algorithm assigns to every task a server reference and to every server a task 
		//reference (the task is actually added to the list of a server, but the task is not 
		//actually deployed to a physical server)
		consolidateTasks();
		
		//dispatch tasks which are not predicted to real servers
		for(i=0;i<unassignedTasks.size();i++) {
			ITask tempTask = unassignedTasks.get(i);
			if(tempTask.isPredicted())
				predictedTasks.add(tempTask);
			else {
				
				// TODO dispatch(tempTask); -- OpenNubula
				
			}		
		}
		
		//clear unassigned tasks list
		unassignedTasks = new ArrayList<ITask>();
	}
	
	/**
	 * This methods received as input a list of tasks and sorts it 
	 * in an ascending order. The criteria is the following:
	 * Sort according to cpu. If two tasks have equal cpu, sort 
	 * according to mem. If two tasks have equal mem, sort according
	 * to hdd.
	 * @param list - the list of tasks that must be sorted
	 */
	private void sort(ArrayList<ITask> list) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method deals with placing a server in the correct list
	 * First, if the server is in the inUseServers list, it is removed 
	 * and its former position is saved. 
	 * Then, if the server is full, it is placed in the fullServers list.
	 * If not, a new position is searched using binary insertion
	 * (the position is searched starting from the former position) 
	 * @param tempServer - the server that needs to be repositioned
	 */
	private void findNewPosition(IServer tempServer) {
		int poz = inUseServers.indexOf(tempServer);
		if(inUseServers.contains(tempServer))
			inUseServers.remove(tempServer);
		if(tempServer.isFull()) 
			fullServers.add(tempServer);
		else {
			boolean ok = false;
			while(ok==false) {
				poz--;
				if(tempServer.compareTo(inUseServers.get(poz))>0) {
					inUseServers.add(poz,tempServer);
					ok = true;
				}
			}	
		}
	}
	
	/**
	 * There are a number of tasks in the unassignedTasks list
	 * This method assigns each task to a server (and also a server to a task)
	 * OPTIMISTIC approach!
	 */
	private void consolidateTasks()
	{
		ArrayList<ITask> tempList = unassignedTasks;
		
		//sort the array in an ascending order (according to cpu, then mem, then hdd)
		sort(tempList);
		
		int i = 0;
		ITask tempTask;
		IServer tempServer;
		tempTask = tempList.get(0);
		
		while(!tempList.isEmpty() && i<inUseServers.size()) {
			tempServer = inUseServers.get(i);
			if(tempServer.meetsRequirments(tempTask)) {
				//this task fits on server i
				tempTask.setServer(tempServer);
				tempServer.addTask(tempTask);
				
				//we must move this server to its new position in the lists
				findNewPosition(tempServer);
				
				tempList.remove(0);
				if(!tempList.isEmpty())
					tempTask = tempList.get(0);
			}
			else
				i++;
		}
		
		if(!tempList.isEmpty()) {
			ArrayList<IServer> newServers = new ArrayList<IServer>();
			boolean ok;
			int tempListSize = tempList.size();
			while(!tempList.isEmpty()) {
				i = 0;
				ok = false;
				
				//tempTask is the last task in the tempList
				tempTask = tempList.get(tempListSize-1);
				tempListSize--;
				
				while(i<newServers.size() && ok==false) {
					tempServer = newServers.get(i);
					if(tempServer.meetsRequirments(tempTask)) {
						tempTask.setServer(tempServer);
						tempServer.addTask(tempTask);						
						tempList.remove(tempListSize);
						ok = true;
					}
					else 
						i++;
				}
				
				if(ok==false) {
					//we must use a new server
					IServer newServer = emptyServers.get(0);
					newServers.add(newServer);
					emptyServers.remove(0);
					
					tempTask.setServer(newServer);
					newServer.addTask(tempTask);
					tempList.remove(tempListSize);
					ok = true;
				}
			}
			
			//all tasks have been deployed
			if(newServers.size()>0) {
				for(i=0;i<newServers.size();i++)
					//we must move the servers to their new position in the lists
					findNewPosition(newServers.get(i));
			}
		}	
	}

	@Override
	public void redistributeTasks(IServer server, ITask finishedTask) {
		// TODO Auto-generated method stub
		
	}

}
