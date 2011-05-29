package vdrm.disp.main;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import vdrm.base.impl.Server;

@XmlRootElement(name = "jaxbServers")


public class JaxbServers {
	@XmlElementWrapper(name = "serverList")

	@XmlElement(name = "server")
	private ArrayList<Server> serverList;

	public ArrayList<Server> getServerList() {
		return serverList;
	}

	public void setServersList(ArrayList<Server> serverList) {
		this.serverList = serverList;
	}
}
