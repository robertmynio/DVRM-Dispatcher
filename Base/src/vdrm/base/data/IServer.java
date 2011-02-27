package vdrm.base.data;

import java.util.UUID;

public interface IServer {
	public int getMaxCpu();
	public int getMaxMem();
	public int getMaxHdd();
	public int getUsedCpu();
	public int getUsedMem();
	public int getUsedHdd();
	//avem nevoie de o metoda getThreshhold()? In caz ca fiecare server are un alt threshold la care e optim si sa nu trecem de el ?
	public boolean addTask(ITask task);
	public boolean removeTask(ITask task);
	public boolean removeTask(UUID taskId);
	public int getTotalNumberOfTasks();
	public int getNumberOfPredictedTasks();
	public boolean isFull();
	//oare este nevoie de o metoda : removeAllPredictedTasks() ?
}
