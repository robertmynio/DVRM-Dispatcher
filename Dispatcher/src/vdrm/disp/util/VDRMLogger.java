package vdrm.disp.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import vdrm.base.impl.BaseCommon;
import vdrm.base.util.ILogger;

public class VDRMLogger implements ILogger {
	public FileHandler logFileHandler;
	public Logger log;
	
	public VDRMLogger(){
		try{
			logFileHandler = new FileHandler("baselog.log.xml");
			log = Logger.getLogger("vdrm.disp.util");
			log.addHandler(logFileHandler);
		}catch(SecurityException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public VDRMLogger(String logFile){
		try{
			logFileHandler = new FileHandler(logFile);
			log = Logger.getLogger("vdrm.disp.util");
			log.addHandler(logFileHandler);
		}catch(SecurityException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void logInfo(String message) {
		if(BaseCommon.logEnabled)
			this.log.log(Level.INFO, message);
	}

	@Override
	public void logSevere(String message) {
		if(BaseCommon.logEnabled)
			this.log.log(Level.WARNING, message);
	}

	@Override
	public void logWarning(String message) {
		if(BaseCommon.logEnabled)
			this.log.log(Level.SEVERE, message);
	}

}
