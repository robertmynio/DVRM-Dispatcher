package workloadScheduler;

import java.io.File;
import java.io.IOException;
import commonDtos.ExtendedTaskDto;
import commonDtos.TaskDiskDto;
import commonInterfaces.Vars;
import datacenterInterface.dtos.jaxbBindingClasses.*;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import misc.GlobalVars;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.Serializable;
import java.util.*;

public class WTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WorkloadSchedulerAgent wagent = new WorkloadSchedulerAgent();
//		JAXBContext context = JAXBContext.newInstance(WorkloadSchedule.class);
//		Marshaller marshaller = context.createMarshaller();
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);       
//		marshaller.marshal(workloadSchedule, FISIER);
//		wagent.setup();
		wagent.run();
//		File file1 = new File ("myNote.txt");
//		if(file1.isFile()){
//			
//		}else{
//			try {
//				file1.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

}
