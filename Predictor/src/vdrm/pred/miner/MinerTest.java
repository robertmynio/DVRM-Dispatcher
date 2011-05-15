package vdrm.pred.miner;

import java.util.ArrayList;

import vdrm.base.data.ITask;
import vdrm.base.impl.Task;
import vdrm.pred.dao.TaskDao;
import vdrm.pred.pred.Predictor;


public class MinerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Predictor pred = new Predictor();
		//TaskDao dao = new TaskDao();
		
		ITask task;
		ArrayList<ITask> tasks = new ArrayList<ITask>();// = dao.getAllTasks();
		task = new Task(500,2500,25000);
		tasks.add(task);
		task = new Task(1750,600,6000);
		tasks.add(task);
		task = new Task(700,600,6000);
		tasks.add(task);
		task = new Task(700,752,10000);
		tasks.add(task);
		task = new Task(1100,500,5000);
		tasks.add(task);
		task = new Task(1350,800,8000);
		tasks.add(task);
		
		ArrayList<ITask> taskHistory = new ArrayList<ITask>();// = dao.getTaskHistory();
		taskHistory.add(tasks.get(0));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(0));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		
		taskHistory.add(tasks.get(5));	//remove this line and the following 5 to make (5,4) and (1,2,3) credibility 100%
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(1));
		
		pred.initialize(tasks);
		pred.setHistory(taskHistory);
		
		
		ArrayList<ITask> prediction = null;
		
		prediction = pred.predict(tasks.get(4));
		taskHistory.add(tasks.get(4));
		prediction = pred.predict(tasks.get(3));
		taskHistory.add(tasks.get(3));
		prediction = pred.predict(tasks.get(0));
		taskHistory.add(tasks.get(0));
		prediction = pred.predict(tasks.get(1));
		taskHistory.add(tasks.get(1));
		prediction = pred.predict(tasks.get(2));
		taskHistory.add(tasks.get(2));
		prediction = pred.predict(tasks.get(3));
		taskHistory.add(tasks.get(3));
	}

}
