package vdrm.base.impl;

import java.util.ArrayList;

import vdrm.base.data.IPrediction;
import vdrm.base.data.ITask;

public class Prediction implements IPrediction {
	private int predictionCredibility;
	private ArrayList<ITask> predictedTasks;
	
	
	public Prediction(){
		predictedTasks = new ArrayList<ITask>();
		predictionCredibility = 100;
	}
	
	@Override
	public void decreaseCredibility() {
		predictionCredibility--; // or -= 5 ??
	}

	@Override
	public int getCredibility() {
		return predictionCredibility;
	}

	@Override
	public ArrayList<ITask> getPredictedTasks() {
		return predictedTasks;
	}

	@Override
	public void increaseCredibility() {
		predictionCredibility++; // or += 5 ??
	}

	@Override
	public void addPredictedTask(ITask predictedTask) {
		this.predictedTasks.add(predictedTask);
	}
	
	

}
