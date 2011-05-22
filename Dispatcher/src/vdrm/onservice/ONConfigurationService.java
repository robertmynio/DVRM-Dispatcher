package vdrm.onservice;

public class ONConfigurationService {
	public static String GetConfiguration(double cpu, int mem, int hdd,
			String hddPath){
//		 String vmTemplate =
//             "NAME     = vm_from_java    CPU ="+ cpu +"    MEMORY = "+mem+"" +
//             		"\n" +
//             		"OS     = [   boot = hd,   ROOT = hda]\n"
//           + "DISK   = [   #bus = virtio,"+
//   "type = disk,   clone=no,   size =4096,   format = ext3,   " +
//   "source= \"/home/oneadmin/Desktop/cuContextualizare/disk.0\",    target   = hda,   readonly = \"no\"   ]" 
//           + "# NIC     = [ NETWORK = \"Non existing network\" ]\n"
//           + "FEATURES = [ acpi=\"no\" ]";
		 String vmTemplate = "NAME   = task" + cpu + " \n" +
         "CPU    = " + cpu + "\n" +
         "MEMORY = " + mem + "\n" +
         "OS     = [\n" +
         "   boot = hd,\n" +
         "   ROOT = hda\n" +
         "    ]\n" + 		 
         "DISK   = [\n" +
                     "   type = disk,\n" +
                     "   clone=no,\n" +
                     "   size = 1024,\n" +
                     "   format = ext3,\n" +
                     "   source= \"/home/oneadmin/Desktop/cuContextualizare/disk.0\",\n" +
                     "   target   = hda,\n" +
                     "   readonly = \"no\" \n" +
                     "   ]\n";
		return vmTemplate;
	}
}
