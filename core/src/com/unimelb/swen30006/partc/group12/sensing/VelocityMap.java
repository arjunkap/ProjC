package com.unimelb.swen30006.partc.group12.sensing;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;


public class VelocityMap {

	private boolean firstUpdateFlag=true;
	private Vector2[][] velocityMap;
	private WorldObject[] worldObjects;
	private Point2D.Double carOldPosition;
	private Point2D.Double[] oldPos;
	private World world;
	private int visibility;
	private Rectangle2D.Double mapBoundary;
	Point2D.Double pos;
	float delta;
	
	public VelocityMap(World world,int visibility,Rectangle2D.Double mapBoundary,Point2D.Double pos,float delta){
		this.world=world;
		this.visibility=visibility;
		this.mapBoundary=mapBoundary;
		this.pos=pos;
		this.delta=delta;
	}
	private void updateVelocityMap(WorldObject object,Vector2 velocity){
		int xStartIndex,xEndIndex,yStartIndex,yEndIndex;
		
		Vector2[] vectors=Calculator.getStartEndIndex(object.boundary, mapBoundary, visibility);
		xStartIndex=(int) vectors[0].x;
		yStartIndex=(int) vectors[0].y;
		xEndIndex=(int) vectors[1].x;
		yEndIndex=(int) vectors[1].y;
		
		for(int x=xStartIndex;x<xEndIndex;x++){
			for(int y=yStartIndex;y<yEndIndex;y++){
				velocityMap[y][x]=velocity;
			}
		}
	}
	
	private void initVelocityMap(){
		this.velocityMap=new Vector2[visibility*2][visibility*2];
		for(int x=0;x<(visibility*2);x++){
			for(int y=0;y<(visibility*2);y++){
				velocityMap[y][x]=new Vector2();
				velocityMap[y][x].x=0;
				velocityMap[y][x].y=0;
			}
		}
	}
	
	private void updateVariables(){
		this.worldObjects=Calculator.objects(mapBoundary,world);
		this.oldPos=new Point2D.Double[worldObjects.length];
		this.carOldPosition=pos;
		
		for(int i=0;i<oldPos.length;i++){
			oldPos[i]=worldObjects[i].getPosition();
		}
	}
	
	public Vector2[][] generateVelocityMap(){
		
		Vector2 carVelocity=new Vector2();
		Point2D.Double currentPos;
		Vector2 objVelocity=new Vector2();
		Vector2 velocity=new Vector2();
	
		initVelocityMap();
		if(this.firstUpdateFlag=true){
			updateVariables();
			this.firstUpdateFlag=false;
			return velocityMap;
		}
		
		carVelocity.x=(float) ((pos.x-carOldPosition.x)/delta);
		carVelocity.y=(float) ((pos.y-carOldPosition.y)/delta);
		
		for(int i=0;i<this.worldObjects.length;i++){
			currentPos=worldObjects[i].getPosition();
			objVelocity.x=(float) ((currentPos.x-oldPos[i].x)/delta);
			objVelocity.y=(float) ((currentPos.y-oldPos[i].y)/delta);
			velocity.x=objVelocity.x-carVelocity.x;
			velocity.y=objVelocity.y-carVelocity.y;
			updateVelocityMap(worldObjects[i],velocity);
		}
		
		updateVariables();
		
		return velocityMap;
		
		
	}
}
