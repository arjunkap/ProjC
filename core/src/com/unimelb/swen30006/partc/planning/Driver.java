package com.unimelb.swen30006.partc.RoadHandler;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.Planners.Route;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.vectormap.Node;

public class Driver {
	private enum Intent{
		NORTH,WEST,SOUTH,EAST,STOP
	}

	private Intent currIntent=null,nextIntent;
	private Car c;
	private Node currDest=null, nextDest;
	private HashMap<Intent,Float> holyAngels=new HashMap<Driver.Intent, Float>();
	private float currAngle;
	private final float turningVel =20f;
	private Route route=null;
	private final float TURN_RATE=75f;


	public Driver(Car c){
		this.c=c;
		this.holyAngels.put(Intent.NORTH,90f);
		this.holyAngels.put(Intent.SOUTH,270f);
		this.holyAngels.put(Intent.WEST,180f);
		this.holyAngels.put(Intent.EAST,0f);
		this.currAngle=0f;

	}

	public void drive(Route r,Stack<PerceptionResponse> cars, PerceptionResponse tl, PerceptionResponse rm, PerceptionResponse lm,float delta){	
		if(r.isNew()){
			this.route = r;
			updateDest();
		}
		currAngle=c.getVelocity().angle();

		if(currIntent==Intent.EAST || currIntent==Intent.WEST){
			if(Math.abs(c.getPosition().x-currDest.getX())<50){
				if(currIntent == nextIntent){
					c.accelerate();
					updateDest();
				}
				if(nextIntent==Intent.STOP){
					
					if(Math.abs(c.getPosition().x-currDest.getX())<3) c.brake();
					else maintainVelocity(10);
				}
				try{
					if(holyAngels.get(nextIntent)-holyAngels.get(currIntent)>0) leftTurn(delta);
					else rightTurn(delta);
				}catch(NullPointerException e){
				}
			}else maintainAngle(delta);
		}else if(currIntent==Intent.NORTH || currIntent==Intent.SOUTH){
			if(Math.abs(c.getPosition().y-currDest.getY())<50){
				if(currIntent == nextIntent){
					c.accelerate();
					updateDest();
				}
				if(nextIntent==Intent.STOP){
					maintainVelocity(10);
					if(Math.abs(c.getPosition().y-currDest.getY())<5) c.brake();
				}
				try{
					if(holyAngels.get(nextIntent)-holyAngels.get(currIntent)>0) leftTurn(delta);
					else rightTurn(delta);
				}catch(NullPointerException e){
				}
			}else maintainAngle(delta);
		}


		//		checkIntent(r);
		//		
		//		if(cars.size()==0 && tl==null){
		//			watchTheRoad(rm,lm);
		//		}else if(cars.size() > 0 && tl!=null){
		//			barrelRoll(cars,tl,rm,lm);
		//		}else if(cars.size()>0){
		//			avoid(cars,rm,lm);
		//		}else if(tl!=null){
		//			dontTextAndDrive(tl.information);
		//		}
	}

	private void rightTurn(float delta) {
		maintainVelocity(turningVel);
		if(currIntent==Intent.EAST || currIntent==Intent.WEST){
			if(Math.abs(c.getPosition().x-currDest.getX())<10){
				turn(delta);
			}
		}else{
			if(Math.abs(c.getPosition().y-currDest.getY())<10){
				turn(delta);
			}
		}

	}

	private void leftTurn(float delta) {
		maintainVelocity(turningVel);
		if(currIntent==Intent.EAST || currIntent==Intent.WEST){
			if(Math.abs(c.getPosition().x-currDest.getX())<20){
				turn(delta);
			}
		}else{
			if(Math.abs(c.getPosition().y-currDest.getY())<20){
				turn(delta);
			}
		}

	}


	private void updateDest(){
		currDest=route.getNext();
		nextDest=route.peek();
		if(currIntent==null){
			float dumAngle = new Vector2((float)(currDest.getX()-c.getPosition().x), (float)(currDest.getX()-c.getPosition().y)).angle();
			if(dumAngle>75 && dumAngle<105) currIntent=Intent.NORTH;
			else if(dumAngle>165 && dumAngle<195) currIntent=Intent.WEST;
			else if(dumAngle>255 && dumAngle<285) currIntent=Intent.SOUTH;
			else currIntent=Intent.EAST;			
		}else{
			currIntent=nextIntent;
		}
		if(nextDest!=null){
			float dumAngle = new Vector2((float)(nextDest.getX()-currDest.getX()), (float)(nextDest.getY()-currDest.getY())).angle();
			if(dumAngle>75 && dumAngle<105) nextIntent=Intent.NORTH;
			else if(dumAngle>165 && dumAngle<195) nextIntent=Intent.WEST;
			else if(dumAngle>255 && dumAngle<285) nextIntent=Intent.SOUTH;
			else nextIntent=Intent.EAST;	
		}else{
			nextIntent=Intent.STOP;
		}

	}

	private void turn(float delta) {
		float angle=holyAngels.get(nextIntent);
		if(nextIntent==Intent.EAST || currIntent==Intent.EAST){
			angle=(angle+180)%360;
			currAngle=(currAngle+180)%360;
		}
		if(currAngle>angle-5 && currAngle<angle+5){
			updateDest();
			return;
		}
		if(currAngle<=angle){
			maintainVelocity(20);
			c.turn(TURN_RATE*delta);
		}
		else{
			maintainVelocity(20);
			c.turn(-TURN_RATE*delta);
		}

	}

	private void maintainAngle(float delta) {
		float angle=holyAngels.get(currIntent);
		if(currIntent==Intent.EAST){
			angle=(angle+180)%360;
			currAngle=(currAngle+180)%360;
		}
		if(currAngle<=angle){
			c.turn(50f*delta);
		}
		else{
			c.turn(-50f*delta);
		}
		c.accelerate();

	}

	private void maintainVelocity(float vel) {
		if(Math.sqrt(Math.pow(c.getVelocity().x, 2)+Math.pow(c.getVelocity().y, 2))>vel)c.brake();
		else c.accelerate();

	}




}
