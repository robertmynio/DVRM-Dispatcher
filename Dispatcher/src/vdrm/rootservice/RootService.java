package vdrm.rootservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TreeMap;
import java.util.Vector;

import vdrm.base.common.IAlgorithm;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.BaseCommon.VMStartedEvent;
import vdrm.disp.alg.Algorithm1;

/***
 * Root Service class.
 * 
 * The way it works: 
 * 1) A new task arrives (under the form of a web service call, which results in a 
 * call to TaskArrived.
 * 2) The task is added to a 'ready' queue, where it remains until the VM associated to it
 * is started. The Root Service calls the algorithm NewTask method.
 * 3) When the OpenNebula service starts the VM and the task is dispatched successfully,
 * the VMStartedEvent is fired and is handled by the RootService
 * 4) When the VMStartedEvent occured, the task is moved in the 'current' working tasks. This
 * is done in the StartTask method. This method removes the task from the 'ready' list,
 * adds it to the 'current' deployed tasks and starts its associated timer.
 * 5) When the timer associated to a task finishes, it calls the TaskIsDone method, which in turn
 * signals the algorithm with a call to 'EndTask' method.
 * @author Vlad & Robi
 *
 */
public class RootService {
	private Map<Timer, TimedTaskWrapper> currentDeployedTasks;
	
	// maybe put a tree map <task.uuid,task> to speed up search :)
	private List<TimedTaskWrapper> readyTasks;
	public IAlgorithm worker;
//	private boolean ResourcesAvailable;
	private static RootService instance;
	
	private RootService(){
		currentDeployedTasks = Collections.synchronizedMap(new HashMap<Timer, TimedTaskWrapper>());
		readyTasks = new Vector<TimedTaskWrapper>();
		worker = new Algorithm1();
		
		BaseCommon.Instance().ResourcesAvailable = true;
		
		// register the in line event handlers
		BaseCommon.Instance().getVMStarted().addObserver(
				new Observer(){
					public void update(Observable o, Object arg) {
						if(arg != null){
							StartTask((ITask)arg);
						}
					}
				}
				);
		
		
		BaseCommon.Instance().getResourceAllocateEvent().addObserver(
				new Observer(){


					public void update(Observable arg0, Object arg1) {
						BaseCommon.Instance().ResourcesAvailable = false;
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
	public synchronized void TaskIsDone(TimedTaskWrapper t){
		worker.endTask(t.getTask());

        if(!BaseCommon.Instance().ResourcesAvailable){
            BaseCommon.Instance().ResourcesAvailable = true;
        }
		// remove the timedTaskWrapper
		synchronized (currentDeployedTasks) {
			// stop the timer thread
			Set set = currentDeployedTasks.entrySet();
		    Iterator i = set.iterator();
		    
		    while(i.hasNext()){
		        Map.Entry me = (Map.Entry)i.next();
		        if(me.getValue().equals(t)){
		        	Timer time = (Timer)me.getKey();
		        	time.cancel();
		        	break;
		        }
		    }
			currentDeployedTasks.remove(t);
		}

		if(!BaseCommon.Instance().ResourcesAvailable){
			// TODO: Step1:iterate through the list of readyTasks and send them all to the worker
			while(!readyTasks.isEmpty() && BaseCommon.Instance().ResourcesAvailable == false){
				StartTask( ((TimedTaskWrapper)readyTasks.get(0)).getTask() );
			}
			// Step2: make it true!
			BaseCommon.Instance().ResourcesAvailable = true;
			
		}
	}
	/***
	 * New Task just arrived (probably from WS). Store it in the DB (maybe)
	 * and pass it to the algorithm.
	 * @param t
	 */
	public synchronized void TaskArrived(ITask t, int duration){
		TimedTaskWrapper tt = new TimedTaskWrapper(t,duration);
		
		synchronized (readyTasks) {
			readyTasks.add(tt);	
		}
		
		if(BaseCommon.Instance().ResourcesAvailable){
				//StartTask( ((TimedTaskWrapper)readyTasks.get(0)).getTask() );
			SendTaskCommand(t);
		}
	}
	
	
	
	/***
	 * Start timer of task
	 * @param t
	 */
	public synchronized void StartTask(ITask t){
		Timer timer = new Timer();
		boolean found = false;
		// Step1: get the timedTaskWrapper from readyTasks
		int index = 0;
		for (TimedTaskWrapper item:readyTasks){
			
//			if(item.getTask().getTaskHandle().toString().compareTo(t.getTaskHandle().toString()) == 0){
//				index =  readyTasks.indexOf(item);
//                found = true;
//				break;
//			}
			if( item.getTask().equals(t) ){
				index =  readyTasks.indexOf(item);
                found = true;
				break;
			}
		}
		
		TimedTaskWrapper tt = null;
        if(found){
            if(readyTasks.size()>0){
                synchronized (readyTasks) {
                    tt =  readyTasks.get(index);
                    readyTasks.remove(tt);
                 }
                // Step2: schedule the task to run until it expires
                timer.schedule(tt, tt.getEstimatedDuration());


                // Step3: add timedtaskwrapper to currentDeployedTasks
                synchronized (currentDeployedTasks) {
                    currentDeployedTasks.put(timer, tt);
                }
            }else{
                return;
            }
        }

		// have fun :)
		//SendTaskCommand(t);
	}
	
	public void SendTaskCommand(ITask t){
		worker.newTask(t);
	}
}
