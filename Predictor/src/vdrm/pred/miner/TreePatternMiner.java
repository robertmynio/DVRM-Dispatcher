package vdrm.pred.miner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreePatternMiner implements IPatternMiner{

	private Node root;
	private ArrayList<UUID> window;
	private static final byte WINDOW_SIZE = 3;
	
	private int startOccurence;
	private ArrayList<Node> solution;
	private double patternProbability;
	
	public TreePatternMiner() {
		root = new Node();
		window = new ArrayList<UUID>();
	}
	
	public void addElement(UUID element) {
		//add the element to the end of the window
		window.add(element);
		if(window.size()>WINDOW_SIZE)
			window.remove(0);
		
		//starting from the end, insert in turns patterns of length 1,2,3...
		List<UUID> pattern;
		Node node = null;
		int windowSize = window.size();
		int patternSize;
		int i,j;
		UUID key;
		for(i=0;i<windowSize;i++) {
			//tempList is i length long pattern (i = 1,2,3...)
			pattern = window.subList(windowSize-1-i, windowSize);
			
			//size2 is size of tempList
			patternSize = pattern.size();
			
			//for a pattern (1,2,4), we must go to the last but one node (node 2) and insert in its children key 4
			node = root;
			if(patternSize>1) {
				for(j=0;j<patternSize-1;j++) {
					key = pattern.get(j);
					node = node.getChild(key);
				}
			}		
			
			//if key already present, increase occurence; otherwise insert new key with occ = 1
			node.insertKey(pattern.get(patternSize-1));
		}
	}

	public ArrayList<UUID> getPattern(UUID element, double minCredibility) {
		//search for start element amongst the children of the root node
		Node startNode = root.getChild(element);
		//if not found -> return null
		if(startNode == null)
			return null;
		
		//initializations
		ArrayList<Node> temporary = new ArrayList<Node>();
		solution = new ArrayList<Node>();
		ArrayList<UUID> pattern = null;
		startOccurence = startNode.getOccurence();
		temporary.add(startNode);
		
		//call traverse method 
		traverse(startNode,temporary,minCredibility);
		int size = solution.size();
		//if size>0 -> if there is a solution (if a pattern was found) then it is copied into the byte arraylist
		if(size>0) {
			pattern = new ArrayList<UUID>();
			for(int i=0;i<size;i++)
				pattern.add(solution.get(i).getKey());
		}
		if(pattern.size()>1)
			return pattern;
		return null;
	}
	
	/*
	 * This method traverses a tree and searches for all the patterns that have a credibility
	 * that exceeds minCredibility. The patterns are built into the "temp". When no more nodes
	 * can be added to "temp", we check if the size of "temp" is greater than that of the solution.
	 * If so, "temp" becomes the new solution. If their sizes are equal, then we check whether the
	 * probability of temp is greater than that of the solution. If so, "temp" becomes the new
	 * solution. In the end, the solution will be the longest pattern having a probability larger
	 * than minCredibility. If there are several patterns of the same length, the solution will be
	 * the one with the highest probability.
	 */
	private void traverse(Node node, ArrayList<Node> temp, double minCredibility) {
		ArrayList<Node> children = node.getChildren();	
		boolean checkSolution = false;
		//if children size is 0, we have reached a leaf and we need to check if
		//"temp" is a good solution
		if(children.size()==0) {
			checkSolution = true;
		}
		else {
			int occurence;
			Node child;
			checkSolution = true;
			//check if any of the children nodes can be inserted into "temp"; if no
			//child can be inserted, then we must check to see if "temp" is a good solution
			for(int i=0;i<children.size();i++) {
				child = children.get(i);
				occurence = child.getOccurence();
				//if probability of child node is greater than minCredibility
				if((double)occurence/startOccurence >= minCredibility) {
					temp.add(child);
					checkSolution = false;
					traverse(child,temp,minCredibility);
					temp.remove(temp.size()-1);
				}
			}
		}
		if(checkSolution==true) {
			int tempSize = temp.size();
			int solutionSize = solution.size();
			int occ = 0;
			boolean ok = false;
			//if the length of "temp" is larger than solution, "temp" becomes the new solution
			if(tempSize > solutionSize) {
				occ = temp.get(tempSize-1).getOccurence();
				ok = true;
			}
			//if their sizes are equal, but "temp" has greater probability, "temp" becomes the new solution
			else if(tempSize == solutionSize) {
				occ = temp.get(tempSize-1).getOccurence();
				double prob = (double)occ/startOccurence;
				if(prob>patternProbability)
					ok = true;
			}
			if(ok) {
				solution = new ArrayList<Node>();
				patternProbability = (double)occ/startOccurence;
				for(int i=0;i<temp.size();i++)
					solution.add(temp.get(i));
			}
		}
	}
}
