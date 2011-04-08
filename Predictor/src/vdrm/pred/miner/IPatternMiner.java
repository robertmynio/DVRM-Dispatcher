package vdrm.pred.miner;

import java.util.ArrayList;
import java.util.UUID;

public interface IPatternMiner {
	public void addElement(UUID element);
	public ArrayList<UUID> getPattern(UUID element, double minCredibility);
}
