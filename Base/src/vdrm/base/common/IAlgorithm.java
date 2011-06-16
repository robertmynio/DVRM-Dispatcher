package vdrm.base.common;

import java.util.ArrayList;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public interface IAlgorithm {
	public void initialize(ArrayList<IServer> servers);
	public void newTask(ITask task);

	
	public void endTask(ITask task);
	public boolean redistributeTasks(IServer server);
	public void initialize(ArrayList<IServer> servers, ArrayList<ITask> tasks,
			ArrayList<ITask> taskHistory);
	
	public Object getVirtualizationService();
}
