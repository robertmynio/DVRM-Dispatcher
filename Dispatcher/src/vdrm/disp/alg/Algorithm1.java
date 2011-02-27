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
	public void newTask(ITask task) {
		
		if(predictedTasks.size()>0)
		{
			if(task.equals(predictedTasks.get(0))) { //prediction is correct
					task = predictedTasks.get(0);
					IServer server = predictedTasks.get(0).getServer();
					server.addTask(task);
					task.setPredicted(false);
					
					// TODO dispatch(task); -- OpenNubula
					
					predictedTasks.remove(0);
					if(predictedTasks.isEmpty())
						prediction.increaseCredibility();
					return;					
				}
			else { //prediction was not correct
				prediction.decreaseCredibility();
				predictedTasks.removeAll(predictedTasks);
				// TODO Question : does removeAll actually remove all tasks?
				unassignedTasks.add(task);
			}
		}
		else
			unassignedTasks.add(task);

		//run prediction and add predicted tasks to unassignedTasks list
		prediction = predictor.predict(task); 
		ArrayList<ITask> temp = prediction.getPredictedTasks();
		for(int i=0;i<temp.size();i++)
			unassignedTasks.add(temp.get(i));
		
		//run consolidation algorithm (distribute tasks from unassignedTasks list to servers
		//this algorithm assigns to every task a server reference and to every server a task 
		//reference (the task is actually added to the list of a server, but the task is not 
		//actually deployed)
		consolidateTasks();
		
		//dispatch tasks which are not predicted to real servers
		for(int i=0;i<unassignedTasks.size();i++) {
			ITask tempTask = unassignedTasks.get(i);
			if(tempTask.isPredicted())
				predictedTasks.add(tempTask);
			else {
				// TODO dispatch(tempTask); -- OpenNubula
			}
				
		}
	}
	
	private void reorderServerList()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void consolidateTasks()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redistributeTasks(IServer server, ITask finishedTask) {
		// TODO Auto-generated method stub
		
	}

}
