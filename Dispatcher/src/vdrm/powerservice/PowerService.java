package vdrm.powerservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;



import vdrm.base.data.IServer;
import vdrm.base.impl.BaseCommon;

public class PowerService {
	public void wakeUpServer(IServer s){
		if(BaseCommon.PSEnabled){
			try {
	            String cmd = "wakeonlan " + s.getMacAddress();
	            Process proc = Runtime.getRuntime().exec(cmd);
	            OutputStream outputStream = proc.getOutputStream();
	            InputStream stdin = proc.getInputStream();
	            InputStreamReader isr = new InputStreamReader(stdin);
	    
	          	BufferedReader br = new BufferedReader(isr);
	            String line = br.readLine();
	            if (line != null) {
	                System.out.println(line);
	 
	                proc.destroy();
	            }
	            waitUntilTargetIsAlive(s.getIPAddress()); // Hostname = the IP
	            waitUntilSSHAvailable(s.getIPAddress());
	            System.err.println("Wait ended");
	        } catch (Exception ex) {
	            System.err.println("Error:" + ex.getMessage());
	            System.out.println("Error:" + ex.getMessage());
	            ex.printStackTrace();
	            
	        }
			
	//        Client client = null;
	//        try {
	//            client = new Client(NEBULA_CREDENTIALS, NEBULA_RCP_ADDRESS);
	//        } catch (Exception e) {
	//            throw new ServiceCenterAccessException(e.getMessage(), e.getCause());
	//        }
	//        Host host = new Host(physicalHost.getId(), client);
	//        host.enable();
			}
	}
	private void waitUntilTargetIsAlive(String ip){
			String pingCmd = "/bin/ping " + ip;
	        boolean ok = false;
	        while (!ok) {
	            try {
	                Runtime r = Runtime.getRuntime();
	                Process p = r.exec(pingCmd);

	                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                String inputLine;
	                while ((inputLine = in.readLine()) != null) {
	                    System.out.println(inputLine);

	                    if (!inputLine.toLowerCase().contains("unreachable") && !inputLine.toLowerCase().equals("") &&
	                            inputLine.toLowerCase().contains("64 bytes")) {
	                        ok = true;
	                        break;
	                    }

	                }
	                in.close();

	            }
	            catch (IOException e) {
	                System.out.println(e);
	            }
	        }
	}
	
	public void waitUntilSSHAvailable(String ip){
		try {
            String cmd = "/usr/bin/ssh " + ip;
            Process proc = Runtime.getRuntime().exec(cmd);

            InputStreamReader isr = new InputStreamReader(proc.getErrorStream());
            BufferedReader br = new BufferedReader(isr);

            {
                String line = br.readLine();
                if (line != null) {
                    System.out.println(line);
                    if (line.contains("Connection refused")) {
                        br.close();
                        proc.destroy();
                        waitUntilSSHAvailable(ip);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());

        }
	}
	
	public void sendServerToSleep(IServer s){
		if(BaseCommon.PSEnabled){
			try {
	            String cmd = "/bin/sh";
	            Process proc = Runtime.getRuntime().exec(cmd);
	            OutputStream outputStream = proc.getOutputStream();
	            InputStream stdin = proc.getInputStream();
	            InputStreamReader isr = new InputStreamReader(stdin);
	            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
	            bufferedWriter.write("/usr/bin/ssh " + s.getIPAddress() + " \"echo \"disk\" > /sys/power/state\"");
	            bufferedWriter.newLine();
	            bufferedWriter.flush();
	            bufferedWriter.close();
	        } catch (Exception ex) {
	            System.err.println("Error:" + ex.getMessage());
	            System.out.println("Error:" + ex.getMessage());
	            ex.printStackTrace();
	        }
		}
	}
}
