package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Sorter;
import vdrm.base.impl.Task;
import vdrm.disp.alg.Algorithm1;
import vdrm.rootservice.RootService;

public class TestsVlad {
	private Algorithm1 alg;
	private RootService rs;
	
	public TestsVlad() {
		alg = new Algorithm1();
		rs = RootService.Instance();
	}
	
	public void runAllTests() {
		boolean result;
		
		//inits the datastructures of the algorithm with five servers
		result = hardcodedInitTest();
		//System.out.println("Result for hardcoded init test is : " + result);
		
		if(result){
		// task initialization
		Task task;
		
		// these 3 should go on server 1
		//task = new Task(4600,1000,400000);
		//alg.newTask(task);
		//rs.TaskArrived(task, 10000);
		
//		task = new Task(600,700,10000);
//		//alg.newTask(task);
//		rs.TaskArrived(task, 9000);
//		
//		task = new Task(600,800,10000);
//		//alg.newTask(task);
//		rs.TaskArrived(task, 8500);
//		
//		// server 2
//		task = new Task(7800,4000,400000);
////		task = new Task(4000,2000,300000);
//		//alg.newTask(task);
//		rs.TaskArrived(task, 9000);
//		
//		// server 3
//		task = new Task(7000,3500,200);
////		//alg.newTask(task);
//		rs.TaskArrived(task, 8000);
//		// this should go on server 3 
//		task = new Task(1000,2600,200000);
//		//alg.newTask(task);
		
		// SERVER 1
		task = new Task(2000,1500,200000);
		//alg.newTask(task);
		rs.TaskArrived(task, 7000);
		task = new Task(2000,1500,200000);
		//alg.newTask(task);
		rs.TaskArrived(task, 9000);
		
		task = new Task(600,600,10000);
		//alg.newTask(task);
		rs.TaskArrived(task, 9000);
		task = new Task(600,600,10000);
		//alg.newTask(task);
		rs.TaskArrived(task, 9000);
		task = new Task(600,600,10000);
		//alg.newTask(task);
		rs.TaskArrived(task, 9000);
		
		// SERVER 2
		task = new Task(2000,1500,15000);
		//alg.newTask(task);
		rs.TaskArrived(task, 12000);
		// this should go on server 4 and mark it full
		//task = new Task(11000,5000,1000000);
		//alg.newTask(task);
		
		// this should go on server 2, as server 1 doesn't meet the required resources and 3 is full
		//task = new Task(4000,3000,300000);
		//alg.newTask(task);

		
//		ArrayList<IServer> servers = alg.getInUseServers();
//		System.out.println("Number of in use servers is : " + servers.size());
//		ArrayList<IServer> servers2 = alg.getFullServers();
//		System.out.println("Number of full servers is : " + servers2.size());
//		if(servers.size()>0 || servers2.size()>0){
//			// good
//		}
		//THIS IS AN EXAMPLE
		//result = exampleTest();
//		System.out.println("Result for example test is : " + result);
		//System.out.println("DONE ");
		}
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
		

//		s = new Server(12000,6000,1000000);
//		servers.add(s);
//		s = new Server(12000,1800,1000000);
//		servers.add(s);
		
		
		//alg.initialize(servers);
		RootService.Instance().worker.initialize(servers);
		//servers = alg.getEmptyServers();
		servers = ((Algorithm1)RootService.Instance().worker).getEmptyServers();
		if(servers.size()==3)
			return true;

		return false;
	} 
	
	//THIS IS AN EXAMPLE
	private boolean exampleTest() {
		Task t;
		
		ArrayList<ITask> tasks = alg.getInUseServers().get(0).getTasks();
		t=(Task)tasks.get(1);
		alg.endTask(t);
		return true;
	}
}
