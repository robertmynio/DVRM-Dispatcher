package vdrm.base.impl;

import java.util.ArrayList;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.opennebula.client.vm.VirtualMachine;

import vdrm.base.data.*;

@XmlRootElement(name = "server")

@XmlAccessorType( XmlAccessType.NONE )

@XmlType(propOrder = { "cpuReq", "memReq", "hddReq"})
public class Task implements ITask{
	
	private UUID taskHandle;
	@XmlElement( required = true )
	private int cpuReq;
	@XmlElement( required = true )
	private int memReq;
	@XmlElement( required = true )
	private int hddReq;
	private double requirements;
	
	private boolean unsuccessfulPlacement;
	private boolean isPredicted;
	private boolean deployedOrdered;
	private boolean canMigrate;
	private boolean isDeployed;
	
	private IServer serverHost;
	private VirtualMachine virtualMachine;
	
	
	public Task(){
		taskHandle = UUID.randomUUID();
		unsuccessfulPlacement = false;
		isDeployed = false;
		isPredicted = false;
		serverHost = null;
		deployedOrdered = false;
		canMigrate = true;
	}
	
	public Task(int cpu, int mem, int hdd){
		this();
		this.cpuReq = cpu;
		this.memReq = mem;
		this.hddReq = hdd;
		computeRequirementsScore();
	}
	
	public Task(int cpu, int mem, int hdd, IServer s){
		this(cpu,mem,hdd);
		serverHost = s;
	}
	
	@Override
	public ArrayList<Integer> GetRequirements() {
		ArrayList<Integer> req = new ArrayList<Integer>();
		req.add(cpuReq);
		req.add(memReq);
		req.add(hddReq);
		return req;
	}
	
	@Override
	public void computeRequirementsScore(){
		requirements = (cpuReq*BaseCommon.CPU_WEIGHT) + (memReq*BaseCommon.MEM_WEIGHT) + (hddReq*BaseCommon.HDD_WEIGHT);
	}
	
	@Override
	public boolean equals(ITask task) {
//		if(this.getCpu() == task.getCpu()){
//			if(this.getMem() == task.getMem()){
//				if(this.getHdd() == task.getHdd()){
//					return true;
//				}
//			}
//		}
		if(this.getCpu() >= task.getCpu() - task.getCpu()* BaseCommon.PERCENT
				&& this.getCpu() <= task.getCpu() + task.getCpu() * BaseCommon.PERCENT){
			if(this.getMem() >= task.getMem() - task.getMem() * BaseCommon.PERCENT
					&& this.getMem() <= task.getMem() + task.getMem() * BaseCommon.PERCENT){
				if(this.getHdd() >= task.getHdd() - task.getHdd() * BaseCommon.PERCENT
						&& this.getHdd() <= task.getHdd() + task.getHdd() * BaseCommon.PERCENT){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(ITask task) {
		/*if(this.getCpu() < task.getCpu()){
			return -1;
		}else{
			if(this.getCpu() > task.getCpu()){
				return 1;
			}else{
				if(this.getMem() < task.getMem()){
					return -1;
				}else{
					if(this.getMem() > task.getMem()){
						return 1;
					}else{
						if(this.getHdd() < task.getHdd()){
							return -1;
						}else{
							if(this.getHdd() > task.getHdd()){
								return 1;
							}else{
								return 0;
							}
						}
					}
				}
			}
		}*/
		if(this.getCpu() < task.getCpu() + task.getCpu() * BaseCommon.PERCENT){
			return -1;
		}else{
			if(this.getCpu() > task.getCpu() - task.getCpu() * BaseCommon.PERCENT){
				return 1;
			}else{
				if(this.getMem() < task.getMem() + task.getMem() * BaseCommon.PERCENT){
					return -1;
				}else{
					if(this.getMem() > task.getMem() - task.getMem() * BaseCommon.PERCENT){
						return 1;
					}else{
						if(this.getHdd() < task.getHdd() + task.getHdd() * BaseCommon.PERCENT){
							return -1;
						}else{
							if(this.getHdd() > task.getHdd() - task.getHdd() * BaseCommon.PERCENT){
								return 1;
							}else{
								return 0;
							}
						}
					}
				}
			}
		}
	}


//************************************************************************
//************************ SETTERS AND GETTERS ***************************
//************************************************************************	
	@Override
	public UUID getTaskHandle() {
		return taskHandle;
	}

	@Override
	public void setTaskHandle(UUID taskHandle) {
		this.taskHandle = taskHandle;
	}

	@Override
	public double getResourceScore() {
		return requirements;
	}

	@Override
	public boolean isUnsuccessfulPlacement() {
		return unsuccessfulPlacement;
	}

	@Override
	public void setUnsuccessfulPlacement(boolean unsuccessfulPlacement) {
		this.unsuccessfulPlacement = unsuccessfulPlacement;
	}
	
	@Override
	public int getCpu() {
		return cpuReq;
	}

	@Override
	public int getHdd() {
		return hddReq;
	}

	@Override
	public int getMem() {
		return memReq;
	}

	@Override
	public IServer getServer() {
		return serverHost;
	}

	@Override
	public String getServerId() {
		if(serverHost!=null)
			return serverHost.getServerID();
		else
			return null;
	}

	@Override
	public boolean isPredicted() {
		return isPredicted;
	}

	@Override
	public boolean setPredicted(boolean predicted) {
		isPredicted = predicted;
		return true;
	}

	@Override
	public double getRequirementsScore() {
		return requirements;
	}

	@Override
	public void setServer(IServer server) {
		this.serverHost = server;
	}

	@Override
	public VirtualMachine GetVirtualMachine() {
		return this.virtualMachine;
	}

	@Override
	public void SerVirtualMachine(VirtualMachine vm) {
		this.virtualMachine = vm;
	}

	public boolean isDeployedOrdered() {
		return deployedOrdered;
	}

	public void setDeployedOrdered(boolean deployedOrdered) {
		this.deployedOrdered = deployedOrdered;
	}

	public boolean getCanMigrate() {
		return canMigrate;
	}

	public void setCanMigrate(boolean canMigrate) {
		this.canMigrate = canMigrate;
	}

	public int getCpuReq() {
		return cpuReq;
	}

	public void setCpuReq(int cpuReq) {
		this.cpuReq = cpuReq;
	}

	public int getMemReq() {
		return memReq;
	}

	public void setMemReq(int memReq) {
		this.memReq = memReq;
	}

	public int getHddReq() {
		return hddReq;
	}

	public void setHddReq(int hddReq) {
		this.hddReq = hddReq;
	}

	public boolean isDeployed() {
		return isDeployed;
	}

	public void setDeployed(boolean isDeployed) {
		this.isDeployed = isDeployed;
	}

	

}
