package vdrm.onservice;

import org.opennebula.client.vm.VirtualMachine;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public interface IOpenNebulaService {
	
	public void Initialize();
	public boolean DeployTask(ITask t);
	public boolean FinishTask(ITask t);
	
	public boolean MigrateTask(ITask t, IServer s);
}
