package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.disp.alg.Algorithm1;

public class Tests {
	
	private Algorithm1 alg;
	
	public Tests() {
		alg = new Algorithm1();
	}
	
	public boolean hardcodedInitTest() {
		Server s;
		ArrayList<IServer> servers = new ArrayList<IServer>();
		
		//simple initialization -> HARDCODED!!!
		s = new Server(11000,6000,1000000);
		servers.add(s);
		s = new Server(11000,6000,1000000);
		servers.add(s);
		s = new Server(11000,6000,1000000);
		servers.add(s);
		s = new Server(12000,6000,1000000);
		servers.add(s);
		s = new Server(12000,1800,1000000);
		servers.add(s);
		
		alg.initialize(servers);
		servers = alg.getEmptyServers();
		if(servers.size()==5)
			return true;
		return false;
	}
	
	public boolean addOneTask() {
		Task task;
		task = new Task(600,1000,100);
		alg.newTask(task);
		
		ArrayList<IServer> servers = alg.getInUseServers();
		if(servers.size()==1) return true;
		return false;
	}
}
