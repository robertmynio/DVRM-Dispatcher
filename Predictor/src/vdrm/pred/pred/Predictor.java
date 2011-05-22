package vdrm.pred.pred;

import java.util.ArrayList;
import java.util.HashMap;
import vdrm.base.common.IPredictor;
import vdrm.base.data.ITask;
import vdrm.base.impl.Task;
import vdrm.pred.dao.ITaskDao;
import vdrm.pred.dao.TaskDao;
import vdrm.pred.miner.IPatternMiner;
import vdrm.pred.miner.TreePatternMiner;

public class Predictor implements IPredictor{

	IPatternMiner miner;
	HashMap<Integer,ITask> taskMap;
	HashMap<ITask,Integer> idMap;
	ArrayList<ITask> currentTasks;
	private static final double MIN_CREDIBILITY = 0.8;
	private int counter; //add only a limited (MAX_INT) number of tasks to the tree
	
	public Predictor() {
		miner = new TreePatternMiner();
		taskMap = new HashMap<Integer,ITask>();
		idMap = new HashMap<ITask,Integer>();
		currentTasks = new ArrayList<ITask>();
		counter = 0;
		
		//databaseInit(); //COMENTEAZA LINIA ASTA daca nu vrei init din baza de date :D
	}
	
	public synchronized void initialize(ArrayList<ITask> tasks) {
		int c = 0;
		currentTasks = tasks;
		for(ITask t : tasks) {
			c++;
			taskMap.put(c, t);
			idMap.put(t, c);
		}
	}

	public synchronized ArrayList<ITask> predict(ITask task) {
		int id = 0;
		id = getTaskId(task);
		
		ArrayList<Integer> pattern;
		pattern = miner.getPattern(id, MIN_CREDIBILITY);
		if(pattern==null) {
			return null;
		}
		
		int patternSize = pattern.size();
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		ITask temp,aux;
		//we do not include the first task in the prediction, because the first 
		//task is the real task (it is not predicted)
		for(int i=1;i<patternSize;i++) {
			temp = taskMap.get(pattern.get(i));
			aux = new Task(temp.getCpu(),temp.getMem(),temp.getHdd());
			aux.setPredicted(true);
			tasks.add(aux);
		}
		return tasks;
	}

	public synchronized void addEntry(ITask task) {
		
		//add task to tree and dynamically update the tree
		counter++;
		if(counter<Integer.MAX_VALUE) {
			int taskId = getTaskId(task);
			taskMap.put(taskId,task);
			miner.addElement(taskId);
		}
	}
	
	public synchronized void setHistory(ArrayList<ITask> history) {
		for(ITask tsk : history) {
			addEntry(tsk);
		}
	}
	
	private int getTaskId(ITask task) {
		int id = 0;
		for(ITask tsk : currentTasks) {
			if(tsk.equals(task))
				id = idMap.get(tsk);
		}
		return id;
	}
	
	/**
	 * NOT NEEDED FOR NOW
	 */
	private void databaseInit() {
		ITaskDao dao = new TaskDao();
		ArrayList<ITask> history = dao.getTaskHistory();
		for(ITask tsk : history) {
			addEntry(tsk);
		}
	}
}
