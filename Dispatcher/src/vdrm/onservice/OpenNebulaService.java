package vdrm.onservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.*;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.base.impl.BaseCommon.VMStartedEvent;
import vdrm.disp.util.VDRMLogger;

public class OpenNebulaService implements IOpenNebulaService {
	private Client openClient;
	private VDRMLogger logger;
	private final String NEBULA_RCP_ADDRESS = "http://192.168.1.10:2633/RPC2";
    private final String NEBULA_CREDENTIALS = "oneadmin:oneadmin";
	public OpenNebulaService(){
		logger = new VDRMLogger();
		Initialize();
	}

	/***
	 * For a new task: customize and create a new VM configuration,
	 * allocate the VM and then
	 * deploy the VM to the desired host
	 */
	@Override
	public boolean DeployTask(ITask t) {
		if(BaseCommon.ONEnabled){
			//if(((Task)t).isDeployedOrdered()==false){
			//TODO: make sure hdd path is integrated into ITask (maybe put linux/windows image paths")
			// Step1: create the VM template
			int nrCores = t.getServer().getNumberOfCores();
			int serverCPUFreq = ((Server)t.getServer()).getCoreFreq();
			int tFreq = t.getCpu();
			int taskFreq = (int)(((double)tFreq/(double)serverCPUFreq) *100);
//			String vmTemplate = ONConfigurationService.GetConfiguration(t.getCpu(), t.getMem(), t.getHdd(), "hddPath");
			String vmTemplate = ONConfigurationService.GetConfiguration(taskFreq, t.getMem(), t.getHdd(), "hddPath");
			OneResponse rc;
			int vmID;
			try{
				// Step2: try to allocate the VM 
				rc = VirtualMachine.allocate(openClient, vmTemplate);
				if(rc.isError()){
					logger.logWarning("ONDeployTask: "+rc.getErrorMessage());
				}else{
					vmID = Integer.parseInt(rc.getMessage());
					//TODO: add VirtualMachineID to a task also! -- not necessary. can be obtained from vm.
					
					// Step3: create the VM
					VirtualMachine vm = new VirtualMachine(vmID, openClient);
					if(vm != null){
						//assign the vm to the task also
						t.SerVirtualMachine(vm);
						
						ExecutorService threadService = Executors.newSingleThreadExecutor();
						VMDeployer vmd = new VMDeployer(t, openClient, vmID);
						threadService.execute(vmd);
						threadService.shutdown();
//						// Step4: deploy the VM to the desired server
//						rc = vm.deploy( Integer.parseInt( ((Task)t).getServerId().toString() ) );
//						
//						// Step5: wait until the VM actually starts
//						rc = vm.info();
//						int timer = 5000;
//						while(vm.lcmState() != 3 &&
//								vm.lcmState() != 0 && vm.lcmState() != 14){
//							Thread.sleep(timer);
//							rc = vm.info();
//							
//							if(timer > 300)
//								timer -= 200;
//						}
						
//						// Step6: notify that the VM started
//						BaseCommon.Instance().getVMStarted().setChanged();
//						BaseCommon.Instance().getVMStarted().notifyObservers(t);
						
						return true;
					}else{
						logger.logWarning("ON: Could not create Virtual Machine for task "+t.getTaskHandle().toString());
					}
				}
			}catch(Exception ex){
				logger.logWarning("Cannot deploy new task."+ex.getMessage());
			}
			return false;
		}else{
			// for testing purposes only
			int nrCores = t.getServer().getNumberOfCores();
			int serverCPUFreq = ((Server)t.getServer()).getCoreFreq();
			int tFreq = t.getCpu();
			int taskFreq = (int)(((double)tFreq/(double)serverCPUFreq) *100);
			
			ExecutorService threadService = Executors.newSingleThreadExecutor();
			VMDeployer vmd = new VMDeployer(t);
			threadService.execute(vmd);
			threadService.shutdown();
			return true;
		}
	}

	/***
	 * Finish Task
	 * 
	 * First, stop the VM, then delete it (finalize it) from open nebula
	 */
	@Override
	public boolean FinishTask(ITask t) {
		if(BaseCommon.ONEnabled){
			Task task = (Task)t;
			VirtualMachine vm = task.GetVirtualMachine();
			if(vm!=null){
				// Step1: stop the VM
				vm.stop();
				
				// release the VM from the task
				task.SerVirtualMachine(null);
				
				// Step2: finalize the VM
				vm.finalizeVM();
				
				return true;
			}
			return false;
		}
		else{
			// for testing purposes only
			return true;
		}
	}

