package vdrm.disp.common;

import java.util.ArrayList;

import vdrm.base.data.IServer;

public class BaseCommon {
	public static ArrayList<IServer> notFullServers;
	public static ArrayList<IServer> fullServers;
	public static ArrayList<IServer> emptyServers;
	
	public static final double CPU_WEIGHT = 0.6;
	public static final double MEM_WEIGHT = 0.3;
	public static final double HDD_WEIGHT = 0.1;
	
	private static BaseCommon instance;
	
	public static BaseCommon Instance(){
		if(instance == null){
			instance = new BaseCommon();
		}
		return instance;
	}
	
	private BaseCommon(){
		notFullServers = new ArrayList<IServer>();
		fullServers = new ArrayList<IServer>();
		emptyServers = new ArrayList<IServer>();
	}
}
