package vdrm.base.common;

import java.util.ArrayList;

import vdrm.base.data.ITask;

public interface IPredictor {
	public ArrayList<ITask> predict(ITask task);
	public void addTaskToDatabase(ITask task);
}
