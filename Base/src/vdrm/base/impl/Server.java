package vdrm.base.impl;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.util.UniqueIdGenerator;
/***
 * TODO: Add logger to each catch clause
 * @author Gygabite
 *
 */
public class Server implements IServer {
	
	// SERVER CHARACTERISTICS
	private int cpuFreq;
	private int memoryAmount;
	private int hddSize;
	private int cores;
	public int coreFreq;
	
	private int usedCPU;
	private int usedRAM;
	private int usedHDD;
	
	private boolean isFull;
	private boolean isEmpty;
	
	private String serverID;
	
	private String ipAddress;
	private String macAddress;
	
	
	// TASKS REPRESENTATION
	private Vector<ITask> taskList;
	private int nrOfTasks;
	private int nrOfPredictedTasks;
	
	// Lowest and highest demanding task
	private ITask lowestDemandingTask;
	private ITask highestDemandingTask;
	
	public Server() {
		//serverID = UniqueIdGenerator.getUID();
		serverID = "";
		usedCPU = 0;
		usedRAM = 0;
		usedHDD = 0;
		isFull = false;
		isEmpty = true;
		taskList = new Vector<ITask>();
		nrOfTasks = 0;
		nrOfPredictedTasks = 0;
		lowestDemandingTask = null;
		highestDemandingTask = null;
		cores = 1;
		ipAddress = "192.168.1.1";
	}
	
	public Server(int cpu, int mem, int hdd) {
		this();
		cpuFreq = cpu;
		memoryAmount = mem;
		hddSize = hdd;
	}
	
	public Server(int cpu, int mem, int hdd, int cores, String ip) {
		this();
		cpuFreq = cpu;
		memoryAmount = mem;
		hddSize = hdd;
		this.cores = cores;
		this.ipAddress = ip;
	}
	public Server(int cpu, int mem, int hdd, int cores, String ip, String mac) {
		this(cpu,mem, hdd, cores, ip);
		this.macAddress = mac;
	}
	/***
	 * Return the next highest demanding task in the server task list
	 * which has not beed previously tried to be placed. 
	 */
	@Override
	public ITask GetNextHighestDemandingTask() {
		for(ITask t : taskList){
			if(t.getRequirementsScore() > highestDemandingTask.getRequirementsScore() &&
					t.isUnsuccessfulPlacement() == false){
				highestDemandingTask = t;
			}
		}
		return highestDemandingTask;
	}

	
	/***
	 * Return the next lowest demanding task in the server task list
	 * which has not beed previously tried to be placed. 
	 */
	@Override
	public ITask GetNextLowestDemandingTask() {
		for(ITask t : taskList){
			if(t.getRequirementsScore() < lowestDemandingTask.getRequirementsScore() &&
					t.isUnsuccessfulPlacement() == false && t.isPredicted() == false ){
				lowestDemandingTask = t;
			}
		}
		return lowestDemandingTask;
	}

