package vdrm.disp.alg;

import java.util.ArrayList;

import vdrm.base.common.IAlgorithm;
import vdrm.base.common.IPredictor;
import vdrm.base.data.IPrediction;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Server;
import vdrm.base.impl.Sorter;
import vdrm.pred.pred.Predictor;

/***
 * TODO: findNewPosition can use insertSortedServer method! May be the same as
 * 		reorder server list, but called when adding a new task. Might be able to
 * 		make a single method out of the 2.
 * TODO: 
 * @author Vlad & Robi
 *
 */
public class Algorithm1 implements IAlgorithm{

	private ArrayList<IServer> emptyServers;
	private ArrayList<IServer> inUseServers;
	private ArrayList<IServer> fullServers;
	private ArrayList<ITask> unassignedTasks;
	private ArrayList<ITask> predictedTasks;
	private IPrediction prediction;
	private IPredictor predictor;
	private Sorter sortingService;
	
	@Override
	public void initialize(ArrayList<IServer> servers) {
		//initialize data structures
		emptyServers = servers;
		inUseServers = new ArrayList<IServer>();
		fullServers = new ArrayList<IServer>();
		unassignedTasks = new ArrayList<ITask>();
		predictedTasks = new ArrayList<ITask>();
		
		//initialize prediction and predictor
		predictor = new Predictor();
		prediction = null;
		
		//initialize sorting service
		sortingService = new Sorter();
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
		if(prediction!=null) {
			ArrayList<ITask> temp = prediction.getPredictedTasks();
			for(i=0;i<temp.size();i++)
				unassignedTasks.add(temp.get(i));
		}
		
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
		list = sortingService.insertSortTasksAscending(list);
	}
	
	/**
	 * This method deals with placing a server in the correct list
	 * First, if the server is in the inUseServers list, it is removed 
	 * and its former position is saved. 
	 * Then, if the server is full, it is placed in the fullServers list.
	 * If not, a new position is searched starting from the current 
	 * position and moving towards the front of the list 
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
			if(inUseServers.isEmpty()) {
				inUseServers.add(tempServer);
				ok = true;
			}
			if(poz>0) 
				poz--;
			while(ok==false) {	
				if(tempServer.compareTo(inUseServers.get(poz))>0) {
					inUseServers.add(poz,tempServer);
					ok = true;
				}
				poz--;
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
					if(emptyServers.isEmpty()) {
						// TODO We don't have empty servers. What do we do ? Wait, sleep, error ?
					}
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

	/***********************************************************/
	/***********************************************************/
	/********************* TASK ENDS ***************************/
	/***********************************************************/
	/***********************************************************/
	
	/*
	 * Main function which gets called when a task ends.
	 * It treats 2 cases:
	 * 
	 * 1) If the server has a load lower than 50%, 
	 * 		a) If it has a small number of tasks, try to redistribute them
	 * 			on other servers and close this server. 
	 * 		b) If it has a large number of tasks and migration time 
	 * 			would be large, try to fill the server with tasks from the
	 * 			last 2 in-use servers (which can make up to a full server).
	 * 
	 * 2) If the server became empty because of the ended task, close it.
	 * 		Else, try to fill the server with tasks from the
	 * 		last 2 in-use servers (which can make up to a full server).
	 * 		
	 */
	@Override
	public void endTask(ITask t){
		IServer server = t.getServer();
		if(server.getLoad() <= 50){
			if(server.getTotalNumberOfTasks() < BaseCommon.Instance().getNrOfTasksThreshold()){
				if(!redistributeTasks(server))
					reorderServerList(server, -1);
			}else{
				tryToFillServer(server);
			}
		}else{
			if(server.getTotalNumberOfTasks() == 0)
				server.OrderStandBy();
			else
				tryToFillServer(server);
		}
	}

	@Override
	public ITask[] findMaximumUtilizationPlacement(IServer server,
			IServer secondToLastServer, IServer lastServer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean redistributeTasks(IServer server) {
		int noPlaceFound = 0;
		boolean found = false;
		
		while((!server.isEmpty() || emptyServers.size()==0 )&& noPlaceFound < server.getTotalNumberOfTasks()){
			ITask t = server.GetNextLowestDemandingTask();
			for(IServer s:inUseServers){
				if(s.compareAvailableResources(t)){
					
					// TODO migrate_to_new_host(s); -- OpenNebula
					
					fullServers.add(s);
					inUseServers.remove(s);
					found = true;
					break;
				}
			}
			if(!found){
				noPlaceFound++;
				t.setUnsuccessfulPlacement(true);
			}
		}
		
		if(!server.isEmpty()){
			for(ITask t:server.getTasks()){
				t.setUnsuccessfulPlacement(false);
			}
			return false;
		}else{
			return true;
		}
	}

	/***
	 * This method reorders the inUseServer list. 
	 * It is based on the fact that once a task is added/finished on a 
	 * specific server, only that server needs to be reinserted in the 
	 * list such that the list remains ordered.
	 * 
	 * If direction < 0 , a task has ended and server has to be reordered
	 * by going right in the list until a server with a smaller utilization is found.
	 * 
	 * If direction > 0 , a task has been placed and server has to be
	 * reorder by going left in the list until a server with a bigger utilization is found.
	 */
	@Override
	public void reorderServerList(IServer server, int direction) {
		sortingService = new Sorter();
		if(direction < 0){
			// TODO: Check/Test this method...
			sortingService.insertSortedServerGoingRightDesc(server, inUseServers,
					inUseServers.indexOf(server));
		}else{
			inUseServers = sortingService.insertSortedServerDesc(server, inUseServers,
								inUseServers.indexOf(server));
		}
	}

	@Override
	public void tryToFillServer(IServer server) {
		IServer lastServer = inUseServers.get(inUseServers.size()-1);
		
		ArrayList<Integer> availableResources = server.GetAvailableResources();
		ITask bingoTask = lastServer.GetTaskWithResources(availableResources);
		if(bingoTask != null){
			// TODO migrate_to_new_host(bingoTask); -- OpenNebula
			fullServers.add(server);
			inUseServers.remove(server);
		}else{
			// TODO: check if there are at least 2 servers in the list
			ITask[] fittingTasks = findMaximumUtilizationPlacement(server, 
					inUseServers.get(inUseServers.size()-2), lastServer);
			if(fittingTasks.length > 0){
				for(ITask t:fittingTasks){
					// TODO migrate_to_new_host(t); -- OpenNebula
				}
				fullServers.add(server);
				inUseServers.remove(server);
			}else{
				reorderServerList(server, -1);
			}
		}
	}
	
	// GETTERS FOR TESTING PURPOSES
	
	
	public ArrayList<IServer> getEmptyServers() {
		return emptyServers;
	}

	public ArrayList<IServer> getInUseServers() {
		return inUseServers;
	}

	public ArrayList<IServer> getFullServers() {
		return fullServers;
	}

	public ArrayList<ITask> getUnassignedTasks() {
		return unassignedTasks;
	}

	public ArrayList<ITask> getPredictedTasks() {
		return predictedTasks;
	}

	public IPrediction getPrediction() {
		return prediction;
	}
}
