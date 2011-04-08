package vdrm.pred.miner;

import java.util.ArrayList;

public interface IPatternMiner {
	public void addElement(byte element);
	public ArrayList<Byte> getPattern(byte element, double minCredibility);
}
