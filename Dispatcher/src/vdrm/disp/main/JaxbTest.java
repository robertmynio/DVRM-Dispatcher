package vdrm.disp.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import vdrm.base.data.ITask;
import vdrm.base.impl.Server;
import vdrm.base.impl.Task;

public class JaxbTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*JaxbConfig config1 = new JaxbConfig();
		config1.setHistoryPath("history1");
		config1.setServersPath("server1");
		config1.setTasksPath("tasks1");
		config1.setWorkloadPath("workload1");
		
		JAXBContext context = JAXBContext.newInstance(JaxbConfig.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(config1, System.out);

		Writer w = null;
		try {
			w = new FileWriter("..\\Config\\config1.xml");
			m.marshal(config1, w);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
			}
		}*/
		ArrayList<Server> servs = new ArrayList<Server>();
		
		Task task;
		ArrayList<Task> tasks = new ArrayList<Task>();
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
		
		JaxbTasks t = new JaxbTasks();
		t.setTasksList(tasks);

		
		Server s = new Server(12000,2000,1000000);
		s.setServerID("3");
		s.setCoreFreq(3000);
		s.setIpAddress("192.168.1.15");
		s.setMacAddress("70:71:bc:b1:6f:e5");
		servs.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("0");
		s.setCoreFreq(3000);
		s.setIpAddress("192.168.1.11");
		s.setMacAddress("00:27:0e:17:df:05");
		servs.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("2");
		s.setCoreFreq(3000);
		s.setIpAddress("192.168.1.14");
		s.setMacAddress("20:cf:30:52:73:20");
		servs.add(s);
		s = new Server(24000,6000,1000000);
		s.setServerID("4");
		s.setCoreFreq(3000);
		s.setIpAddress("192.168.1.13");
		s.setMacAddress("00:27:0e:17:df:1f");
		servs.add(s);
		
		JaxbServers j = new JaxbServers();
		j.setServersList(servs);
		
		ArrayList<Integer> hist = new ArrayList<Integer>();
		hist.add(0);
		hist.add(1);
		hist.add(2);
		hist.add(3);
		
		hist.add(5);
		hist.add(4);
		hist.add(0);
		hist.add(1);
		
		hist.add(2);
		hist.add(3);
		hist.add(1);
		hist.add(2);
		
		hist.add(3);
		hist.add(4);
		hist.add(1);
		hist.add(2);
		
		hist.add(3);
		hist.add(5);
		hist.add(4);
		hist.add(5);
		
		hist.add(4);
		hist.add(5);
		hist.add(4);
		hist.add(5);
		
		hist.add(4);
		hist.add(5);
		hist.add(1);
		
		JaxbHistory history = new JaxbHistory();
		history.setTasksHistory(hist);
		
		Unmarshaller um=null;
		JAXBContext context =null;
		try {
			context = JAXBContext.newInstance(JaxbTasks.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Marshaller m=null;
		try {
			m = context.createMarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			m.marshal(t, System.out);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		Writer w = null;
		try {
			try {
				w = new FileWriter("..\\Config\\tasks1.xml");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				m.marshal(t, w);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				w.close();
			} catch (Exception e) {
			}
		}
		
	}

}
