package rootservice;

import java.util.TimerTask;

import vdrm.base.impl.BaseCommon;
import datacenterInterface.dtos.jaxbBindingClasses.JaxbPair;

public class ON_TimedGeneratedTaskWrapper extends TimerTask{
private JaxbPair task;

	
	public ON_TimedGeneratedTaskWrapper(JaxbPair task) {
		super();
		this.task = task;
		
	}
	
	

	public JaxbPair getTask() {
		return task;
	}



	@Override
	public void run() {
		// fire an event
        BaseCommon.Instance().getXMLTaskGenerated().setChanged();
        BaseCommon.Instance().getXMLTaskGenerated().notifyObservers(this);
	}
	
	
}
