package rootservice;

import java.util.TimerTask;

import vdrm.base.data.ITask;
import vdrm.rootservice.RootService;

public class ON_TimedTaskWrapper extends TimerTask {
	private ITask task;
	private int estimatedDuration;
	private boolean resume;
	private long startTime;
	private long actualRunTime;
	private long remainingRunTime;
	
	public ON_TimedTaskWrapper(ITask t, int duration){
		this.task = t;
		this.estimatedDuration = duration;
		this.resume = true;
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
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getActualRunTime() {
		return actualRunTime;
	}

	public void setActualRunTime(long actualRunTime) {
		this.actualRunTime = actualRunTime;
	}

	public long getRemainingRunTime() {
		return remainingRunTime;
	}

	public void setRemainingRunTime(long remainingRunTime) {
		this.remainingRunTime = remainingRunTime;
	}

	public boolean canResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}
}
