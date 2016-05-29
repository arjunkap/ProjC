package com.unimelb.swen30006.partc.group50.planning.RoadHandler;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.PriorityQueue;


import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;

public class PriorityRanker {

	
	PriorityQueue<PerceptionResponse> cars = new PriorityQueue<PerceptionResponse>();
	PerceptionResponse trafficL, roadM, laneM;

	public void prioritise(PerceptionResponse[] pr){
		Iterator<PerceptionResponse> o = Arrays.asList(pr).iterator();
		PerceptionResponse tmp;
		ArrayList<PerceptionResponse> cars = new ArrayList<PerceptionResponse>();
		ArrayList<PerceptionResponse> TL = new ArrayList<PerceptionResponse>();
		ArrayList<PerceptionResponse> RM = new ArrayList<PerceptionResponse>();
		ArrayList<PerceptionResponse> LM = new ArrayList<PerceptionResponse>();
		
		reset();
		
		while(o.hasNext()){
			tmp = o.next();
			Classification type = tmp.objectType;
			if(isApproaching(tmp)){
				if(type==Classification.StreetLight || type==Classification.Sign || type==Classification.Building){
					o.remove();
					continue;
				}
				if(type == Classification.Car){
					cars.add(tmp);
					continue;
				}
				if(type == Classification.TrafficLight){
					TL.add(tmp);
					continue;
				}
				if(type==Classification.LaneMarking){
					LM.add(tmp);
					continue;
				}
				if(type==Classification.RoadMarking){
					RM.add(tmp);
					continue;
				}
			}else o.remove();

		}
		if(cars.size()>1) rankCar(cars); // rank based on distance to current car
		
		switch(TL.size()){
		case 0:
			trafficL = null;
			break;
		case 1:
			trafficL = TL.get(0);
			break;
		default:
			trafficL = getNearest(TL);
			break;
		}
		
		switch(LM.size()){
		case 0:
			laneM = null;
			break;
		case 1:
			laneM = TL.get(0);
			break;
		default:
			laneM = getNearest(TL);
			break;
		}
		
		switch(RM.size()){
		case 0:
			roadM = null;
			break;
		case 1:
			roadM = TL.get(0);
			break;
		default:
			roadM = getNearest(TL);
			break;
		}
	}

	private void reset() {
		this.cars.clear();
		trafficL=null;
		roadM=null;
		laneM=null;
	}

	private PerceptionResponse getNearest(ArrayList<PerceptionResponse> a){
		float minD = Float.MAX_VALUE;
		PerceptionResponse tmp=null;
		for(PerceptionResponse b:a){
			if(b.distance < minD){
				tmp=b;
			}
		}		
		return tmp;
	}
	
	private void rankCar(ArrayList<PerceptionResponse> c){
		float minD = Float.MAX_VALUE;
		PerceptionResponse tmp,min=c.get(0);;
		while(!c.isEmpty()){
			Iterator<PerceptionResponse> d=c.iterator();
			while(d.hasNext()){
				tmp = d.next();
				if(minD>tmp.distance){
					min = tmp;
				}
			}
			this.cars.add(min);
		}

	}

	private boolean isApproaching(PerceptionResponse tmp) {
		if(tmp.direction.x*tmp.velocity.x <= 0 || tmp.direction.y*tmp.velocity.y <= 0) return true;
		return false;
	}

	public PriorityQueue<PerceptionResponse> getRankedCars(){
		return this.cars;
	}
	
	public PerceptionResponse getTrafficLight(){
		return trafficL;
	}
	
	public PerceptionResponse getRoadMarking(){
		return roadM;
	}
	
	public PerceptionResponse getLaneMarking(){
		return laneM;
	}

}
