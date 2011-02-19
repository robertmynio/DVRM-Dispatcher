package vdrm.base.common;

import java.util.ArrayList;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public interface IAlgorithm {
	public void initialize(ArrayList<IServer> servers);
	public void newTask(ITask task);
	public void redistributeTasks(IServer server, ITask finishedTask);
}
