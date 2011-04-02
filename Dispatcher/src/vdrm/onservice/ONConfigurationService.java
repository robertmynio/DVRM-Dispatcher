package vdrm.onservice;

public class ONConfigurationService {
	public static String GetConfiguration(int cpu, int mem, int hdd,
			String hddPath){
		 String vmTemplate =
             "NAME     = vm_from_java    CPU ="+ cpu +"    MEMORY = "+mem+"\n"
           + "DISK     = [\n"
           + "\tsource   = \""+hddPath+"\",\n"
           + "\ttarget   = \"hda\",\n"
           + "\treadonly = \"no\" ]\n"
           + "# NIC     = [ NETWORK = \"Non existing network\" ]\n"
           + "FEATURES = [ acpi=\"no\" ]"; 
		return vmTemplate;
	}
}
