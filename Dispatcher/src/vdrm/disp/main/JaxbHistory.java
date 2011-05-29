package vdrm.disp.main;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "history")

public class JaxbHistory {
	@XmlElementWrapper(name = "taskHistory")

	@XmlElement(name = "task")
	private ArrayList<Integer> taskHistory;

	public ArrayList<Integer> getTaskHistory() {
		return taskHistory;
	}

	public void setTasksHistory(ArrayList<Integer> taskHistory) {
		this.taskHistory = taskHistory;
	}
}
