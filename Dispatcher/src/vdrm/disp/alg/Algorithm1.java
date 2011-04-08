package vdrm.disp.alg;

import java.util.ArrayList;
import java.util.Date;

import vdrm.base.common.IAlgorithm;
import vdrm.base.common.IPredictor;
import vdrm.base.data.IPrediction;
import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.base.impl.Server;
import vdrm.base.impl.Sorter;
import vdrm.disp.util.VDRMLogger;
import vdrm.onservice.IOpenNebulaService;
import vdrm.onservice.OpenNebulaService;
import vdrm.pred.pred.Predictor;

/***
 * TODO: findNewPosition can use insertSortedServer method! May be the same as
 * 		reorder server list, but called when adding a new task. Might be able to
 * 		make a single method out of the 2.
 * TODO: 
 * @author Vlad & Robi
 *
 */
public class Algorithm1 implements IAlgorithm{

	private ArrayList<IServer> emptyServers;
	private ArrayList<IServer> inUseServers;
	private ArrayList<IServer> fullServers;
	private ArrayList<ITask> unassignedTasks;
	private ArrayList<ITask> predictedTasks;
	private IPredictor predictor;
	private Sorter sortingService;
	
	private VDRMLogger logger;
	
	private IOpenNebulaService onService;
	
	@Override
	public void initialize(ArrayList<IServer> servers) {
		//initialize data structures
		emptyServers = servers;
		inUseServers = new ArrayList<IServer>();
		fullServers = new ArrayList<IServer>();
		unassignedTasks = new ArrayList<ITask>();
		predictedTasks = new ArrayList<ITask>();
		
		//initialize prediction and predictor
		predictor = new Predictor();
		
		//initialize sorting service
		sortingService = new Sorter();
		
		//initialize logger
		logger = new VDRMLogger();
		
		//initialize OpenNebula
		onService = new OpenNebulaService();
	}

	@Override
	public void newTask(ITask newTask) {
		//we use an OPTIMISTIC approach (regarding the prediction)
		//if predicted tasks list has tasks in it, than we need to deal with prediction
		int i;
		if(predictedTasks.size()>0) {
			if(newTask.equals(predictedTasks.get(0))) { 
					//prediction is correct
					newTask = predictedTasks.get(0);
					newTask.setPredicted(false);
					
					// OpenNebula
					if(newTask.getServerId() != null){
						//onService.DeployTask(newTask);
					}
					
					predictedTasks.remove(0);
					
					//since the prediction was correct, we exit algorithm and wait for next task
					return;					
				}
			else { 	
				//prediction was not correct
				
				//since we used an optimistic approach, we have to remove the tasks from the servers
				IServer tempServer;
				ITask tempTask;
				for(i=0;i<predictedTasks.size();i++) {
					tempTask = predictedTasks.get(i);
					tempServer = tempTask.getServer();
					tempServer.removeTask(tempTask);
				}
				
				//clear list of predicted tasks
				predictedTasks = new ArrayList<ITask>();
				
				//add this task to unassigned tasks list
				unassignedTasks.add(newTask);
			}
		}
		else
			unassignedTasks.add(newTask);

		//run prediction and add predicted tasks to unassignedTasks list
		ArrayList<ITask> prediction;
		prediction = predictor.predict(newTask);
		if(prediction!=null) {
			int predSize = prediction.size();
			for(i=0;i<predSize;i++)
				unassignedTasks.add(prediction.get(i));
		}
		
		//run consolidation algorithm (distribute tasks from unassignedTasks list to servers
		//this algorithm assigns to every task a server reference and to every server a task 
		//reference (the task is actually added to the list of a server, but the task is not 
		//actually deployed to a physical server)
		
		
		
		
		consolidateTasks();
		
		//dispatch tasks which are not predicted to real servers
		for(i=0;i<unassignedTasks.size();i++) {
			ITask tempTask = unassignedTasks.get(i);
			if(tempTask.isPredicted())
				predictedTasks.add(tempTask);
			else {
				
				// OpenNebula
				if(tempTask.getServerId() != null){
					//onService.DeployTask(tempTask);
				}
			}		
		}
		
		//clear unassigned tasks list
		unassignedTasks = new ArrayList<ITask>();
	}
	
