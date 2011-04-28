package vdrm.base.common;

import java.util.ArrayList;

import vdrm.base.data.ITask;

public interface IPredictor {
	public void initialize(ArrayList<ITask> tasks);
	public void setHistory(ArrayList<ITask> history);
	public ArrayList<ITask> predict(ITask task);
	public void addEntry(ITask task);
}
