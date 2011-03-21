package vdrm.base.impl;

import java.util.ArrayList;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;

public class Sorter {
	/******************************** INSERTION SORT ON SERVERS **************************/
	public ArrayList<IServer> insertSortServersDescending(ArrayList<IServer> source){
		int index = 1;
		while(index < source.size()){
			insertSortedServerDesc((IServer)source.get(index), source, index);
			index++;
		}
		
		return source;
	}

	public ArrayList<IServer> insertSortedServerDesc(IServer s, ArrayList<IServer> source, int index) {
		int loc = index-1;
		while( (loc >= 0) &&
				(s.compareTo(source.get(loc)) > 0)){
			source.set(loc+1, source.get(loc));
			loc--;
		}
		source.set(loc+1, s);
		return source;
	}
	
	public ArrayList<IServer> insertSortServersAscending(ArrayList<IServer> source){
		int index = 1;
		while(index < source.size()){
			insertSortedServerAsc((IServer)source.get(index), source, index);
			index++;
		}
		
		return source;
	}

	public ArrayList<IServer> insertSortedServerAsc(IServer s, ArrayList<IServer> source, int index) {
		int loc = index-1;
		while( (loc >= 0) &&
				(s.compareTo(source.get(loc)) <= 0)){
			source.set(loc+1, source.get(loc));
			loc--;
		}
		source.set(loc+1, s);
		return source;
	}
	
	public ArrayList<IServer> insertSortedServerGoingRightDesc(IServer s, ArrayList<IServer> source, int index) {
		int loc = index+1;
		while( (loc < source.size()) &&
				(s.compareTo(source.get(loc)) > 0)){
			source.set(loc+1, source.get(loc));
			loc++;
		}
		source.set(loc+1, s);
		return source;
	}
	
	/******************************** INSERTION SORT ON TASKS **************************/
	public ArrayList<ITask> insertSortTasksDescending(ArrayList<ITask> source){
		int index = 1;
		while(index < source.size()){
			insertSortedTaskDesc((ITask)source.get(index), source, index);
			index++;
		}
		
		return source;
	}

	private ArrayList<ITask> insertSortedTaskDesc(ITask s, ArrayList<ITask> source, int index) {
		int loc = index-1;
		while( (loc >= 0) &&
				(s.compareTo(source.get(loc)) > 0)){
			source.set(loc+1, source.get(loc));
			loc--;
		}
		source.set(loc+1, s);
		return source;
	}
	
	public ArrayList<ITask> insertSortTasksAscending(ArrayList<ITask> source){
		int index = 1;
		while(index < source.size()){
			insertSortedTaskAsc((ITask)source.get(index), source, index);
			index++;
		}
		
		return source;
	}

	private ArrayList<ITask> insertSortedTaskAsc(ITask s, ArrayList<ITask> source, int index) {
		int loc = index-1;
		while( (loc >= 0) &&
				(s.compareTo(source.get(loc)) <= 0)){
			source.set(loc+1, source.get(loc));
			loc--;
		}
		source.set(loc+1, s);
		return source;
	}
}
