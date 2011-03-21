package vdrm.base.data;

import java.util.ArrayList;
import java.util.UUID;

public interface IServer {
	public int getMaxCpu();
	public int getMaxMem();
	public int getMaxHdd();
	public int getUsedCpu();
	public int getUsedMem();
	public int getUsedHdd();
	public int getLoad();
	
	public UUID getServerID();
	public ArrayList<ITask> getTasks();
	
	//avem nevoie de o metoda getThreshhold()? In caz ca fiecare server are un alt threshold la care e optim si sa nu trecem de el ?
	public boolean addTask(ITask task);
	public boolean removeTask(ITask task);
	public boolean removeTask(UUID taskId);
	public int getTotalNumberOfTasks();
	public int getNumberOfPredictedTasks();
	public boolean isFull();
	public boolean isEmpty();
	public boolean meetsRequirments(ITask task);
	public boolean compareAvailableResources(ITask t);
	public int compareTo(IServer server);
	//oare este nevoie de o metoda : removeAllPredictedTasks() ?
	
	// on/off functionality
	public void OrderStandBy();
	public void OrderWakeUp();
	
	// resource functionality
	public ITask GetNextHighestDemandingTask();
	public ITask GetNextLowestDemandingTask();
	public ITask GetTaskWithResources(ArrayList<Integer> resourceDemands);//0->cpu, 1->mem, 2->hdd
	public ArrayList<Integer> GetAvailableResources();
}
