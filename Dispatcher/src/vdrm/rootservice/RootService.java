package vdrm.rootservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import javax.xml.bind.JAXB;

import datacenterInterface.dtos.jaxbBindingClasses.ActivityDescription;
import datacenterInterface.dtos.jaxbBindingClasses.ActivityService;
import datacenterInterface.dtos.jaxbBindingClasses.ApplicationDescription;
import datacenterInterface.dtos.jaxbBindingClasses.JaxbPair;
import datacenterInterface.dtos.jaxbBindingClasses.WorkloadSchedule;

import vdrm.base.common.IAlgorithm;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Task;
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
	private Map<Timer, TimedGeneratedTaskWrapper> currentTimedTasks;
	// maybe put a tree map <task.uuid,task> to speed up search :)
	private List<TimedTaskWrapper> readyTasks;
	public IAlgorithm worker;
//	private boolean ResourcesAvailable;
	private static RootService instance;
	
	private RootService(){
		currentDeployedTasks = Collections.synchronizedMap(new HashMap<Timer, TimedTaskWrapper>());
		currentTimedTasks = Collections.synchronizedMap(new HashMap<Timer, TimedGeneratedTaskWrapper>());
		readyTasks = new Vector<TimedTaskWrapper>();
		worker = new Algorithm1();
		
		BaseCommon.Instance().ResourcesAvailable = true;
		
		// register the in line event handlers
		BaseCommon.Instance().getVMStarted().addObserver(
				new Observer(){
					public void update(Observable o, Object arg) {
						if(arg != null){
							System.out.println("*** Deployed for task with UUID: " + ((ITask) arg).getTaskHandle().toString());
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
		
		BaseCommon.Instance().getTaskGeneratedEvent().addObserver(
				new Observer(){

					@Override
					public void update(Observable arg0, Object arg1) {
						if(arg1 != null){
							ParseGeneratedTask(arg1);
						}
					}
					
				}
		);
		
		BaseCommon.Instance().getXMLTaskGenerated().addObserver(
				new Observer(){

					@Override
					public void update(Observable arg0, Object arg1) {
						
						if(arg1 != null){
							// remove the timedTaskWrapper
							synchronized (currentTimedTasks) {
								// stop the timer thread
								Set set = currentTimedTasks.entrySet();
							    Iterator i = set.iterator();
							    
							    while(i.hasNext()){
							        Map.Entry me = (Map.Entry)i.next();
							        if(me.getValue().equals((TimedGeneratedTaskWrapper)arg1)){
							        	Timer time = (Timer)me.getKey();
							        	time.cancel();
							        	break;
							        }
							    }
							    currentTimedTasks.remove((TimedGeneratedTaskWrapper)arg1);
							}
							ParseGeneratedTask(((TimedGeneratedTaskWrapper)arg1).getTask());
						}
					}
					
				}
		);
		
		BaseCommon.Instance().getTaskStartedMigrating().addObserver(
				new Observer(){

					@Override
					public void update(Observable arg0, Object arg1) {
						if(arg1 != null)
							PauseTimerForTask(arg1);
					}
					
				});
		BaseCommon.Instance().getTaskEndedMigrating().addObserver(
				new Observer(){

					@Override
					public void update(Observable arg0, Object arg1) {
						if(arg1 != null)
							ResumeTimerForTask(arg1);
					}
					
				});
	}
	
	

	public static RootService Instance(){
		if(instance == null){
			instance = new RootService();
		}
		return instance;
	}
	
	/***
	 * 0753 775 107
	 * This method parses the task generated by the WorkloadGenerator
	 * @param generatedTask
	 */
	private synchronized void ParseGeneratedTask(Object generatedTask){
		ITask newTask;
		int duration = 0;
		JaxbPair task = (JaxbPair)generatedTask;
		if(task.getApplication() != null && task.getStartDelay() != null){
			ApplicationDescription appDescription = task.getApplication();
			ActivityService taskDescription;
			if(appDescription.getActivities()[0] != null){
				taskDescription = appDescription.getActivities()[0].getServices()[0];
				
				// set task duration
				duration = appDescription.getActivities()[0].getDuration() * 100;
				
				// set task requirements
				int cpu, mem, hdd;
				
				if(taskDescription.getRequestedCPUMax() < 1000
						|| ((taskDescription.getRequestedCPUMax() + taskDescription.getRequestedCPUMin())/2) < 500 )
					cpu = taskDescription.getRequestedCPUMax();
				else{
					cpu = (taskDescription.getRequestedCPUMax() + taskDescription.getRequestedCPUMin())/2;
//					if(cpu < 1000)
//						cpu *= 5;
				}
				
				//cpu = taskDescription.getRequestedCPUMin();
				if(taskDescription.getRequestedMemoryMax() < 1000){
					mem = taskDescription.getRequestedMemoryMax();
					
				}else{
					if(((taskDescription.getRequestedMemoryMax() + taskDescription.getRequestedMemoryMin())/2) < 300
							&& taskDescription.getRequestedMemoryMin() > 300){
						mem = taskDescription.getRequestedMemoryMin();
					}else{
						mem = (taskDescription.getRequestedMemoryMax() + taskDescription.getRequestedMemoryMin())/2;
					}
					//mem = taskDescription.getRequestedMemoryMin();
				}
				
				if(taskDescription.getRequestedMemoryMax() < 1000
						|| ((taskDescription.getRequestedMemoryMax() + taskDescription.getRequestedMemoryMin())/2) < 1000){
					hdd = taskDescription.getRequestedMemoryMax()*10;
				}else{
					hdd = (taskDescription.getRequestedMemoryMax() + taskDescription.getRequestedMemoryMin())/2;
					hdd *= 5;
					//mem = taskDescription.getRequestedMemoryMin();
				}
				//hdd = taskDescription.getRequestedMemoryMin()*10;
				//hdd = (taskDescription.getRequestedStorageMax() + taskDescription.getRequestedStorageMin())/2;
				//hdd = (taskDescription.getRequestedStorageMin());
				
				// build the task
				newTask = new Task(cpu,mem,hdd);
				
				TaskArrived(newTask, duration);
				System.out.println("Newtask has arrived with UUID: " + newTask.getTaskHandle().toString());
			}
			
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
			//SendTaskCommand(readyTasks.get(0).getTask());
			SendTaskCommand(GetFirstUndeployedTask());
		}
	}
	
	private ITask GetFirstUndeployedTask(){
		for (TimedTaskWrapper item:readyTasks){
			if( !((Task)item.getTask()).isDeployedOrdered() ){
				((Task)item.getTask()).setDeployedOrdered(true);
				return item.getTask();
			}
		}
		return null;
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
			
			if(item.getTask().getTaskHandle().toString().compareTo(t.getTaskHandle().toString()) == 0){
				index =  readyTasks.indexOf(item);
				found = true;
				break;
			}
//			if( item.getTask().equals(t) ){
//				index =  readyTasks.indexOf(item);
//                found = true;
//				break;
//			}
		}
		
		TimedTaskWrapper tt = null;
        if(found){
        	System.out.println("Newtask has started with UUID: " + t.getTaskHandle().toString());
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
        }else{
        	System.out.println("Newtask has NOT started with UUID: " + t.getTaskHandle().toString());
        }

		// have fun :)
		//SendTaskCommand(t);
	}
	
	public void SendTaskCommand(ITask t){
		if(t != null){
			((Task)t).setDeployedOrdered(true);
			worker.newTask(t);
		}
	}
	
	/***
	 * This calls the EndTask method.
	 * @param t
	 */
	public synchronized void TaskIsDone(TimedTaskWrapper t){
		System.out.println("Newtask has ended with UUID: " + t.getTask().getTaskHandle().toString() + "    ***");
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
			
			if(readyTasks.size() > 0){
				SendTaskCommand(GetFirstUndeployedTask());
				//SendTaskCommand(readyTasks.get(0).getTask());
				
			}
		}
		
		
		

//		if(!BaseCommon.Instance().ResourcesAvailable){
//			// TODO: Step1:iterate through the list of readyTasks and send them all to the worker
//			while(!readyTasks.isEmpty() && BaseCommon.Instance().ResourcesAvailable == false){
//				StartTask( ((TimedTaskWrapper)readyTasks.get(0)).getTask() );
//			}
//			// Step2: make it true!
//			BaseCommon.Instance().ResourcesAvailable = true;
//			
//		}
	}
	
	public void ParseWorkloadXML(String path){
		FileInputStream fin = null;
		WorkloadSchedule workloadSchedule;
		TimedGeneratedTaskWrapper tt;
		try{
			 fin = new FileInputStream(path);
		}catch(FileNotFoundException e){
			System.out.println("Aww shit");
		}
		
		if(fin != null){
			workloadSchedule = JAXB.unmarshal(fin, WorkloadSchedule.class);
			if(workloadSchedule != null){
				JaxbPair[] tasks = workloadSchedule.getApplications();
				for(JaxbPair pair:tasks){
					long startDelay = pair.getStartDelay();
					tt = new TimedGeneratedTaskWrapper(pair);
					
					// compute the start delay, starting from now
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.SECOND, (int)startDelay);
					
					// schedule the timer
					Timer timer = new Timer();
					currentTimedTasks.put(timer, tt);
					timer.schedule(tt, cal.getTime());
				}
			}
		}
	}
	
	protected synchronized void PauseTimerForTask(Object t) {
		synchronized (currentDeployedTasks) {
			// stop the timer thread
			Set set = currentDeployedTasks.entrySet();
		    Iterator i = set.iterator();
		    
		    while(i.hasNext()){
		        Map.Entry me = (Map.Entry)i.next();
		        if(me.getValue().equals((ITask)t)){
		        	Timer time = (Timer)me.getKey();
//		        	try {
//						time.wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
		        	break;
		        }
		    }
			currentDeployedTasks.remove(t);
		}
	}
	
	protected synchronized void ResumeTimerForTask(Object t) {
		synchronized (currentDeployedTasks) {
			// stop the timer thread
			Set set = currentDeployedTasks.entrySet();
		    Iterator i = set.iterator();
		    
		    while(i.hasNext()){
		        Map.Entry me = (Map.Entry)i.next();
		        if(me.getValue().equals((ITask)t)){
		        	Timer time = (Timer)me.getKey();
//		        	try {
//						// time start
//		        		
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
		        	break;
		        }
		    }
			currentDeployedTasks.remove(t);
		}
	}
}
