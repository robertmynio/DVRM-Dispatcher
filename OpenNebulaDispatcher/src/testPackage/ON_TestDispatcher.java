package testPackage;

import java.util.ArrayList;

import rootservice.ON_RootService;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;

public class ON_TestDispatcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ON_RootService rs = ON_RootService.Instance();
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

		rs.ParseWorkloadXML("workload1.xml");

	}

}
