package vdrm.disp.main;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Configurations")

@XmlType(propOrder = { "serversPath", "tasksPath", "historyPath", "workloadPath" })
public class JaxbConfig {
	
	private String serversPath;
	private String tasksPath;
	private String historyPath;
	private String workloadPath;
	
	public String getServersPath() {
		return serversPath;
	}
	public void setServersPath(String serversPath) {
		this.serversPath = serversPath;
	}
	public String getTasksPath() {
		return tasksPath;
	}
	public void setTasksPath(String tasksPath) {
		this.tasksPath = tasksPath;
	}
	public String getHistoryPath() {
		return historyPath;
	}
	public void setHistoryPath(String historyPath) {
		this.historyPath = historyPath;
	}
	public String getWorkloadPath() {
		return workloadPath;
	}
	public void setWorkloadPath(String workloadPath) {
		this.workloadPath = workloadPath;
	}
}
