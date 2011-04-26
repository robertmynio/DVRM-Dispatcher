package vdrm.pred.dao;

import java.util.ArrayList;

import vdrm.base.data.ITask;

public interface ITaskDao {
	public ArrayList<ITask> getAllTasks();
	public void addTask(ITask task);
	public ArrayList<ITask> getTaskHistory();
	public void addTaskToHistory(ITask task);
}
