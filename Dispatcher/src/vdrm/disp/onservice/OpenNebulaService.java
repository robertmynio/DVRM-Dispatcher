package vdrm.disp.onservice;

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
import vdrm.base.util.VDRMLogger;

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
	public boolean DeployTask(ITask t, boolean isMigration, IServer newServ){
		if(BaseCommon.ONEnabled){
			//if(((Task)t).isDeployedOrdered()==false){
			//TODO: make sure hdd path is integrated into ITask (maybe put linux/windows image paths")

			try{
				// Step2: try to allocate the VM 

					//TODO: add VirtualMachineID to a task also! -- not necessary. can be obtained from vm.
					
					// Step3: create the VM
					//VirtualMachine vm = new VirtualMachine(vmID, openClient);
					//if(vm != null){
						//assign the vm to the task also
					//	t.SerVirtualMachine(vm);
						
						ExecutorService threadService = Executors.newSingleThreadExecutor();
						VMDeployer vmd = new VMDeployer(t, openClient, 0, isMigration,newServ);
						threadService.execute(vmd);
						threadService.shutdown();
						return true;
					//}else{
					//	logger.logWarning("ON: Could not create Virtual Machine for task "+t.getTaskHandle().toString());
					//}
				
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
			if(newServ == null){
				System.out.println("Shit, the server is null before deploying.");
			}else{
				ExecutorService threadService = Executors.newSingleThreadExecutor();
				VMDeployer vmd = new VMDeployer(t,isMigration,newServ);
				threadService.execute(vmd);
				threadService.shutdown();
			}
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
	public boolean MigrateTask(ITask t, IServer s) {
		if(BaseCommon.ONEnabled){
			Task task = (Task)t;
			

			DeployTask(task, true, s);
			
			
//			BaseCommon.Instance().TaskEndedMigrating.setChanged();
//			BaseCommon.Instance().TaskEndedMigrating.notifyObservers(task);
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
			// for TESTING purposes only
//			ExecutorService threadService = Executors.newSingleThreadExecutor();
//			VMMigrator vmd = new VMMigrator(t,s);
//			threadService.execute(vmd);
//			threadService.shutdown();
			
			DeployTask((Task)t, true, s);
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
//				VirtualMachine vm = task.GetVirtualMachine();
//				if(vm != null){
//					//The Only Step: migrate to server ID
//					OneResponse status = vm.migrate(Integer.parseInt(destinationServer.getServerID().toString()), false);
//					status = vm.info();
//					int timer = 5000;
//					while (vm.lcmState() != 3 && vm.lcmState() != 0 && vm.lcmState() != 14) {
//			            //  System.out.println(machine.lcmState()+machine.lcmStateStr());
//			            try {
//			                Thread.sleep(timer);
//			            } catch (InterruptedException e) {
//			                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//			            }
//			            if(timer > 300)
//							timer -= 200;
//			            vm.info();
//			        }
//					BaseCommon.Instance().TaskEndedMigrating.setChanged();
//					BaseCommon.Instance().TaskEndedMigrating.notifyObservers(task);
//				}else{
//					
//				}
			}
			else{
				try {
					task.setServer(destinationServer);
					Thread.sleep(40000);
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
		private boolean isMigrate;
		private IServer newServer;
		
		public VMDeployer(ITask t, Client onClient, int vmID){
			this.task = t;
			this.vmID = vmID;
			this.on_Client = onClient;
		}
		
		public VMDeployer(ITask t, Client onClient, int vmID, boolean isM, IServer newServer){
			this.task = t;
			this.vmID = vmID;
			this.on_Client = onClient;
			this.isMigrate = isM;
			this.newServer = newServer;
		}
		
		public VMDeployer(ITask t){
			this.task = t;
		}
		
		public VMDeployer(ITask t, boolean isM, IServer newServer){
			this.task = t;
			this.isMigrate = isM;
			this.newServer = newServer;
		}
		
		@Override
		public void run() {
			boolean dontMigrate = false;
			if(BaseCommon.Instance().ONEnabled){
				if(isMigrate){
					// fire the event
					if(task.getServerId() != null && newServer != null){
//						BaseCommon.Instance().TaskStartedMigrating.setChanged();
//						BaseCommon.Instance().TaskStartedMigrating.notifyObservers(task);
						
						
						((Task)(task)).setDeployed(false);
						FinishTask(task);
						task.setServer(newServer);
					}else{
						dontMigrate = true;
					}
				}else{
					if(task.getServer() == null)
						task.setServer(newServer);
				}
				OneResponse rc;
				
				// if the server is null, do not migrate
				if(!isMigrate || (isMigrate && (!dontMigrate))){
					int nrCores = task.getServer().getNumberOfCores();
					int serverCPUFreq = ((Server)task.getServer()).getCoreFreq();
					int tFreq = task.getCpu();
					double taskFreq = (((double)tFreq/(double)serverCPUFreq));
					String vmTemplate = ONConfigurationService.GetConfiguration(taskFreq, task.getMem(), task.getHdd(), "hddPath");
					rc = VirtualMachine.allocate(openClient, vmTemplate);
					if(rc.isError()){
						logger.logWarning("ONDeployTask: "+rc.getErrorMessage());
					}else{
						
						vmID = Integer.parseInt(rc.getMessage());
						
					
				
					// Step3: create the VM
					VirtualMachine vm = new VirtualMachine(vmID, openClient);
					if(vm != null){
						//assign the vm to the task also
						task.SerVirtualMachine(vm);
						
						// Step4: deploy the VM to the desired server
						rc = vm.deploy( Integer.parseInt( ((Task)task).getServerId().toString() ) );
						
						// Step5: wait until the VM actually starts
						rc = vm.info();
						
							if(isMigrate){
								System.out.println("$$ Starting migration deploy at " + System.currentTimeMillis());
							}
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
							if(isMigrate){
								System.out.println("## ENDING migration deploy at " + System.currentTimeMillis());
							}
						}
					}
				}
			}else{
				try {
					if(isMigrate){
						if(task.getServerId() != null && newServer != null){
							// fire the event
							//BaseCommon.Instance().TaskStartedMigrating.setChanged();
							//BaseCommon.Instance().TaskStartedMigrating.notifyObservers(task);
							((Task)(task)).setDeployed(false);
							task.setServer(newServer);
						}
					}
					Thread.sleep(40000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!isMigrate){
				// Step6: notify that the VM started
				if(task.getServer() != null){
					((Task)(task)).setDeployed(true);
					BaseCommon.Instance().getVMStarted().setChanged();
					BaseCommon.Instance().getVMStarted().notifyObservers(task);
				}else{
					System.out.println("Shit, the server is null after deploying.");		
				}
			}else{
				if(task.getServerId() != null && newServer != null){
					((Task)(task)).setDeployed(true);
					System.out.println("$$ Sending migration finished event at " + System.currentTimeMillis());
					
					BaseCommon.Instance().TaskEndedMigrating.setChanged();
					BaseCommon.Instance().TaskEndedMigrating.notifyObservers(task);
				}
			}
		}
		
	}
}
