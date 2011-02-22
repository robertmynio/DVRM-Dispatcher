package vdrm.base.data;

import java.util.UUID;

public interface IServer {
	public int getMaxCpu();
	public int getMaxMem();
	public int getMaxHdd();
	public int getUsedCpu();
	public int getUsedMem();
	public int getUsedHdd();
	public boolean addTask(ITask task);
	public boolean removeTask(ITask task);
	public boolean removeTask(UUID taskId);
	public int getTotalNumberOfTasks();
	public int getNumberOfPredictedTasks();
}
