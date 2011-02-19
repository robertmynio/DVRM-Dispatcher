package vdrm.base.common;

import vdrm.base.data.IPrediction;
import vdrm.base.data.ITask;

public interface IPredictor {
	public IPrediction predict(ITask task);
	public void addTaskToDatabase(ITask task);
}
