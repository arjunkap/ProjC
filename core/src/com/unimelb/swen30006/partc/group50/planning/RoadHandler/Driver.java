package com.unimelb.swen30006.partc.group50.planning.RoadHandler;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.group50.planning.Planners.Route;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.group50.planning.vectormap.Node;

public class Driver {
	private enum Intent{
		NORTH,WEST,SOUTH,EAST,STOP
	}

	private enum State{
		NORMAL,CARBLOCK,UTURN,REDLIGHT
	}

	private enum carBlockState{
		AVOIDCAR,STOP,FOLLOWCAR
	}

	private Intent currIntent=null,nextIntent;
	private Car c;
	private Node currDest=null, nextDest;
	private HashMap<Intent,Float> holyAngels=new HashMap<Driver.Intent, Float>();
	private float currAngle;
	private final float turningVel =20f;
	private Route route=null;
	private final float TURN_RATE=75f;
	private State state;


	public Driver(Car c){
		this.c=c;
		this.holyAngels.put(Intent.NORTH,90f);
		this.holyAngels.put(Intent.SOUTH,270f);
		this.holyAngels.put(Intent.WEST,180f);
		this.holyAngels.put(Intent.EAST,0f);
		this.currAngle=0f;
		this.state = State.NORMAL;

	}

	public void drive(Route r,Stack<PerceptionResponse> cars, PerceptionResponse tl, PerceptionResponse rm, PerceptionResponse lm,float delta){	
		if(r.isNew()){
			this.route = r;
			updateDest();
			currAngle=c.getVelocity().angle();
			if((currAngle+150)%360<holyAngels.get(currIntent) || (currAngle+210)%360>holyAngels.get(currIntent)) state=State.UTURN;
		}
		if(tl==null)state=(state==State.UTURN)? state:State.NORMAL;
		else{
			Object tlState = tl.information.get("State");
			if(tlState == "Red") state = State.REDLIGHT;
			else state = State.NORMAL;
		}
		if (cars!=null){
			if (cars.peek().timeToCollision < tl.timeToCollision){
				state = State.CARBLOCK;		
			}



		}

		currAngle=c.getVelocity().angle();
		switch(state){
		case CARBLOCK:
			
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
		default:
			break;

		}





	}

	private void uturn(float delta){

		Vector2 dummy = new Vector2((float)(currDest.getX()-c.getPosition().x),(float)(currDest.getY()-c.getPosition().y));
		switch(currIntent){
		case EAST:
			if(c.getVelocity().x*dummy.x <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if((currAngle+180)%360>(holyAngels.get(currIntent)+180)%360){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					state = State.NORMAL;
				}
			}
			break;
		case WEST:
			if(c.getVelocity().x*dummy.x <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if(currAngle>holyAngels.get(currIntent)){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					state = State.NORMAL;
				}
			}
			break;
		case NORTH:
		case SOUTH:
			if(c.getVelocity().y*dummy.y <= 0){
				maintainVelocity(15);
				c.turn(-delta*150f);
			}else{
				if(currAngle>holyAngels.get(currIntent)){
					maintainVelocity(15);
					c.turn(-150f*delta);
				}
				else{
					state = State.NORMAL;
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