	/**
	 * This methods received as input a list of tasks and sorts it 
	 * in an ascending order. The criteria is the following:
	 * Sort according to cpu. If two tasks have equal cpu, sort 
	 * according to mem. If two tasks have equal mem, sort according
	 * to hdd.
	 * @param list - the list of tasks that must be sorted
	 */
	private void sort(ArrayList<ITask> list) {
		list = sortingService.insertSortTasksAscending(list);
	}
	
	/**
	 * This method deals with placing a server in the correct list
	 * First, if the server is in the inUseServers list, it is removed 
	 * and its former position is saved. 
	 * Then, if the server is full, it is placed in the fullServers list.
	 * If not, a new position is searched starting from the current 
	 * position and moving towards the front of the list 
	 * @param tempServer - the server that needs to be repositioned
	 */
	private void findNewPosition(IServer tempServer) {
		int poz = 0;

		if(inUseServers.contains(tempServer)){
			poz = inUseServers.indexOf(tempServer);
			inUseServers.remove(tempServer);
		}
		
		if(tempServer.isFull()) 
			fullServers.add(tempServer);
		else {
			boolean ok = false;
			if(inUseServers.isEmpty() && inUseServers.contains(tempServer) == false) {
				inUseServers.add(tempServer);
				ok = true;
				return;
			}
			if(poz>0){ 
				poz--;
				while(ok==false && poz>=0) {	
					if(tempServer.compareTo(inUseServers.get(poz))<0 && inUseServers.contains(tempServer) == false) {
						inUseServers.add(poz+1,tempServer);
						ok = true;
					}
					poz--;
				}	
				// place it last
				if(poz==0 && inUseServers.contains(tempServer) == false){
					inUseServers.add(inUseServers.size()+1,tempServer);
				}
			}else if(poz==0){
				poz = inUseServers.size()-1;
				if(poz > 0){
					while(ok==false&&poz>=0){
						int compareResult = tempServer.compareTo(inUseServers.get(poz));
						if(compareResult < 0 || compareResult == 0){
							inUseServers.add(poz,tempServer);
							ok=true;
						}
						poz--;
					}
				}else 
					if(poz == 0)
						inUseServers.add(poz+1, tempServer);
					else
						inUseServers.add(0,tempServer);
				
			}
		}
	}
	
	/**
	 * There are a number of tasks in the unassignedTasks list
	 * This method assigns each task to a server (and also a server to a task)
	 * OPTIMISTIC approach!
	 */
	private void consolidateTasks()
	{
		ArrayList<ITask> tempList = unassignedTasks;
		
		//sort the array in an ascending order (according to cpu, then mem, then hdd)
		sort(tempList);
		
		int i = 0;
		ITask tempTask;
		IServer tempServer;
		tempTask = tempList.get(0);
		
		while(!tempList.isEmpty() && i<inUseServers.size()) {
			tempServer = inUseServers.get(i);
			if(tempServer.meetsRequirments(tempTask)) {
				//this task fits on server i
				tempTask.setServer(tempServer);
				tempServer.addTask(tempTask);
				
				//we must move this server to its new position in the lists
				findNewPosition(tempServer);
				
				tempList.remove(0);
				if(!tempList.isEmpty())
					tempTask = tempList.get(0);
			}
			else
				i++;
		}
		
		if(!tempList.isEmpty()) {
			ArrayList<IServer> newServers = new ArrayList<IServer>();
			boolean ok;
			int tempListSize = tempList.size();
			while(!tempList.isEmpty()) {
				i = 0;
				ok = false;
				
				//tempTask is the last task in the tempList
				tempTask = tempList.get(tempListSize-1);
				tempListSize--;
				
				while(i<newServers.size() && ok==false) {
					tempServer = newServers.get(i);
					if(tempServer.meetsRequirments(tempTask)) {
						tempTask.setServer(tempServer);
						tempServer.addTask(tempTask);						
						tempList.remove(tempListSize);
						ok = true;
					}
					else 
						i++;
				}
				
				if(ok==false) {
					//we must use a new server
					if(emptyServers.size() == 0) {
						Date d = new Date();
						// TODO We don't have empty servers. What do we do ? Wait, sleep, error ?
						// Vlad: WE should Log the event and/or queue the task. Maybe later alligator
						logger.logSevere("SEVERE: Task " + tempTask.getTaskHandle().toString() + " cannot be deployed at this moment (" +
								d.toString());
						return;
					}else{
						IServer newServer = emptyServers.get(0);
						newServers.add(newServer);
						emptyServers.remove(0);
						
						tempTask.setServer(newServer);
						newServer.addTask(tempTask);
						tempList.remove(tempListSize);
						ok = true;
					}
				}
			}
			
			//all tasks have been deployed
			if(newServers.size()>0) {
				for(i=0;i<newServers.size();i++)
					//we must move the servers to their new position in the lists
					findNewPosition(newServers.get(i));
			}
		}	
	}

