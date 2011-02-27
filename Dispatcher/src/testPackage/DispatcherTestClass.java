package testPackage;

import vdrm.disp.util.VDRMLogger;


public class DispatcherTestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VDRMLogger logger = new VDRMLogger("dispatcherLog.xml");
		
		logger.logInfo("Some info about something");
		logger.logSevere("Severe message");
		logger.logWarning("Warning message");
	}

}
