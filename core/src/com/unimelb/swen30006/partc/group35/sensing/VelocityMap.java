package com.unimelb.swen30006.partc.group35.sensing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class VelocityMap {

	private World world;
	private WorldObject worldObject;
	private Vector2[] relativeVelocity;
	private Vector2[] objectVelocity;
	private Vector2 trackingVelocity;
	private Point2D.Double trackingObject;
	private static Point2D.Double[] ObjectOldPosition;
	private Point2D.Double[] ObjectNewPosition;
	private Point2D.Double Newpos;
	private static Point2D.Double Oldpos;
	private WorldObject[] ObjectList;
	private WorldObject[][] ObjectMap;
	private Vector2[][] velocityMap;
	private int visibility;
	private Rectangle2D.Double mapBoundary;
	private float delta;
	private static boolean firstTime = true;

	public VelocityMap(World world, WorldObject worldObject, float delta, Double mapBoundary,int visibility) {
		// TODO Auto-generated constructor stub
		this.world = world;
		this.worldObject = worldObject;
		this.trackingObject = worldObject.getPosition();
		this.ObjectList = this.world.objectsAtPoint(trackingObject);
		this.visibility = visibility;
		this.ObjectMap = new WorldObject[2*this.visibility+1][2*this.visibility+1];
		this.mapBoundary = mapBoundary;
		this.delta = delta;
	}



	private void velocityMapInitialization(){
		velocityMap = new Vector2[2*this.visibility+1][(int) (2*this.visibility+1)];
		for(int i = 0;i<2*this.visibility+1;i++){
			for(int j = 0; j <2*this.visibility+1;j++){
				velocityMap[i][j] = new Vector2();
				velocityMap[i][j].x = 0;
				velocityMap[i][j].y = 0;
			}
		}
	}
	private void transfer1Dto2D(WorldObject[] ObjectList){
		int indexX = 0;
		int indexY = 0;
		for(WorldObject w: ObjectList){
			for(double i = w.boundary.getMinY(); i <= w.boundary.getMaxY();i++){
				for(double j = w.boundary.getMinX(); j <=w.boundary.getMaxX();j++){
					if((i<=mapBoundary.getMaxY())&&(i>=mapBoundary.getMinY())){
						indexY = (int) Math.round(i-this.worldObject.getPosition().getY()+this.visibility);
					}
					if((j <= mapBoundary.getMaxX()) && (j >=mapBoundary.getMinX())){
						indexX = (int)Math.round(j-this.worldObject.getPosition().getX()+this.visibility);
					}
          /*
          System.out.println(indexX);
          System.out.println(indexY);
          System.out.println(ObjectMap.length);
          System.out.println(ObjectMap[0].length);
          */
					ObjectMap[indexX][indexY] = w;
				}
			}
		}
	}
	
	public Vector2[][] getVelocityMap(){
		
		this.velocityMapInitialization();
		if(ObjectList == null){
			return this.velocityMap;
		}else{
			this.transfer1Dto2D(ObjectList);
			if(firstTime){
				this.ObjectOldPosition = new Point2D.Double[this.ObjectList.length];
				this.Oldpos = this.trackingObject;
				for(int i = 0; i <ObjectOldPosition.length;i++){
					this.ObjectOldPosition[i] = this.ObjectList[i].getPosition();
				}
				firstTime = false;
				return this.velocityMap;
			}else{
				this.ObjectNewPosition = new Point2D.Double[this.ObjectList.length];
				this.Newpos = this.trackingObject;
				this.trackingVelocity.x = (float) ((this.Newpos.getX()-this.Oldpos.getX())/this.delta);
				this.trackingVelocity.y = (float) ((this.Newpos.getY()-this.Oldpos.getY())/delta);
				for(int i = 0; i<this.ObjectList.length;i++){
					this.ObjectNewPosition[i] = ObjectList[i].getPosition();
					this.objectVelocity[i].x = (float) ((this.ObjectNewPosition[i].getX()-this.ObjectOldPosition[i].getX())/delta);
					this.objectVelocity[i].y = (float) ((this.ObjectNewPosition[i].getY()-this.ObjectOldPosition[i].getY())/delta);
					for(int a = 0; a<(int) (mapBoundary.getMaxY()-mapBoundary.getMinY()+1);a++){
						for(int b = 0;b <(int) (mapBoundary.getMaxX()-mapBoundary.getMinX()+1);b++){
							if(this.ObjectList[i]==this.ObjectMap[a][b]){
								this.velocityMap[a][b].x = this.objectVelocity[i].x-this.trackingVelocity.x;
								this.velocityMap[a][b].y = this.objectVelocity[i].y-this.trackingVelocity.y;
							}
						}
					}
				}
				for(int i = 0; i< this.ObjectNewPosition.length;i++){
					this.ObjectOldPosition[i] = this.ObjectNewPosition[i];
				}
				this.Oldpos = this.Newpos;
				return this.velocityMap;
			}
		}
	}
	

	public WorldObject[] getObjectList() {
		return this.ObjectList;
	}
}
