package com.unimelb.swen30006.partc.group50.planning.Planners;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.PriorityQueue;

import com.unimelb.swen30006.partc.group50.planning.RoadHandler.Driver;
import com.unimelb.swen30006.partc.group50.planning.RoadHandler.PriorityRanker;
import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

public class Planner implements IPlanning {

	private Car c;
	private Road[] roads;
	private RouteGenerator generator;
	private Route currRoute;
	private Driver driver;
	private PriorityQueue<PerceptionResponse> cars;
	private PerceptionResponse tl;
	private PerceptionResponse rm;
	private PerceptionResponse lm;
	private final float AVG_VEL = 40f; 
	private PriorityRanker priorityRanker;
	
	public Planner(Car c, Road[] r,Point2D.Double p){
		this.c=c;
		this.roads=r;
		this.generator = new RouteGenerator(r);
		this.driver = new Driver(c);
		System.out.println(planRoute(p));	
	}
	
	@Override
	public boolean planRoute(Double destination) {
		this.currRoute = generator.generateRoute(destination, c.getPosition());
		return (this.currRoute==null)?false:true;
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {	
		priorityRanker.prioritise(results);
		driver.drive(roads,currRoute, priorityRanker.getRankedCars(), priorityRanker.getTrafficLight(), priorityRanker.getRoadMarking(), priorityRanker.getLaneMarking(),delta);	
		//driver.drive(roads,currRoute, cars,tl , rm, lm,delta);	
		
		//driver.drive(roads,currRoute, cars, tl, rm, lm,delta);	
		
		this.c.update(delta);
		
	}

	@Override
	public float eta() {
		float eta = 0;
		if(this.currRoute.peek()!=null){
			eta = (this.currRoute.getDistance() + (float)this.c.getPosition().distance(this.currRoute.peek().getPos()))/AVG_VEL; 
		} else
			eta = (float)this.c.getPosition().distance(this.currRoute.finalDest)/AVG_VEL;
		return eta;
	}

}
