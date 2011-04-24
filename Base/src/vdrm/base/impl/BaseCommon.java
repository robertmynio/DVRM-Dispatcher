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
	
	
	
	private int nrOfTasksThreshold;
	
	public static Boolean logEnabled = false;
	
	private static BaseCommon instance;
	
	public VMStartedEvent VMStarted;
	public ResourceAllocationEvent ResourceAllocateEvent;
	
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
	 * Event which is fired when the VirtualMachine is deployed and started
	 * @author Vlad
	 *
	 */
	public class VMDeployedEvent extends EventObject{

		public VMDeployedEvent(Object arg0) {
			super(arg0);
		}
	}
	
	
	
	/***
	 * Event Listener for the VirtualMachine deploy event
	 * @author Vlad
	 *
	 */
	public interface VMDeployedEventListener extends EventListener{
		public void vmDeployedOccured(VMDeployedEvent ev);
	}
	
	
}
