package vdrm.base.data;

import java.util.UUID;

public interface ITask {
	public int getMem();
	public int getHdd();
	public int getCpu();
	public boolean isPredicted();
	public UUID serverId();
}
