package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.disp.alg.Algorithm1;

public class TestsRobi {
	
	private Algorithm1 alg;
	
	public TestsRobi() {
		alg = new Algorithm1();
	}
	
	public void runAllTests() {
		boolean result;
		
		result = hardcodedInitTest();
		System.out.println("Result for hardcoded init test is : " + result);
		
		//this test should be called after an initialization (for example, after hardcoded init test)
		//result = addOneTask();
		//System.out.println("Result for add one task test is : " + result);
		
		result = addFiveTasks();
		System.out.println("Result for add five tasks test is : " + result);
	}
	
	private boolean hardcodedInitTest() {
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
	
	private boolean addOneTask() {
		Task task;
		task = new Task(600,1000,100);
		alg.newTask(task);
		
		ArrayList<IServer> servers = alg.getInUseServers();
		if(servers.size()==1) return true;
		return false;
	}
	
	private boolean addFiveTasks() {
		Task task;
		
		task = new Task(2600,1000,100);
		alg.newTask(task);
		task = new Task(600,4000,100);
		alg.newTask(task);
		task = new Task(4000,2000,100);
		alg.newTask(task);
		task = new Task(1000,500,100);
		alg.newTask(task);
		task = new Task(6000,1600,100);
		alg.newTask(task);
		
		ArrayList<IServer> servers = alg.getInUseServers();
		System.out.println("Number of in use servers is : " + servers.size());
		ArrayList<IServer> servers2 = alg.getFullServers();
		System.out.println("Number of full servers is : " + servers2.size());
		if(servers.size()>0 || servers2.size()>0) return true;
		return false;
	}
}
