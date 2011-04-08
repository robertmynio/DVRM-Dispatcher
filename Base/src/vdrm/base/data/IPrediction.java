package vdrm.base.data;

import java.util.ArrayList;

public interface IPrediction {
	//we assume for now that the prediction only holds the PREDICTED tasks, and not the task from which they were deduced(predicted)
	public ArrayList<ITask> getPredictedTasks();
}
