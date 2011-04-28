package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Sorter;
import vdrm.base.impl.Task;
import vdrm.disp.alg.Algorithm1;
import vdrm.pred.dao.TaskDao;
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
		//result = hardcodedInitTest();
		//System.out.println("Result for hardcoded init test is : " + result);
		
		
		// task initialization
		Task task;
		
		Server s;
		ArrayList<IServer> servers = new ArrayList<IServer>();
		
		//simple initialization -> HARDCODED!!!
		s = new Server(12000,2000,1000000);
		s.setServerID("1");
		s.setCoreFreq(3000);
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("2");
		s.setCoreFreq(3000);
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("3");
		s.setCoreFreq(3000);
		servers.add(s);

		//alg.initialize(servers);
		
		
		TaskDao dao = new TaskDao();
		ArrayList<ITask> tasks = dao.getAllTasks();
		ArrayList<ITask> taskHistory = dao.getTaskHistory();
		RootService.Instance().worker.initialize(servers, tasks, taskHistory);
		rs.TaskArrived(tasks.get(5),1800);
		rs.TaskArrived(tasks.get(4),1800);
		rs.TaskArrived(tasks.get(2),2800);
		rs.TaskArrived(tasks.get(3),1800);
		rs.TaskArrived(tasks.get(2),2800);
		rs.TaskArrived(tasks.get(3),1800);
		rs.TaskArrived(tasks.get(2),2800);
		rs.TaskArrived(tasks.get(1),1800);
		//rs.TaskArrived(tasks.get(1),1800);

//		System.out.println("DONE ");
		
	}
	
	private boolean hardcodedInitTest() {
		Server s;
		ArrayList<IServer> servers = new ArrayList<IServer>();
		
		//simple initialization -> HARDCODED!!!
		s = new Server(12000,2000,1000000);
		s.setServerID("1");
		s.setCoreFreq(3000);
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("2");
		s.setCoreFreq(3000);
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("3");
		s.setCoreFreq(3000);
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