	@Override
	// TODO: add +- 10%
	public ITask GetTaskWithResources(ArrayList<Integer> resourceDemands) {
		try{
			ITask t = null;
			for(ITask task : taskList){
				int cpu = Integer.parseInt(resourceDemands.get(0).toString());
				int mem = Integer.parseInt(resourceDemands.get(1).toString());
				int hdd = Integer.parseInt(resourceDemands.get(2).toString());
				if( (task.getCpu() >= cpu - cpu * BaseCommon.PERCENT && task.getCpu() <= cpu + cpu * BaseCommon.PERCENT) &&
						(task.getMem() >= mem - mem * BaseCommon.PERCENT && task.getMem() <= mem + mem * BaseCommon.PERCENT) &&
						(task.getHdd() >= hdd - hdd * BaseCommon.PERCENT && task.getHdd() <= hdd + hdd * BaseCommon.PERCENT)
				){
					t = task;
				}
			}
			return t;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public void OrderStandBy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OrderWakeUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addTask(ITask task) {
		try{
			taskList.add(task);
			
			if(lowestDemandingTask==null){
				lowestDemandingTask = task;
			}else{
				if(task.compareTo(lowestDemandingTask)<=0)
					lowestDemandingTask = task;
			}
			if(highestDemandingTask == null){
				highestDemandingTask = task;
			}else{
				if(task.compareTo(highestDemandingTask)>=0){
					highestDemandingTask = task;
				}
			}						
			
			isEmpty = false;
			if(task.isPredicted())
				nrOfPredictedTasks++;
			nrOfTasks++;
			usedCPU += task.getCpu();
			usedRAM += task.getMem();
			usedHDD += task.getHdd();
			
			if( (usedCPU >= cpuFreq - cpuFreq * BaseCommon.PERCENT) || 
				(usedRAM >= memoryAmount - memoryAmount * BaseCommon.PERCENT)||
				(usedHDD >= hddSize - hddSize * BaseCommon.PERCENT))
				isFull = true;
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public int getMaxCpu() {
		return cpuFreq;
	}

	@Override
	public int getMaxHdd() {
		return hddSize;
	}

	@Override
	public int getMaxMem() {
		return memoryAmount;
	}

	@Override
	public int getNumberOfPredictedTasks() {
		return nrOfPredictedTasks;
	}

	@Override
	public int getTotalNumberOfTasks() {
		return nrOfTasks - nrOfPredictedTasks;
	}

	@Override
	public int getUsedCpu() {
		return usedCPU;
	}

	@Override
	public int getUsedHdd() {
		return usedHDD;
	}

	@Override
	public int getUsedMem() {
		return usedRAM;
	}

	@Override
	public boolean isFull() {
		return isFull;
	}

	@Override
	public boolean removeTask(ITask task) {
		try{
			if(taskList.isEmpty() != true){
				if(taskList.contains(task)){
					taskList.remove(task);
					usedCPU -= task.getCpu();
					usedRAM -= task.getMem();
					usedHDD -= task.getHdd();
					if(task.isPredicted())
						nrOfPredictedTasks--;
					nrOfTasks--;
					if(nrOfTasks==0)
						isEmpty = true;
					
					if( (usedCPU >= cpuFreq - cpuFreq * BaseCommon.PERCENT) || 
							(usedRAM >= memoryAmount - memoryAmount * BaseCommon.PERCENT)||
							(usedHDD >= hddSize - hddSize * BaseCommon.PERCENT)){
							isFull = true;
					}else{
						isFull = false;
					}
				}
			}
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public boolean removeTask(UUID taskId) {
		try{
			if(taskList.isEmpty() != true){
				for(ITask t : taskList){
					if(t.getTaskHandle() == taskId){
						taskList.remove(t);
						usedCPU -= t.getCpu();
						usedRAM -= t.getMem();
						usedHDD -= t.getHdd();
						if(t.isPredicted())
							nrOfPredictedTasks--;
						nrOfTasks--;
						if(nrOfTasks==0)
							isEmpty = true;
					}
						
				}
			}
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public String getServerID() {
		return serverID;
	}


	@Override
	public boolean meetsRequirments(ITask task) {
		if((this.getMaxCpu() - this.getUsedCpu()) >= task.getCpu()){
			if( (this.getMaxMem() - this.getUsedMem()) >= task.getMem() ){
				if((this.getMaxHdd() - this.getUsedHdd()) >= task.getHdd()){
					return true;
				}
			}
		}
//		if( ((this.getMaxCpu() - this.getUsedCpu()) - this.getMaxCpu() * BaseCommon.PERCENT) >= task.getCpu()){
//			if( ((this.getMaxMem() - this.getUsedMem())- this.getMaxMem() * BaseCommon.PERCENT) >= task.getMem() ){
//				if( ((this.getMaxHdd() - this.getUsedHdd())- this.getMaxHdd() * BaseCommon.PERCENT) >= task.getHdd()){
//					return true;
//				}
//			}
//		}
		return false;
	}


	@Override
	public int compareTo(IServer server) {
		if(this.getUsedCpu() < server.getUsedCpu()){
			return -1;
		}else{
			if(this.getUsedCpu() > server.getUsedCpu()){
				return 1;
			}else{
				if(this.getUsedMem() < server.getUsedMem()){
					return -1;
				}else{
					if(this.getUsedMem() > server.getUsedMem()){
						return 1;
					}else{
						if(this.getUsedHdd() < server.getUsedHdd()){
							return -1;
						}else{
							if(this.getUsedHdd() > server.getUsedHdd()){
								return 1;
							}else{
								return 0;
							}
						}
					}
				}
			}
		}
		/*if(this.getUsedCpu() < server.getUsedCpu() + server.getUsedCpu() * BaseCommon.PERCENT){
			return -1;
		}else{
			if(this.getUsedCpu() > server.getUsedCpu() - server.getUsedCpu() * BaseCommon.PERCENT){
				return 1;
			}else{
				if(this.getUsedMem() < server.getUsedMem() + server.getUsedMem() * BaseCommon.PERCENT){
					return -1;
				}else{
					if(this.getUsedMem() > server.getUsedMem() - server.getUsedMem() * BaseCommon.PERCENT){
						return 1;
					}else{
						if(this.getUsedHdd() < server.getUsedHdd() + server.getUsedHdd() * BaseCommon.PERCENT){
							return -1;
						}else{
							if(this.getUsedHdd() > server.getUsedHdd() - server.getUsedHdd() * BaseCommon.PERCENT){
								return 1;
							}else{
								return 0;
							}
						}
					}
				}
			}
		}*/
	}


	@Override
	public int getLoad() {
		// get the full power of the Server
		Double fullPower = (cpuFreq*BaseCommon.CPU_WEIGHT) + (memoryAmount*BaseCommon.MEM_WEIGHT) + (hddSize*BaseCommon.HDD_WEIGHT);
		//Double full = Double.valueOf(arg0)(fullPower.toString());
		
		// get the used amount of resources of the Server
		Double usedAmount = (usedCPU*BaseCommon.CPU_WEIGHT) + (usedRAM*BaseCommon.MEM_WEIGHT) + (usedHDD*BaseCommon.HDD_WEIGHT);
		//int used = Integer.getInteger(usedAmount.toString());
		
		
		return (int)((usedAmount*100)/fullPower);
	}


	@Override
	public boolean isEmpty() {
		return isEmpty;
	}


	@Override
	public boolean compareAvailableResources(ITask t) {
		
		if((this.getMaxCpu() - this.getUsedCpu()) >= t.getCpu() + t.getCpu() * BaseCommon.PERCENT 
				//&& (this.getMaxCpu() - this.getUsedCpu()) <= t.getCpu() + t.getCpu() * BaseCommon.PERCENT){
				){
			if( (this.getMaxMem() - this.getUsedMem()) >= t.getMem() + t.getMem() * BaseCommon.PERCENT
					//&& (this.getMaxMem() - this.getUsedMem()) <= t.getMem() + t.getMem() * BaseCommon.PERCENT){
					){
				if((this.getMaxHdd() - this.getUsedHdd()) >= t.getHdd() + t.getHdd() * BaseCommon.PERCENT
						//&& (this.getMaxHdd() - this.getUsedHdd()) <= t.getHdd() + t.getHdd() * BaseCommon.PERCENT){
						){
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public Vector<ITask> getTasks() {
		return taskList;
	}


	@Override
	public ArrayList<Integer> GetAvailableResources() {
		ArrayList<Integer> resources = new ArrayList<Integer>();
		resources.add(this.getUsedCpu());
		resources.add(this.getUsedMem());
		resources.add(this.getUsedHdd());
		return resources;
	}

	public void setCpuFreq(int cpuFreq) {
		this.cpuFreq = cpuFreq;
	}

	public void setMemoryAmount(int memoryAmount) {
		this.memoryAmount = memoryAmount;
	}

	public void setHddSize(int hddSize) {
		this.hddSize = hddSize;
	}

	public void setUsedCPU(int usedCPU) {
		this.usedCPU = usedCPU;
	}

	public void setUsedRAM(int usedRAM) {
		this.usedRAM = usedRAM;
	}

	public void setUsedHDD(int usedHDD) {
		this.usedHDD = usedHDD;
	}

	public void setServerID(String serverID) {
		this.serverID = serverID;
	}

	@Override
	public String getIPAddress() {
		return ipAddress;
	}
	
	@Override
	public void setIPAddress(String ip) {
		this.ipAddress = ip;
	}

	@Override
	public int getNumberOfCores() {
		return cores;
	}
	
	public void setCoreFreq(int am){
		coreFreq = am;
	}

	public int getCoreFreq() {
		return coreFreq;
	}

	public String getMacAddress() {
		return macAddress;
	}

	@Override
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}


	
	

}