	/***
	 * Create the OpenNebula Client.
	 */
	@Override
	public void Initialize() {
		try {
			if(BaseCommon.ONEnabled)
				openClient = new Client(NEBULA_CREDENTIALS, NEBULA_RCP_ADDRESS);
		} catch (Exception e) {
			logger.logWarning("Cannot create OpenNebula client. "+e.getMessage());
		}
	}

	/***
	 * Migrate the task to the new server
	 * ToDo later: check out if we can track status and return true only when task  
	 * successfully migrated (check with vm.status())
	 */
	@Override
	public boolean MigrateTask(ITask t, IServer s) {
		if(BaseCommon.ONEnabled){
			Task task = (Task)t;
//			ExecutorService threadService = Executors.newSingleThreadExecutor();
//			VMMigrator vmd = new VMMigrator(t,s);
//			threadService.execute(vmd);
//			threadService.shutdown();
			
			return true;
//			VirtualMachine vm = task.GetVirtualMachine();
//			if(vm != null){
//				//The Only Step: migrate to server ID
//				OneResponse status = vm.migrate(Integer.parseInt(s.getServerID().toString()), false);
//				if(!status.isError()){
//					//BaseCommon.Instance().TaskEndedMigrating.setChanged();
//					//BaseCommon.Instance().TaskEndedMigrating.notifyObservers(t);
//					return true;
//				}
//				else{
//					//BaseCommon.Instance().TaskEndedMigrating.setChanged();
//					//BaseCommon.Instance().TaskEndedMigrating.notifyObservers(t);
//					return false;
//				}
//			}
//			return false;
		}
		else{
			// for testing purposes only
			ExecutorService threadService = Executors.newSingleThreadExecutor();
			VMMigrator vmd = new VMMigrator(t,s);
			threadService.execute(vmd);
			threadService.shutdown();
			return true;
		}
	}

	public class VMMigrator implements Runnable{
		private ITask task;
		private IServer destinationServer;
		
		public VMMigrator(ITask t, IServer s){
			this.task = t;
			this.destinationServer = s;
		}
		
		@Override
		public void run() {
			
			// fire the event
			BaseCommon.Instance().TaskStartedMigrating.setChanged();
			BaseCommon.Instance().TaskStartedMigrating.notifyObservers(task);
			
			if(BaseCommon.ONEnabled){
				VirtualMachine vm = task.GetVirtualMachine();
				if(vm != null){
					//The Only Step: migrate to server ID
					OneResponse status = vm.migrate(Integer.parseInt(destinationServer.getServerID().toString()), false);
					status = vm.info();
					int timer = 5000;
					while (vm.lcmState() != 3 && vm.lcmState() != 0 && vm.lcmState() != 14) {
			            //  System.out.println(machine.lcmState()+machine.lcmStateStr());
			            try {
			                Thread.sleep(timer);
			            } catch (InterruptedException e) {
			                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			            }
			            if(timer > 300)
							timer -= 200;
			            vm.info();
			        }
					BaseCommon.Instance().TaskEndedMigrating.setChanged();
					BaseCommon.Instance().TaskEndedMigrating.notifyObservers(task);
				}else{
					
				}
			}
			else{
				try {
					Thread.sleep(10000);
					BaseCommon.Instance().TaskEndedMigrating.setChanged();
					BaseCommon.Instance().TaskEndedMigrating.notifyObservers(task);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public class VMDeployer implements Runnable{
		private ITask task;
		private int vmID;
		private Client on_Client;
		
		public VMDeployer(ITask t, Client onClient, int vmID){
			this.task = t;
			this.vmID = vmID;
			this.on_Client = onClient;
		}
		
		public VMDeployer(ITask t){
			this.task = t;
		}
		
		@Override
		public void run() {
			if(BaseCommon.Instance().ONEnabled){
				OneResponse rc;
				// Step3: create the VM
				VirtualMachine vm = new VirtualMachine(vmID, openClient);
				if(vm != null){
					//assign the vm to the task also
					task.SerVirtualMachine(vm);
					
					// Step4: deploy the VM to the desired server
					rc = vm.deploy( Integer.parseInt( ((Task)task).getServerId().toString() ) );
					
					// Step5: wait until the VM actually starts
					rc = vm.info();
					
					
						int timer = 5000;
						while(vm.lcmState() != 3 &&
								vm.lcmState() != 0 && vm.lcmState() != 14){
							try {
								Thread.sleep(timer);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							rc = vm.info();
							
							if(timer > 300)
								timer -= 200;
						}
				}
			}else{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Step6: notify that the VM started
			BaseCommon.Instance().getVMStarted().setChanged();
			BaseCommon.Instance().getVMStarted().notifyObservers(task);
		}
		
	}
}
