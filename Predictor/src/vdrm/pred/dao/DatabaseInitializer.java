package vdrm.pred.dao;

import java.util.ArrayList;

import vdrm.base.data.ITask;
import vdrm.base.impl.Task;

public class DatabaseInitializer {

	private static ITaskDao dao = new TaskDao();
	
	public static void main(String[] args) {
		initTasks();
		initHistory();
	}
	
	private static void initHistory() {
		//HARDCODED history :D
		ArrayList<ITask> tasks = dao.getAllTasks();
		
		dao.addTaskToHistory(tasks.get(0));
		dao.addTaskToHistory(tasks.get(1));
		dao.addTaskToHistory(tasks.get(2));
		dao.addTaskToHistory(tasks.get(3));
		dao.addTaskToHistory(tasks.get(5));
		dao.addTaskToHistory(tasks.get(4));
		dao.addTaskToHistory(tasks.get(0));
		dao.addTaskToHistory(tasks.get(1));
		dao.addTaskToHistory(tasks.get(2));
		dao.addTaskToHistory(tasks.get(3));
		dao.addTaskToHistory(tasks.get(1));
		dao.addTaskToHistory(tasks.get(2));
		dao.addTaskToHistory(tasks.get(3));
		dao.addTaskToHistory(tasks.get(4));
		dao.addTaskToHistory(tasks.get(1));
		dao.addTaskToHistory(tasks.get(2));
		dao.addTaskToHistory(tasks.get(3));
		dao.addTaskToHistory(tasks.get(5));
		dao.addTaskToHistory(tasks.get(4));
	}
	
	private static void initTasks() {
		Task t;
		
		t = new Task(300,400,100);
		dao.addTask(t);
		
		t = new Task(400,2800,100);
		dao.addTask(t);
		
		t = new Task(7800,300,100);
		dao.addTask(t);
		
		t = new Task(900,1000,100);
		dao.addTask(t);
		
		t = new Task(1400,500,100);
		dao.addTask(t);
		
		t = new Task(1600,700,100);
		dao.addTask(t);
	}

}
