package vdrm.disp.main;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import vdrm.base.impl.Task;

@XmlRootElement(name = "jaxbTasks")

public class JaxbTasks {
	@XmlElementWrapper(name = "tasksList")

	@XmlElement(name = "task")
	private ArrayList<Task> taskList;

	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public void setTasksList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}
}
