package vdrm.base.impl;

import java.util.ArrayList;
import java.util.UUID;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public class Server implements IServer {

	@Override
	public ITask GetNextHighestDemandingTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITask GetNextLowestDemandingTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITask GetTaskWithResources(ArrayList<Integer> resourceDemands) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxCpu() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHdd() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxMem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfPredictedTasks() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalNumberOfTasks() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUsedCpu() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUsedHdd() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUsedMem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTask(ITask task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTask(UUID taskId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UUID getServerID() {
		// TODO Auto-generated method stub
		return null;
	}

}
