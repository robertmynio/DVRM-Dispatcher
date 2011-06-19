package vdrm.base.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Observable;

import vdrm.base.data.IServer;

public class BaseCommon {
	public static ArrayList<IServer> notFullServers;
	public static ArrayList<IServer> fullServers;
	public static ArrayList<IServer> emptyServers;
	
	public static final double CPU_WEIGHT = 0.6;
	public static final double MEM_WEIGHT = 0.3;
	public static final double HDD_WEIGHT = 0.1;
	
	public static final double PERCENT = 0.1;
	public static final double SERVER_THRESHOLD = 0.2;
	
	public static boolean ONEnabled = false;
	public static boolean PSEnabled = false;
	public static boolean PredictionEnabled = true;
	public static boolean logEnabled = false;
	
	private int nrOfTasksThreshold;

    public boolean ResourcesAvailable;
    public boolean ServerStarting;
	
	private static BaseCommon instance;
	
	public VMStartedEvent VMStarted;
	public ResourceAllocationEvent ResourceAllocateEvent;
	public TaskGeneratedEvent TaskGenerated;
	public TaskStartedMigrationEvent TaskStartedMigrating;
	public TaskEndedMigrationEvent TaskEndedMigrating;
	public XMLTaskGeneratedEvent XMLTaskGenerated;
	
	public static BaseCommon Instance(){
		if(instance == null){
			instance = new BaseCommon();
		}
		return instance;
	}
	
	private BaseCommon(){
		//notFullServers = new ArrayList<IServer>();
		//fullServers = new ArrayList<IServer>();
		//emptyServers = new ArrayList<IServer>();
		nrOfTasksThreshold = 3;
		VMStarted = new VMStartedEvent();
		ResourceAllocateEvent = new ResourceAllocationEvent();
		TaskGenerated = new TaskGeneratedEvent();
		TaskStartedMigrating = new TaskStartedMigrationEvent();
		TaskEndedMigrating = new TaskEndedMigrationEvent();
		XMLTaskGenerated = new XMLTaskGeneratedEvent();
		
		ResourcesAvailable = true;
		ServerStarting = false;
	}

	public int getNrOfTasksThreshold() {
		return nrOfTasksThreshold;
	}
	
	public VMStartedEvent getVMStarted() {
		return VMStarted;
	}
	
	public ResourceAllocationEvent getResourceAllocateEvent() {
		return ResourceAllocateEvent;
	}

	public TaskGeneratedEvent getTaskGeneratedEvent() {
		return TaskGenerated;
	}
	
	public TaskStartedMigrationEvent getTaskStartedMigrating() {
		return TaskStartedMigrating;
	}

	public TaskEndedMigrationEvent getTaskEndedMigrating() {
		return TaskEndedMigrating;
	}

	public XMLTaskGeneratedEvent getXMLTaskGenerated() {
		return XMLTaskGenerated;
	}

	/**
	 * New style event :)
	 * Event Listener for the VirtualMachine deploy event
	 * @author Vlad
	 *
	 */
	public class VMStartedEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
	
	/***
	 * Event which gets fired when there are no more hardware resources available
	 * @author Vlad
	 *
	 */
	public class ResourceAllocationEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
	
	/***
	 * Event which gets fired when a new task is generated by the Workload Generator
	 * @author Vlad
	 *
	 */
	public class TaskGeneratedEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
	
	/***
	 * Event which gets fired when a new task is generated by the Workload Generator
	 * @author Vlad
	 *
	 */
	public class XMLTaskGeneratedEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
	
	/***
	 * Event which gets fired when a tasks starts migrating to another server
	 * ( to pause the timer )
	 * @author Vlad
	 *
	 */
	public class TaskStartedMigrationEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
	
	/***
	 * Event which gets fired when a tasks ends migrating to another server
	 * ( to resume the timer )
	 * @author Vlad
	 *
	 */
	public class TaskEndedMigrationEvent extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
}
