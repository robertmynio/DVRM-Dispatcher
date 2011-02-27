package vdrm.base.data;

import java.util.UUID;

public interface ITask {
	public int getMem();
	public int getHdd();
	public int getCpu();
	public boolean isPredicted();
	public boolean setPredicted(boolean predicted);
	public UUID getServerId();
	public IServer getServer();
	public boolean equals(ITask task);
}
