package testPackage;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.impl.Server;
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
		
		//THIS IS AN EXAMPLE
		result = exampleTest();
		System.out.println("Result for example test is : " + result);
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
	
	//THIS IS AN EXAMPLE
	private boolean exampleTest() {
		return true;
	}
}
