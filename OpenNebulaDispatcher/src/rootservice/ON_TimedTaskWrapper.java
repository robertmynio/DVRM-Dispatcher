package rootservice;

import java.util.TimerTask;

import vdrm.base.data.ITask;
import vdrm.rootservice.RootService;

public class ON_TimedTaskWrapper extends TimerTask {
	private ITask task;
	private int estimatedDuration;
	
	public ON_TimedTaskWrapper(ITask t, int duration){
		this.task = t;
		this.estimatedDuration = duration;
	}
	
	/***
	 * When this is called, it means that the task's time has finished.
	 * Notify the RootService.
	 */
	@Override
	public void run() {
		ON_RootService.Instance().TaskIsDone(this);
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
