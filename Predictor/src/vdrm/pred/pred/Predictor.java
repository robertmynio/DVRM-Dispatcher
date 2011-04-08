package vdrm.pred.pred;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import vdrm.base.common.IPredictor;
import vdrm.base.data.IPrediction;
import vdrm.base.data.ITask;
import vdrm.base.impl.Prediction;
import vdrm.pred.miner.IPatternMiner;
import vdrm.pred.miner.TreePatternMiner;

public class Predictor implements IPredictor{

	IPatternMiner miner;
	HashMap<ITask,Byte> taskToByteMap;
	HashMap<Byte,ITask> byteToTaskMap;
	private static final double MIN_CREDIBILITY = 0.7;
	
	public Predictor() {
		miner = new TreePatternMiner();
		taskToByteMap = new HashMap<ITask,Byte>();
		byteToTaskMap = new HashMap<Byte,ITask>();
	}

	public IPrediction predict(ITask task) {
		Byte b = taskToByteMap.get(task);
		if(b==null)
			return null;
		
		ArrayList<Byte> pattern;
		pattern = miner.getPattern(b, MIN_CREDIBILITY);
		if(pattern==null)
			return null;
		
		int patternSize = pattern.size();
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		ITask temp;
		//we do not include the first task in the prediction, because the first 
		//task is the real task (it is not predicted)
		for(int i=1;i<patternSize;i++) {
			temp = byteToTaskMap.get(pattern.get(i));
			tasks.add(temp);
		}
		IPrediction pred = new Prediction(tasks);
		return pred;
	}

	@Override
	public void addTaskToDatabase(ITask task) {
		//add task to relational database
		
		// TODO  DO IT!
		
		//add task to tree and dynamically update the tree
		
		// TODO  DO IT!
		
	}

}
