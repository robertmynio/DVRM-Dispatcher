package vdrm.pred.miner;

import java.util.ArrayList;

public class Node {
	private Node parent;
	ArrayList<Node> children;
	private int key;
	private int occurence;
	
	//Constructors
	public Node() { //constructor for root 
		parent = null;
		children = new ArrayList<Node>();
		this.key = 0;
		this.occurence = 0;
	}
	public Node(int key) {
		parent = null;
		children = new ArrayList<Node>();
		this.key = key;
		occurence = 1;
	}
	
	public Node(int key, Node parent) {
		this(key);
		this.parent = parent;
	}
	
	//Setters and Getters
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getOccurence() {
		return occurence;
	}
	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	public void incrementOccurence() {
		this.occurence++;
	}
	public ArrayList<Node> getChildren() {
		return children;
	}
	public void addChild(Node child) {
		children.add(child);
		child.setParent(this);
		child.setOccurence(1);
	}
	public void addChild(int key) {
		children.add(new Node(key,this));
	}
	public Node getChild(int key) {
		Node node= null;
		int size = children.size();
		for(int i=0;i<size;i++) {
			if(children.get(i).getKey() == key)
				node = children.get(i);
		}
		return node;
	}
	//use this method to add child to a parent; if child exists, occurence is incremented, otherwise it is added with occurence=1
	public void insertKey(int key) {
		Node node = this.getChild(key);
		if(node!=null)
			node.incrementOccurence();
		else this.addChild(key);
	}
}
