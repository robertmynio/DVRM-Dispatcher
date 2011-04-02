package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.disp.alg.Algorithm1;

public class TestsVlad {
	private Algorithm1 alg;
	
	public TestsVlad() {
		alg = new Algorithm1();
	}
	
	public void runAllTests() {
		boolean result;
		
		//inits the datastructures of the algorithm with five servers
		result = hardcodedInitTest();
		System.out.println("Result for hardcoded init test is : " + result);
		
		//ar trebui sa adaugi niste task-uri inainte sa faci remove :P
		//pentru adaugare, vezi testele mele
		// P.S. : ar trebui sa faci remove si fara sa adaugi vreun task numa ca sa vezi daca da fail sau nu:)
		// P.S.2. : incearca sa faci cat mai multe teste care ar sparge algoritmu! Spor :)
		
		if(result){
		// task initialization
		Task task;
		
		task = new Task(2600,1000,100);
		alg.newTask(task);
		task = new Task(600,4000,100);
		alg.newTask(task);
		task = new Task(4000,2000,100);
		alg.newTask(task);
//		task = new Task(1000,500,100);
//		alg.newTask(task);
//		task = new Task(6000,1600,100);
//		alg.newTask(task);
		
		ArrayList<IServer> servers = alg.getInUseServers();
		System.out.println("Number of in use servers is : " + servers.size());
		ArrayList<IServer> servers2 = alg.getFullServers();
		System.out.println("Number of full servers is : " + servers2.size());
		if(servers.size()>0 || servers2.size()>0){
			// good
		}
		//THIS IS AN EXAMPLE
		result = exampleTest();
		System.out.println("Result for example test is : " + result);
		}
	}
	
	private boolean hardcodedInitTest() {
		Server s;
		ArrayList<IServer> servers = new ArrayList<IServer>();
		
		//simple initialization -> HARDCODED!!!
		s = new Server(11000,6000,1000000);
		servers.add(s);
//		s = new Server(11000,6000,1000000);
//		servers.add(s);
//		s = new Server(11000,6000,1000000);
//		servers.add(s);
//		s = new Server(12000,6000,1000000);
//		servers.add(s);
//		s = new Server(12000,1800,1000000);
//		servers.add(s);
		
		
		alg.initialize(servers);
		servers = alg.getEmptyServers();
		if(servers.size()==1)
			return true;

		return false;
	} 
	
	//THIS IS AN EXAMPLE
	private boolean exampleTest() {
		Task t;
		
		ArrayList<ITask> tasks = alg.getInUseServers().get(0).getTasks();
		t=(Task)tasks.get(0);
		alg.endTask(t);
		return true;
	}
}
