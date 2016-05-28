package com.unimelb.swen30006.partc.group50.planning.Planners;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Stack;

import com.unimelb.swen30006.partc.group50.planning.RoadHandler.Driver;
import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

public class Planner implements IPlanning {

	private Car c;
	private Road[] roads;
	private RouteGenerator generator;
	private Route route;
	private Driver driver;
	private Stack<PerceptionResponse> cars;
	private PerceptionResponse tl;
	private PerceptionResponse rm;
	private PerceptionResponse lm;
	private final float AVG_VEL = 40f; 
	
	public Planner(Car c, Road[] r,Point2D.Double p){
		this.c=c;
		this.roads=r;
		this.generator = new RouteGenerator(r);
		this.driver = new Driver(c);
		planRoute(p);
		
		
	}
	
	@Override
	public boolean planRoute(Double destination) {
		this.route = generator.generateRoute(destination, c.getPosition());
//		while(route.peek()!=null){
//			System.out.println(route.getNext().getX());
//		}
		return (this.route==null)?false:true;
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {	
		driver.drive(route, cars, tl, rm, lm,delta);
//		this.c.accelerate();// TODO Auto-generated method stub
//		if(timer>=2.5){
//			this.c.turn(-150f*delta);
//		}
//		timer+=delta;
		this.c.update(delta);
		
	}

	@Override
	public float eta() {
		float eta = 0;
		if(this.route.peek()!=null){
			eta = (this.route.getDistance() + (float)this.c.getPosition().distance(this.route.peek().getPos()))/AVG_VEL; 
		} else
			eta = (float)this.c.getPosition().distance(this.route.finalDest)/AVG_VEL;
		return eta;
	}

}
