package vdrm.onservice;

import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.*;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Task;
import vdrm.disp.util.VDRMLogger;

public class OpenNebulaService implements IOpenNebulaService {
	private Client openClient;
	private VDRMLogger logger;
	public OpenNebulaService(){
		logger = new VDRMLogger();
	}

	/***
	 * For a new task: customize and create a new VM configuration,
	 * allocate the VM and then
	 * deploy the VM to the desired host
	 */
	@Override
	public boolean DeployTask(ITask t) {
		//TODO: make sure hdd path is integrated into ITask (maybe put linux/windows image paths")
		// Step1: create the VM template
		String vmTemplate = ONConfigurationService.GetConfiguration(t.getCpu(), t.getMem(), t.getHdd(), "hddPath");
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
					
					// Step4: deploy the VM to the desired server
					vm.deploy( Integer.parseInt( ((Task)t).getServerId().toString() ) );
					
					return true;
				}else{
					logger.logWarning("ON: Could not create Virtual Machine for task "+t.getTaskHandle().toString());
				}
			}
		}catch(Exception ex){
			logger.logWarning("Cannot deploy new task."+ex.getMessage());
		}
		return false;
	}

	/***
	 * Finish Task
	 * 
	 * First, stop the VM, then delete it (finalize it) from open nebula
	 */
	@Override
	public boolean FinishTask(ITask t) {
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

	/***
	 * Create the OpenNebula Client.
	 */
	@Override
	public void Initialize() {
		try {
			openClient = new Client();
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
		Task task = (Task)t;
		VirtualMachine vm = task.GetVirtualMachine();
		if(vm != null){
			//The Only Step: migrate to server ID
			vm.migrate(Integer.parseInt(s.getServerID().toString()), true);
			return true;
		}
		return false;
	}

}