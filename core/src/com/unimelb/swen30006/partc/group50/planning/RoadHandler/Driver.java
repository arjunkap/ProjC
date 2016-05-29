package com.unimelb.swen30006.partc.group50.planning.RoadHandler;


import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.PriorityQueue;


import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.group50.planning.Planners.Route;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.group50.planning.vectormap.Node;
import com.unimelb.swen30006.partc.roads.Road;

public class Driver {
	private enum Intent{ // to know the direction the car currently has and next direction it intends to move
		NORTH,WEST,SOUTH,EAST,STOP
	}

	private enum ActionState{ // states of the car. Could have make interfaces for them, but we stick with the submitted design
		NORMAL,CARHANDLE,UTURN,REDLIGHT,AVOID 
	}
	
	
	// AVOID STATE, first intended to use if the perception can get lane marking
	// the algorithm should be if there is an obstacle in current lane
	// and there is >1 lane and check all possible lanes
	// maneuver to the first lane found that is not occupied
	// otherwise just follow the car (if the obstacle is car) and stop if the obstacle is a static one

	
	private Intent currIntent=null,nextIntent;
	private Car c;
	private Node currDest=null, nextDest;
	private HashMap<Intent,Float> intendedAngles=new HashMap<Driver.Intent, Float>();
	private float currAngle;
	private final float turningVel =20f;
	private Route route=null;
	private final float TURN_RATE=75f;
	private ActionState actionState;

	private final float MAX_ROAD_DISTANCE = 50f;

	public Driver(Car c){
		this.c=c;
		this.intendedAngles.put(Intent.NORTH,90f);
		this.intendedAngles.put(Intent.SOUTH,270f);
		this.intendedAngles.put(Intent.WEST,180f);
		this.intendedAngles.put(Intent.EAST,0f);
		this.currAngle=0f;
		this.actionState = ActionState.NORMAL;
	}

	public void drive(Road[] roads, Route r,PriorityQueue<PerceptionResponse> cars, PerceptionResponse tl, PerceptionResponse rm, PerceptionResponse lm,float delta){	
		if(r.isNew()){
			this.route = r;
			updateDest();
			currAngle=c.getVelocity().angle();
			if((currAngle+150)%360<intendedAngles.get(currIntent) || (currAngle+210)%360>intendedAngles.get(currIntent)) actionState=ActionState.UTURN;
		}
		
		if(tl==null)actionState=(actionState==ActionState.UTURN)? actionState:ActionState.NORMAL;
		else{
			Object tlState = tl.information.get("State");
			if(tlState == "Red") actionState = ActionState.REDLIGHT;
			else actionState = ActionState.NORMAL;
		}
		
		if (cars!=null){
			if (cars.peek().distance < tl.distance){ // check whether there is a car between the current car and upcoming traffic light
				if (closestRoad(c.getPosition(),roads).getNumLanes()<=1){
					actionState = ActionState.CARHANDLE;	
				} else{
					actionState = ActionState.AVOID;
				}				
			}
		}
		currAngle=c.getVelocity().angle();
		switch(actionState){
		case CARHANDLE:
			maintainDistance(cars.peek().velocity);	
			break;
		case NORMAL:
			driveNormal(delta);
			break;
		case UTURN:
			uturn(delta);
			break;
		case REDLIGHT:
			if(tl.distance<40) c.brake();
			else driveNormal(delta);
			break;
		case AVOID:
			break;
		default:
			break;
		}
	}
	
	private void maintainDistance(Vector2 relativeVelocity){
		if(Math.sqrt(Math.pow(relativeVelocity.x, 2)+Math.pow(relativeVelocity.y, 2))<=0)c.brake();
		else c.accelerate();
	}

	private void uturn(float delta){

		Vector2 dummy = new Vector2((float)(currDest.getX()-c.getPosition().x),(float)(currDest.getY()-c.getPosition().y));
		switch(currIntent){
		case EAST:
			if(c.getVelocity().x*dummy.x <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if((currAngle+180)%360>(intendedAngles.get(currIntent)+180)%360){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					actionState = ActionState.NORMAL;
				}
			}
			break;
			
		case WEST:
			if(c.getVelocity().x*dummy.x <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if(currAngle>intendedAngles.get(currIntent)){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					actionState = ActionState.NORMAL;
				}
			}
			break;
			
		case NORTH:
		case SOUTH:
			if(c.getVelocity().y*dummy.y <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if(currAngle>intendedAngles.get(currIntent)){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					actionState = ActionState.NORMAL;
				}
			}
			break;
		case STOP:
			break;

		}
	}

	private void driveNormal(float delta){
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
					if(intendedAngles.get(nextIntent)-intendedAngles.get(currIntent)>0) leftTurn(delta);
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
					if(intendedAngles.get(nextIntent)-intendedAngles.get(currIntent)>0) leftTurn(delta);
					else rightTurn(delta);
				}catch(NullPointerException e){
				}
			}else maintainAngle(delta);
		}
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
			float dumAngle = new Vector2((float)(currDest.getX()-c.getPosition().x), (float)(currDest.getY()-c.getPosition().y)).angle();
			if(dumAngle>45 && dumAngle<135) currIntent=Intent.NORTH;
			else if(dumAngle>135 && dumAngle<235) currIntent=Intent.WEST;
			else if(dumAngle>235 && dumAngle<315) currIntent=Intent.SOUTH;
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
		float angle=intendedAngles.get(nextIntent);
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
		float angle=intendedAngles.get(currIntent);
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

	private Road closestRoad(Point2D.Double pos, Road[] roadArray){
		float minDist = Float.MAX_VALUE;
		Road minRoad = null;
		for(Road r: roadArray){
			float tmpDist = r.minDistanceTo(pos);
			if(tmpDist < minDist){
				minDist = tmpDist;
				minRoad = r;
			}
		}
		return (minDist < MAX_ROAD_DISTANCE) ? minRoad : null;
	}


}