	/***********************************************************/
	/***********************************************************/
	/********************* TASK ENDS ***************************/
	/***********************************************************/
	/***********************************************************/
	
	/*
	 * Main function which gets called when a task ends.
	 * It treats 2 cases:
	 * 
	 * 1) If the server has a load lower than 50%, 
	 * 		a) If it has a small number of tasks, try to redistribute them
	 * 			on other servers and close this server. 
	 * 		b) If it has a large number of tasks and migration time 
	 * 			would be large, try to fill the server with tasks from the
	 * 			last 2 in-use servers (which can make up to a full server).
	 * 
	 * 2) If the server became empty because of the ended task, close it.
	 * 		Else, try to fill the server with tasks from the
	 * 		last 2 in-use servers (which can make up to a full server).
	 * 		
	 */
	@Override
	public void endTask(ITask t){
		IServer server = t.getServer();
		
		// remove task from server (this is the point of this method, DUUUH !)
		//onService.FinishTask(t);
		server.removeTask(t);
		t.setServer(null);
		
		// now make some house cleaning and ordering
		if(server.getLoad() <= 50){
			if(server.getTotalNumberOfTasks() > 0){
				if(inUseServers.size() > 1){
					if(server.getTotalNumberOfTasks() < BaseCommon.Instance().getNrOfTasksThreshold()){
						if(!redistributeTasks(server)){
							if(inUseServers.size() > 0){
								reorderServerList(server, -1);
							}
						}
					}else{
						tryToFillServer(server);
					}
				}else{
					// do nothing, there is only this one server left.
				}
			}else{
				// the server is now empty, sleep
				if(server.getTotalNumberOfTasks() == 0)
					server.OrderStandBy();
			}
		}else{
			if(server.getTotalNumberOfTasks() == 0)
				server.OrderStandBy();
			else
				tryToFillServer(server);
		}
	}

	/***
	 * Using the last 2 servers in the inUseList, try to fill a server in order to
	 * maximize the number of full servers
	 */
	@Override
	public ITask[] findMaximumUtilizationPlacement(IServer server,
			IServer secondToLastServer, IServer lastServer) {
		ArrayList<Integer> destinationResources = server.GetAvailableResources();
		ITask t1, t2;
		
		int nrTasks1, nrTasks2;
		nrTasks1 = lastServer.getTotalNumberOfTasks();
		nrTasks2 = secondToLastServer.getTotalNumberOfTasks();
		
		// try to empty the server with the fewest tasks
		if(nrTasks1 <= nrTasks2){
			for(ITask t:lastServer.getTasks()){
				if(server.compareAvailableResources(t) && !server.isFull()){
					//remove task from current server
					lastServer.removeTask(t);
					// set the new server
					t.setServer(server);
					
					// add task to new server
					server.addTask(t);
					if(server.isFull()){
						fullServers.add(server);
						break;
					}
					//onService.MigrateTask(t, server);
				}
			}
			
			if(lastServer.isEmpty()){
				inUseServers.remove(inUseServers.indexOf(lastServer));
				emptyServers.add(lastServer);
			}
			
			// server is not full. start with second to last server
			if(!server.isFull()){
				ITask t;
				while(( t = secondToLastServer.GetNextLowestDemandingTask())!=null && (!server.isFull())){
					if(server.compareAvailableResources(t) && !server.isFull()){
						secondToLastServer.removeTask(t);
						t.setServer(server);
						server.addTask(t);
						if(server.isFull()){
							fullServers.add(server);
							break;
						}
					}
				}
			}
			
			if(secondToLastServer.isEmpty()){
				inUseServers.remove(inUseServers.indexOf(secondToLastServer));
				emptyServers.add(secondToLastServer);
			}
		}else{
			for(ITask t:secondToLastServer.getTasks()){
				if(server.compareAvailableResources(t) && !server.isFull()){
					//remove task from current server
					secondToLastServer.removeTask(t);
					// set the new server
					t.setServer(server);
					
					// add task to new server
					server.addTask(t);
					if(server.isFull()){
						fullServers.add(server);
						break;
					}
					//onService.MigrateTask(t, server);
				}
			}
			
			if(secondToLastServer.isEmpty()){
				inUseServers.remove(inUseServers.indexOf(secondToLastServer));
				emptyServers.add(secondToLastServer);
			}
			
			// server is not full. start with second to last server
			if(!server.isFull()){
				ITask t;
				while(( t = lastServer.GetNextLowestDemandingTask())!=null && (!server.isFull())){
					if(server.compareAvailableResources(t) && !server.isFull()){
						lastServer.removeTask(t);
						t.setServer(server);
						server.addTask(t);
						if(server.isFull()){
							fullServers.add(server);
							break;
						}
					}
				}
			}
			if(lastServer.isEmpty()){
				inUseServers.remove(inUseServers.indexOf(lastServer));
				emptyServers.add(lastServer);
			}
		}
		return null;
	}

