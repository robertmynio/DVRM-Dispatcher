package vdrm.rootservice;

import java.util.Timer;
import java.util.TimerTask;

import vdrm.base.data.ITask;

public class TimedTaskWrapper extends TimerTask {
	private ITask task;
	private int estimatedDuration;
	
	public TimedTaskWrapper(ITask t, int duration){
		this.task = t;
		this.estimatedDuration = duration;
	}
	
	/***
	 * When this is called, it means that the task's time has finished.
	 * Notify the RootService.
	 */
	@Override
	public void run() {
			RootService.Instance().TaskIsDone(this);
	}

	///////////////////////////////////////////////////////////////////////
	public ITask getTask() {
		return task;
	}

	public void setTask(ITask task) {
		this.task = task;
	}

	public int getEstimatedDuration() {
		return estimatedDuration;
	}

	public void setEstimatedDuration(int estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}
	
	
}
