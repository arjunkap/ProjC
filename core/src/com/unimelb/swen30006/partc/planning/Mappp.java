package com.unimelb.swen30006.partc.vectormap;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Mappp {
	private final int WIDTH=15;
	private ArrayList<Node> intersections;
	private MapReaderV2 reader = new MapReaderV2("test_course.xml");
	
	public Mappp(){
		this.intersections = reader.buildIt();
	}
	
	public Node whichIsThis(Point2D.Double dd){
		double X = dd.getX();
		double Y = dd.getY();
		
		for(Node n: intersections){
			if((X<=n.getX()+WIDTH && X>=n.getX()-WIDTH) && (Y<=n.getY()+WIDTH && Y>=n.getY()-WIDTH)) return n;
		}
		return null;
	}
	
	public Node whichIsThis(double X, double Y){	
		for(Node n: intersections){
			if(X==n.getX() && Y==n.getY()) return n;
		}
		return null;
	}

	
}
