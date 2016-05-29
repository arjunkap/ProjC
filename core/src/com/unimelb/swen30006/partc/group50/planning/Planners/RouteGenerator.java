package com.unimelb.swen30006.partc.group50.planning.Planners;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.group50.planning.vectormap.Mappp;
import com.unimelb.swen30006.partc.group50.planning.vectormap.Node;

public class RouteGenerator {

	private Road[] roads;
	private final float MAX_ROAD_DISTANCE = 50f;
	private final float SENSITIVITY = 8f;
	private Mappp m;

	public RouteGenerator(Road[] r){
		this.roads = r;
		this.m = new Mappp();
	}

	public Route generateRoute(Point2D.Double dest, Point2D.Double curr){
		Road destRoad = closestRoad(dest);
		Road currRoad = closestRoad(curr);
		ArrayList<Node> endPt = new ArrayList<Node>();
		if(m.whichIsThis(currRoad.getEndPos())!=null)
			endPt.add(m.whichIsThis(currRoad.getEndPos()));
		if(m.whichIsThis(currRoad.getStartPos())!=null)
			endPt.add(m.whichIsThis(currRoad.getStartPos()));

		if(destRoad == null) return null;
		dest = getNewDest(dest, destRoad);
		Route r = new Route(dest);
		ArrayList<Node> next = new ArrayList<Node>();
		if(m.whichIsThis(destRoad.getStartPos())!=null)
			next.add(m.whichIsThis(destRoad.getStartPos()));
		if(m.whichIsThis(destRoad.getEndPos())!=null)
			next.add(m.whichIsThis(destRoad.getEndPos()));

		while(!endPt.contains(r.peek())){
			Node tmp = ScoreComparator(next,curr);
			if(r.contains(tmp)) return null;
			r.queueItIn(tmp);
			next = tmp.getChildren();
		}
		
		r.complete();

		return r;
	}

	private Node ScoreComparator(ArrayList<Node> next,Point2D.Double curr) {
		float minD= Float.MAX_VALUE;
		float tmpD = 0;
		Node tmp = null;
		for(Node n:next){
			tmpD = getDistance(curr, n.getX(),n.getY());
			if(minD>tmpD){
				tmp = n;
				minD = tmpD;
			}
		}
		return tmp;
	}

	private float getDistance(Point2D.Double p1,double d, double e){
		return (float) Math.sqrt(Math.pow(p1.getX()-d, 2)+Math.pow(p1.getY()-e,2));
	}

	private Point2D.Double getNewDest(Point2D.Double d, Road r){
		double X = d.getX();
		double Y = d.getY();
		Point2D.Double tmp;
		for(double i=0;i<=50;i++){
			for(double k=0;k<2*Math.PI;k+=Math.PI/SENSITIVITY){
				tmp = new Point2D.Double(X+i*Math.cos(k),Y+i*Math.sin(k));
				if(r.containsPoint(tmp)) return tmp;
			}
		}
		System.err.println("GAAAAH! DEBUG!");
		System.exit(1);
		return null;
	}

	private Road closestRoad(Point2D.Double pos){
		float minDist = Float.MAX_VALUE;
		Road minRoad = null;
		for(Road r: this.roads){
			float tmpDist = r.minDistanceTo(pos);
			if(tmpDist < minDist){
				minDist = tmpDist;
				minRoad = r;
			}
		}
		return (minDist < MAX_ROAD_DISTANCE) ? minRoad : null;
	}
}
