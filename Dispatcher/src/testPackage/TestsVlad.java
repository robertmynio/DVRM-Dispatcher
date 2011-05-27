package testPackage;

import java.util.ArrayList;
import java.util.UUID;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;
import vdrm.base.util.Sorter;
import vdrm.disp.alg.Algorithm;
import vdrm.disp.dispatcher.Dispatcher;
import workloadScheduler.WorkloadSchedulerAgent;

public class TestsVlad {
	private Algorithm alg;
	private Dispatcher rs;
	
	public TestsVlad() {
		alg = new Algorithm();
		rs = Dispatcher.Instance();
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
		s.setServerID("3");
		s.setCoreFreq(3000);
		s.setIPAddress("192.168.1.15");
		s.setMacAddress("70:71:bc:b1:6f:e5");
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("0");
		s.setCoreFreq(3000);
		s.setIPAddress("192.168.1.11");
		s.setMacAddress("00:27:0e:17:df:05");
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("2");
		s.setCoreFreq(3000);
		s.setIPAddress("192.168.1.14");
		s.setMacAddress("20:cf:30:52:73:20");
		servers.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("4");
		s.setCoreFreq(3000);
		s.setIPAddress("192.168.1.13");
		s.setMacAddress("00:27:0e:17:df:1f");
		servers.add(s);

		//alg.initialize(servers);
		
		
		
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		task = new Task(500,2500,12500);
		tasks.add(task);
		task = new Task(1750,600,6000);
		tasks.add(task);
		task = new Task(700,600,6000);
		tasks.add(task);
		task = new Task(700,752,10000);
		tasks.add(task);
		task = new Task(1100,500,5000);
		tasks.add(task);
		task = new Task(1350,800,8000);
		tasks.add(task);
		
		ArrayList<ITask> taskHistory = new ArrayList<ITask>();
		taskHistory.add(tasks.get(0));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(0));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(1));
		taskHistory.add(tasks.get(2));
		taskHistory.add(tasks.get(3));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
//		
//		taskHistory.add(tasks.get(5));	//remove this line and the following 5 to make (5,4) and (1,2,3) credibility 100%
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(4));
		taskHistory.add(tasks.get(5));
		taskHistory.add(tasks.get(1));
		
		//TaskDao dao = new TaskDao();
		//ArrayList<ITask> tasks = dao.getAllTasks();
		//ArrayList<ITask> taskHistory = dao.getTaskHistory();
		rs.worker.initialize(servers, tasks, taskHistory);
		rs.ParseWorkloadXML("workload2.xml");
//		WorkloadSchedulerAgent wagent = new WorkloadSchedulerAgent();
//		wagent.run();
//		task = (Task) tasks.get(5);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,1800);
//		task = (Task) tasks.get(4);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,2600);
//		task = (Task) tasks.get(1);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,3900);
//		task = (Task) tasks.get(3);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,1800);
//		task = (Task) tasks.get(0);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,5700);
//		task = (Task) tasks.get(3);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,1800);
//		task = (Task) tasks.get(2);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,2800);
//		task = (Task) tasks.get(1);
//		task.setTaskHandle(UUID.randomUUID());
//		rs.TaskArrived(task,1800);
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
		Dispatcher.Instance().worker.initialize(servers);
		//servers = alg.getEmptyServers();
		servers = ((Algorithm)Dispatcher.Instance().worker).getEmptyServers();
		if(servers.size()==3)
			return true;

		return false;
	} 
	
	//THIS IS AN EXAMPLE
	private boolean exampleTest() {
		Task t;
		
//		ArrayList<ITask> tasks = alg.getInUseServers().get(0).getTasks();
//		t=(Task)tasks.get(1);
//		alg.endTask(t);
		return true;
	}
}
