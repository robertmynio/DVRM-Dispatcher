package vdrm.pred.miner;

import java.util.ArrayList;

public interface IPatternMiner {
	public void addElement(int element);
	public ArrayList<Integer> getPattern(int element, double minCredibility);
}
