package vdrm.base.impl;

import java.util.ArrayList;

import vdrm.base.data.IPrediction;
import vdrm.base.data.ITask;

public class Prediction implements IPrediction {
	private ArrayList<ITask> predictedTasks;
	
	public Prediction(){
		predictedTasks = new ArrayList<ITask>();
	}
	
	public Prediction(ArrayList<ITask> tasks) {
		predictedTasks = tasks;
	}
	
	public ArrayList<ITask> getPredictedTasks() {
		return predictedTasks;
	}
}
