package rootservice;

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

import datacenterInterface.dtos.jaxbBindingClasses.ActivityService;
import datacenterInterface.dtos.jaxbBindingClasses.ApplicationDescription;
import datacenterInterface.dtos.jaxbBindingClasses.JaxbPair;

import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Task;
import vdrm.onservice.IOpenNebulaService;
import vdrm.onservice.OpenNebulaService;


public class ON_RootService {
	private Map<Timer, ON_TimedTaskWrapper> currentDeployedTasks;
	// maybe put a tree map <task.uuid,task> to speed up search :)
	private List<ON_TimedTaskWrapper> readyTasks;
	private static ON_RootService instance;
	private IOpenNebulaService onService;
	
	private ON_RootService(){
		currentDeployedTasks = Collections.synchronizedMap(new HashMap<Timer, ON_TimedTaskWrapper>());
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
			SendTaskCommand(readyTasks.get(0).getTask());
		}
	}

	private void SendTaskCommand(ITask task) {
		// TODO Auto-generated method stub
		onService.DeployTask(task);
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
		}
		
	}
}
