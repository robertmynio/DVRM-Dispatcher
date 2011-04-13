package vdrm.pred.pred;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import vdrm.base.common.IPredictor;
import vdrm.base.data.ITask;
import vdrm.pred.miner.IPatternMiner;
import vdrm.pred.miner.TreePatternMiner;

public class Predictor implements IPredictor{

	IPatternMiner miner;
	HashMap<UUID,ITask> taskMap;
	private static final double MIN_CREDIBILITY = 0.7;
	
	public Predictor() {
		miner = new TreePatternMiner();
		taskMap = new HashMap<UUID,ITask>();
		
		//Database Init method call here ?
	}
	
	public Predictor(ArrayList<ITask> tasks) {
		this();
		Initialize(tasks);
	}

	public ArrayList<ITask> predict(ITask task) {
		UUID id = task.getTaskHandle();
		
		ArrayList<UUID> pattern;
		pattern = miner.getPattern(id, MIN_CREDIBILITY);
		if(pattern==null)
			return null;
		
		int patternSize = pattern.size();
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		ITask temp;
		//we do not include the first task in the prediction, because the first 
		//task is the real task (it is not predicted)
		for(int i=1;i<patternSize;i++) {
			temp = taskMap.get(pattern.get(i));
			tasks.add(temp);
		}
		return tasks;
	}

	@Override
	public void addTaskToDatabase(ITask task) {
		//add task to relational database
		
		// TODO  DO IT!
		
		//add task to tree and dynamically update the tree
		UUID taskId = task.getTaskHandle();
		taskMap.put(taskId,task);
		miner.addElement(taskId);
	}
	
	private void Initialize(ArrayList<ITask> tasks) {
		UUID taskId;
		for(ITask tsk : tasks) {
			taskId = tsk.getTaskHandle();
			taskMap.put(taskId, tsk);
			miner.addElement(taskId);
		}
	}

}