	@Override
	public boolean redistributeTasks(IServer server) {
		int noPlaceFound = 0;
		boolean found = false;
		
		while((!server.isEmpty() || emptyServers.size()==0 )&& noPlaceFound < server.getTotalNumberOfTasks()){
			ITask t = server.GetNextLowestDemandingTask();
			for(IServer s:inUseServers){
				if(s.compareAvailableResources(t)){
					
					// TODO migrate_to_new_host(s); -- OpenNebula
					//onService.MigrateTask(t, s);
					
					fullServers.add(s);
					inUseServers.remove(s);
					found = true;
					break;
				}
			}
			if(!found){
				noPlaceFound++;
				t.setUnsuccessfulPlacement(true);
			}
		}
		
		if(!server.isEmpty()){
			for(ITask t:server.getTasks()){
				t.setUnsuccessfulPlacement(false);
			}
			return false;
		}else{
			return true;
		}
	}

	/***
	 * This method reorders the inUseServer list. 
	 * It is based on the fact that once a task is added/finished on a 
	 * specific server, only that server needs to be reinserted in the 
	 * list such that the list remains ordered.
	 * 
	 * If direction < 0 , a task has ended and server has to be reordered
	 * by going right in the list until a server with a smaller utilization is found.
	 * 
	 * If direction > 0 , a task has been placed and server has to be
	 * reorder by going left in the list until a server with a bigger utilization is found.
	 */
	@Override
	public void reorderServerList(IServer server, int direction) {
		sortingService = new Sorter();
		if(direction < 0){
			// TODO: Check/Test this method...
			sortingService.insertSortedServerGoingRightDesc(server, inUseServers,
					inUseServers.indexOf(server));
		}else{
			inUseServers = sortingService.insertSortedServerDesc(server, inUseServers,
								inUseServers.indexOf(server));
		}
	}

	@Override
	public void tryToFillServer(IServer server) {
		IServer lastServer = inUseServers.get(inUseServers.size()-1);
		
		ArrayList<Integer> availableResources = server.GetAvailableResources();
		ITask bingoTask = lastServer.GetTaskWithResources(availableResources);
		if(bingoTask != null){
			
			// OPEN NEBULA
			//onService.MigrateTask(bingoTask, server);
			
			fullServers.add(server);
			inUseServers.remove(server);
		}else{
			// TODO: check if there are at least 2 servers in the list
			if(inUseServers.size() >=2 ){
				ITask[] fittingTasks = findMaximumUtilizationPlacement(server, 
						inUseServers.get(inUseServers.size()-2), lastServer);
//				
//				if(fittingTasks.length > 0){
//					for(ITask t:fittingTasks){
//						// TODO migrate_to_new_host(t); -- OpenNebula
//						//onService.MigrateTask(t, server);
//					}
//					fullServers.add(server);
//					inUseServers.remove(server);
//				}else{
//					reorderServerList(server, -1);
//				}
			}else{
				//
			}
		}
	}
	
	// GETTERS FOR TESTING PURPOSES
	
	
	public ArrayList<IServer> getEmptyServers() {
		return emptyServers;
	}

	public ArrayList<IServer> getInUseServers() {
		return inUseServers;
	}

	public ArrayList<IServer> getFullServers() {
		return fullServers;
	}

	public ArrayList<ITask> getUnassignedTasks() {
		return unassignedTasks;
	}

	public ArrayList<ITask> getPredictedTasks() {
		return predictedTasks;
	}
}
