package vdrm.base.impl;

import java.util.ArrayList;
import java.util.UUID;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
/***
 * TODO: Add logger to each catch clause
 * @author Gygabite
 *
 */
public class Server implements IServer {
	
	// SERVER CHARACTERISTICS
	private int cpuFreq;
	private int memoryAmount;
	private int hddSize;
	
	private int usedCPU;
	private int usedRAM;
	private int usedHDD;
	
	private boolean isFull;
	private boolean isEmpty;
	
	private UUID serverID;
	
	
	// TASKS REPRESENTATION
	private ArrayList<ITask> taskList;
	private int nrOfTasks;
	private int nrOfPredictedTasks;
	
	// Lowest and highest demanding task
	private ITask lowestDemandingTask;
	private ITask highestDemandingTask;
	
	
	/***
	 * Return the next highest demanding task in the server task list
	 * which has not beed previously tried to be placed. 
	 */
	@Override
	public ITask GetNextHighestDemandingTask() {
		for(ITask t : taskList){
			if(t.getRequirementsScore() > highestDemandingTask.getRequirementsScore() &&
					t.isUnsuccessfulPlacement() == false){
				highestDemandingTask = t;
			}
		}
		return highestDemandingTask;
	}

	
	/***
	 * Return the next lowest demanding task in the server task list
	 * which has not beed previously tried to be placed. 
	 */
	@Override
	public ITask GetNextLowestDemandingTask() {
		for(ITask t : taskList){
			if(t.getRequirementsScore() < lowestDemandingTask.getRequirementsScore() &&
					t.isUnsuccessfulPlacement() == false){
				lowestDemandingTask = t;
			}
		}
		return lowestDemandingTask;
	}

	@Override
	public ITask GetTaskWithResources(ArrayList<Integer> resourceDemands) {
		try{
			ITask t = null;
			for(ITask task : taskList){
				if( (task.getCpu() == Integer.parseInt(resourceDemands.get(0).toString())) &&
						(task.getMem() == Integer.parseInt(resourceDemands.get(1).toString())) &&
						(task.getHdd() == Integer.parseInt(resourceDemands.get(2).toString()))
				){
					t = task;
				}
			}
			return t;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public void OrderStandBy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OrderWakeUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addTask(ITask task) {
		try{
			taskList.add(task);
			if(task.isPredicted())
				nrOfPredictedTasks++;
			nrOfTasks++;
			usedCPU += task.getCpu();
			usedRAM += task.getMem();
			usedHDD += task.getHdd();
			
			if( (usedCPU == cpuFreq) || 
				(usedRAM == memoryAmount)||
				(usedHDD == hddSize))
				isFull = true;
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public int getMaxCpu() {
		return cpuFreq;
	}

	@Override
	public int getMaxHdd() {
		return hddSize;
	}

	@Override
	public int getMaxMem() {
		return memoryAmount;
	}

	@Override
	public int getNumberOfPredictedTasks() {
		return nrOfPredictedTasks;
	}

	@Override
	public int getTotalNumberOfTasks() {
		return nrOfTasks;
	}

	@Override
	public int getUsedCpu() {
		return usedCPU;
	}

	@Override
	public int getUsedHdd() {
		return usedHDD;
	}

	@Override
	public int getUsedMem() {
		return usedRAM;
	}

	@Override
	public boolean isFull() {
		return isFull;
	}

	@Override
	public boolean removeTask(ITask task) {
		try{
			if(taskList.isEmpty() != true){
				if(taskList.contains(task)){
					taskList.remove(task);
					usedCPU -= task.getCpu();
					usedRAM -= task.getMem();
					usedHDD -= task.getHdd();
					if(task.isPredicted())
						nrOfPredictedTasks--;
					nrOfTasks--;
					if(nrOfTasks==0)
						isEmpty = true;
				}
			}
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public boolean removeTask(UUID taskId) {
		try{
			if(taskList.isEmpty() != true){
				for(ITask t : taskList){
					if(t.getTaskHandle() == taskId){
						taskList.remove(t);
						usedCPU -= t.getCpu();
						usedRAM -= t.getMem();
						usedHDD -= t.getHdd();
						if(t.isPredicted())
							nrOfPredictedTasks--;
						nrOfTasks--;
						if(nrOfTasks==0)
							isEmpty = true;
					}
						
				}
			}
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public UUID getServerID() {
		return serverID;
	}


	@Override
	public boolean meetsRequirments(ITask task) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int compareTo(IServer server) {
		// TODO Auto-generated method stub
		return 0;
	}

}
