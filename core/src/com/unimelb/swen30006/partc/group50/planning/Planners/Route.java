package com.unimelb.swen30006.partc.group50.planning.Planners;

import java.awt.geom.Point2D;
import java.util.EmptyStackException;
import java.util.Stack;

import com.unimelb.swen30006.partc.group50.planning.vectormap.Node;

public class Route {
	private Stack<Node> thisOne;
	private boolean complete;
	private boolean isNew = true;
	
	public Point2D.Double finalDest;

	public Route(Point2D.Double dest){
		this.thisOne = new Stack<Node>();
		thisOne.push(new Node(dest.getX(),dest.getY()));
		complete = false;
		this.finalDest = dest;
	}

	public Node getNext(){
		if(!complete){
			System.err.println("Not done yet");
			return null;
		}
		isNew=false;
		return thisOne.pop();
	}

	public boolean areWeThereYet(){
		return thisOne.isEmpty();
	}

	public void complete(){
		complete=true;
	}

	public void queueItIn(Node n){
		thisOne.push(n);
	}

	public Node peek(){
		try{
			return thisOne.peek();
		}catch(EmptyStackException e){
			return null;
		}
	}

	public boolean contains(Node tmp) {
		if(thisOne.search(tmp)!=-1) return true;
		return false;
	}

	public float getDistance(){
		Stack<Node> tempStack = new Stack<Node>();
		tempStack.addAll(thisOne);
		float tempDistance = 0 ;
		while (tempStack.size()>1){
			tempDistance+=tempStack.pop().getPos().distance(tempStack.peek().getPos());
		}
		
		return tempDistance;
	}

	public boolean isNew(){
		return isNew;
	}
	

}
