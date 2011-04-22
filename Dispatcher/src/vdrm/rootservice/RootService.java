package vdrm.rootservice;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TreeMap;

import vdrm.base.common.IAlgorithm;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.BaseCommon.VMStartedEvent;
import vdrm.disp.alg.Algorithm1;

public class RootService {
	private SortedMap currentDeployedTasks;
	
	// maybe put a tree map <task.uuid,task> to speed up search :)
	private ArrayList<TimedTaskWrapper> readyTasks;
	private IAlgorithm worker;
	
	private static RootService instance;
	
	private RootService(){
		currentDeployedTasks = new TreeMap<Timer, TimedTaskWrapper>();
		readyTasks = new ArrayList<TimedTaskWrapper>();
		worker = new Algorithm1();
		
		// register the in line event handler
		BaseCommon.Instance().getVMStarted().addObserver(
				new Observer(){

					@Override
					public void update(Observable o, Object arg) {
						if(arg != null){
							StartTask((ITask)arg);
						}
					}
					
				}
				);
	}
	
	public static RootService Instance(){
		if(instance == null){
			instance = new RootService();
		}
		return instance;
	}
	
	/***
	 * This calls the EndTask method.
	 * @param t
	 */
	public void TaskIsDone(TimedTaskWrapper t){
		worker.endTask(t.getTask());
		
		// remove the timedTaskWrapper
		currentDeployedTasks.remove(t);
	}
	/***
	 * New Task just arrived (probably from WS). Store it in the DB (maybe)
	 * @param t
	 */
	public void TaskArrived(ITask t, int duration){
		TimedTaskWrapper tt = new TimedTaskWrapper(t,duration);
		readyTasks.add(tt);
	}
	
	/***
	 * Start deployment of task, by calling Algorithm NewTask
	 * @param t
	 */
	public void StartTask(ITask t){
		Timer timer = new Timer();
		// Step1: get the timedTaskWrapper from readyTasks
		
		// Step2: add timedtaskwrapper to currentDeployedTasks
		
		// Step3: schedule the task to run until it expires
		//currentDeployedTasks.put(arg0, arg1)
		
		// have fun :)
	}
	
	
}
