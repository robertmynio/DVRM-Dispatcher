package vdrm.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public class VDRM_Hash<Integer, Object> extends Hashtable {

	public void putObject(Object key, Object value) {

		// memory hashtable for CPU bucket
		VDRM_Hash<Integer, Object> htMem;

		// get the memory hashtable for CPU bucket if it exists, if not, create
		// it
		Object existing = (Object) get(key);

		// if the CPU bucket already contains a hashtable for memory
		if (existing != null) {
			if (existing instanceof VDRM_Hash<?, ?>) {
				htMem = (VDRM_Hash<Integer, Object>) existing;
				// put object in memory hash, where key = mem score and value =
				// server
				putObjectInMemHash(htMem, key, value);
			}
		} else {
			// create a new hashtable for memory
			htMem = new VDRM_Hash<Integer, Object>();
			// put object in memory hash, where key = mem score and value =
			// task
			putObjectInMemHash(htMem, key, value);
		}
		

	}

	private void putObjectInMemHash(VDRM_Hash<Integer, Object> memHash,
			Object key, Object value) {
		// hdd hashtable for MEM bucket
		VDRM_Hash<Integer, Object> htHdd;

		// get the hdd hashtable for MEM bucket if it exists, if not, create
		// it
		Object existing = (Object)memHash.get(key);
		if(existing != null){
			if (existing instanceof VDRM_Hash<?, ?>) {
				htHdd = (VDRM_Hash<Integer, Object>) existing;
				// put object in hdd hash, where key = hdd score and value =
				// server
				putObjectInMemHash(htHdd, key, value);
			}
		}else{
			htHdd = new VDRM_Hash<Integer, Object>();
			putObjectInHddHash(htHdd, key, value);
		}
	}

	private void putObjectInHddHash(VDRM_Hash<Integer, Object> hddHash,
			Object key, Object value) {
		 
		IServer newServer = (IServer)value;
		Hashtable<String, IServer> similarServers;
 
		Object existing = (Object)hddHash.get(key);
		if(existing != null){
			if(existing instanceof Hashtable){
				similarServers = (Hashtable<String, IServer>)existing;
				similarServers.put("ServerID", newServer);
			}else{
				
				// this shouldn't happen...
			}
		}else{
			similarServers = new Hashtable<String, IServer>();
			//ArrayList<ITask> identicalList = new ArrayList<ITask>();
			//identicalList.add(newTask);
			similarServers.put("ServerID", newServer);
			
			//put(hddHash,identicalList);
			// put in hdd Hash the hashtable for the similar servers with common specifications
			hddHash.put(key, similarServers);
		}
	}
}
