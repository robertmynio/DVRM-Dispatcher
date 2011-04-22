package testPackage;

import java.io.Console;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.*;

import vdrm.base.impl.BaseCommon.VMDeployedEvent;
import vdrm.base.impl.BaseCommon.VMDeployedEventListener;
public class runTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TestsRobi testsRobi = new TestsRobi();
		//testsRobi.runAllTests();
		
//		TestsVlad testsVlad = new TestsVlad();
//		testsVlad.runAllTests();	
		runTests r = new runTests();
		r.Go();
		
	}
	
	public void Go(){
		MyModel model = new MyModel();
		
		//register for events
		model.addObserver(new Observer(){
			public void update(Observable o, Object arg){
				System.out.println("Event occured");
				if(arg != null){
					System.out.println(arg.toString());	
				}
			}
		}
		);
		
		model.setChanged();
		
		Object arg = "some info about the event";
		model.notifyObservers(arg);
	}

	/***
	 * Old way - hardcore way
	 * @author Gygabite
	 *
	 */
	public class EventTest{
		protected EventListenerList listenerList = new EventListenerList();
		
		public void subscribeEventListener(VMDeployedEventListener el){
			listenerList.add(VMDeployedEventListener.class, el);
		}
		
		public void unsubscribeEventListener(VMDeployedEventListener el){
			listenerList.remove(VMDeployedEventListener.class, el);
		}
		
		public void fireVMDeployedEvent(VMDeployedEvent evt){
			Object[] listeners = listenerList.getListenerList();
	        // Each listener occupies two elements - the first is the listener class
	        // and the second is the listener instance
	        for (int i=0; i<listeners.length; i+=2) {
	            if (listeners[i]==VMDeployedEventListener.class) {
	                ((VMDeployedEventListener)listeners[i+1]).vmDeployedOccured(evt);
	            }
	        }

		}
	}

	public class MyModel extends Observable{
		public synchronized void setChanged(){
			super.setChanged();
		}
	}
}
