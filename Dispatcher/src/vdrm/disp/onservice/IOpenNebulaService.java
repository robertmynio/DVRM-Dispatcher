package vdrm.disp.onservice;

import org.opennebula.client.vm.VirtualMachine;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public interface IOpenNebulaService {
	
	public void Initialize();
	public boolean DeployTask(ITask t, boolean isMigrate, IServer newServer);
	public boolean FinishTask(ITask t);
	public boolean MigrateTask(ITask t, IServer s);
}
