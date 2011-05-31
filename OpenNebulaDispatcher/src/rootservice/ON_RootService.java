package rootservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.Vector;

import javax.xml.bind.JAXB;

import datacenterInterface.dtos.jaxbBindingClasses.ActivityService;
import datacenterInterface.dtos.jaxbBindingClasses.ApplicationDescription;
import datacenterInterface.dtos.jaxbBindingClasses.JaxbPair;
import datacenterInterface.dtos.jaxbBindingClasses.WorkloadSchedule;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.disp.dispatcher.TimedGeneratedTaskWrapper;
import vdrm.disp.dispatcher.TimedTaskWrapper;
import vdrm.disp.onservice.IOpenNebulaService;
import vdrm.disp.onservice.OpenNebulaService;


public class ON_RootService {
	private Map<Timer, ON_TimedTaskWrapper> currentDeployedTasks;
	private Map<Timer, ON_TimedGeneratedTaskWrapper> currentTimedTasks;
	// maybe put a tree map <task.uuid,task> to speed up search :)
	private List<ON_TimedTaskWrapper> readyTasks;
	private static ON_RootService instance;
	private IOpenNebulaService onService;
	
	private ON_RootService(){
		currentDeployedTasks = Collections.synchronizedMap(new HashMap<Timer, ON_TimedTaskWrapper>());
		currentTimedTasks = Collections.synchronizedMap(new HashMap<Timer, ON_TimedGeneratedTaskWrapper>());
		readyTasks = new Vector<ON_TimedTaskWrapper>();
		BaseCommon.Instance().ResourcesAvailable = true;
		//initialize OpenNebula
		onService = new OpenNebulaService();
		
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
							System.out.println("### Pause task event arrived.");
							PauseTimerForTask(arg1);
					}
					
				});
		BaseCommon.Instance().getTaskEndedMigrating().addObserver(
				new Observer(){

					@Override
					public void update(Observable arg0, Object arg1) {
						if(arg1 != null){
							System.out.println("### Resume task event arrived.");
							ResumeTimerForTask(arg1);
						}
					}
					
				});
	}
	
	public static ON_RootService Instance(){
		if(instance == null){
			instance = new ON_RootService();
		}
		return instance;
	}
	protected void ParseGeneratedTask(Object generatedTask) {
		ITask newTask;
		int duration = 0;
		JaxbPair task = (JaxbPair)generatedTask;
		if(task.getApplication() != null && task.getStartDelay() != null){
			ApplicationDescription appDescription = task.getApplication();
			ActivityService taskDescription;
			if(appDescription.getActivities()[0] != null){
				taskDescription = appDescription.getActivities()[0].getServices()[0];
				
				// set task duration
				duration = appDescription.getActivities()[0].getDuration() * 1000;
				
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
				
				if(taskDescription.getRequestedMemoryMax() <= 1000
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
			}
			
		}
		
	}
	private void TaskArrived(ITask t, int duration) {
		ON_TimedTaskWrapper tt = new ON_TimedTaskWrapper(t,duration);
		
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
		for (ON_TimedTaskWrapper item:readyTasks){
			if( !((Task)item.getTask()).isDeployedOrdered() ){
				((Task)item.getTask()).setDeployedOrdered(true);
				return item.getTask();
			}
		}
		return null;
	}

	private void SendTaskCommand(ITask task) {
		if(task != null){
			((Task)task).setDeployedOrdered(true);
			IServer serv = new Server();
			onService.DeployTask(task, false, serv);
		}
	}

	protected void StartTask(ITask t) {
		Timer timer = new Timer();
		boolean found = false;
		// Step1: get the timedTaskWrapper from readyTasks
		int index = 0;
		for (ON_TimedTaskWrapper item:readyTasks){
			
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
		
		ON_TimedTaskWrapper tt = null;
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
		
	}

	public void TaskIsDone(ON_TimedTaskWrapper t) {
		onService.FinishTask(t.getTask());

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
				//SendTaskCommand(readyTasks.get(0).getTask());
				SendTaskCommand(GetFirstUndeployedTask());
			}
		}
		
	}
	
	public void ParseWorkloadXML(String path){
		FileInputStream fin = null;
		WorkloadSchedule workloadSchedule;
		ON_TimedGeneratedTaskWrapper tt;
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
					tt = new ON_TimedGeneratedTaskWrapper(pair);
					
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
		ON_TimedTaskWrapper tt = null;
		Timer time = null;
		synchronized (currentDeployedTasks) {
			ITask pausedTask = (ITask)t;
			// stop the timer thread
			Set set = currentDeployedTasks.entrySet();
		    Iterator i = set.iterator();
		    
		    while(i.hasNext()){
		        Map.Entry me = (Map.Entry)i.next();
		        tt = (ON_TimedTaskWrapper)me.getValue();
		        if( tt.getTask().getTaskHandle().toString().compareTo(pausedTask.getTaskHandle().toString())==0){
		        	time = (Timer)me.getKey();
		        	
		        	System.out.println("@@@ Task will be paused, migration STARTED " + tt.getTask().getTaskHandle().toString());
		        	try {
		        		// cancel the timer
		        		time.cancel();
		        		// set the actual run time (until now)
		        		tt.setActualRunTime(System.currentTimeMillis() - tt.getStartTime());
		        		
		        		// set the remaining running time
		        		if((tt.getEstimatedDuration()) - tt.getActualRunTime() > 0)
		        			tt.setRemainingRunTime( (tt.getEstimatedDuration()) - tt.getActualRunTime());
		        		else{
		        			tt.setRemainingRunTime(0);
		        			tt.setResume(false);
		        		}
		        		System.out.println("@@@ Remaining time: " + tt.getRemainingRunTime() + ", with actual run time of " + tt.getActualRunTime() + "and original estimated time of " + tt.getEstimatedDuration() );
		    		}catch(Exception e){
		    			e.printStackTrace();
		    		}
		        	break;
		        }
		    }
		    //WaitForTaskMigration(tt, time);
			//currentDeployedTasks.remove(t);
		}
	}
	
	protected synchronized void ResumeTimerForTask(Object t) {
		ON_TimedTaskWrapper tt = null;
		Timer time = null;
		synchronized (currentDeployedTasks) {
			ITask pausedTask = (ITask)t;
			// stop the timer thread
			Set set = currentDeployedTasks.entrySet();
		    Iterator i = set.iterator();
		    
		    while(i.hasNext()){
		        Map.Entry me = (Map.Entry)i.next();
		        tt = (ON_TimedTaskWrapper)me.getValue();
		        if( tt.getTask().getTaskHandle().toString().compareTo(pausedTask.getTaskHandle().toString())==0){
		        	
		        	time = (Timer)me.getKey();
		        	currentDeployedTasks.remove(time);
        			
        			// kill it!
        			time.purge();
		        	break;
		        }
		    }
		    try{
        		//System.out.println("@@@ Task resuming, migration FINISHED " + tt.getTask().getTaskHandle().toString());
        		synchronized (currentDeployedTasks) {
        			
        			if(tt.canResume() && tt.getRemainingRunTime() > 0){
        				// search for duplicates 
        				i = set.iterator();
        				boolean found = false;
        				while(i.hasNext()){
        					Map.Entry me = (Map.Entry)i.next();
        			        tt = (ON_TimedTaskWrapper)me.getValue();
        			        if( tt.getTask().getTaskHandle().toString().compareTo(pausedTask.getTaskHandle().toString())==0){
        			        	found = true;
        			        	break;
        			        }
        				}
        				if(!found){
        					System.out.println("@@@ Task resuming, migration FINISHED (no duplicates)" + tt.getTask().getTaskHandle().toString());
		        			Timer newtime = new Timer();
		        			ON_TimedTaskWrapper newTask = new ON_TimedTaskWrapper(tt.getTask(), (int) tt.getRemainingRunTime());
		        			newTask.setRemainingRunTime(tt.getRemainingRunTime());
		        			newTask.setStartTime(tt.getStartTime());
		        			newTask.setActualRunTime(tt.getActualRunTime());
		        			newtime.schedule(newTask, newTask.getRemainingRunTime());
		        			currentDeployedTasks.put(newtime, tt);
        				}else{
        					System.out.println("@@@ Task NOT resuming, migration FINISHED (duplicate found)" + tt.getTask().getTaskHandle().toString());
        				}
        			}
        		}
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
		    
		}
	}
}
