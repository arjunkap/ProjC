package com.unimelb.swen30006.partc.vectormap;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node {
	
	private double x;
	private double y;
	private double w;
	private double h;
	private ArrayList<Node> children;
	
	public Node(double X,double Y,double W, double H){
		this.x=X;
		this.y=Y;
		this.w=W;
		this.h=H;
		this.children = new ArrayList<Node>();	
	}
	
	public Node(double X,double Y){
		this.x=X;
		this.y=Y;
		this.w=0;
		this.h=0;
		this.children = null;	
	}
	
	public Node(Point2D.Double p){
		this.x=p.getX();
		this.y=p.getY();
		this.w=0;
		this.h=0;
		this.children = null;	
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	public void addChildren(Node n){
		this.children.add(n);
	}
	
	public Point2D.Double getPos(){
		return new Point2D.Double(x, y);
		
	}
}
