package com.unimelb.swen30006.partc.group35.sensing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class SpaceMap {
	private WorldObject[] ObjectList;
	private boolean[][] spaceMap;
	private WorldObject[][] ObjectMap;
	private WorldObject worldObject;
	private World world;
	private Point2D.Double trackingObject;
	private int visibility;
	private Rectangle2D.Double mapBoundary;
	
	public SpaceMap(World world, WorldObject worldObject,int visibility, Double mapBoundary) {
		// TODO Auto-generated constructor stub
		this.world = world;
		this.worldObject = worldObject;
		this.trackingObject = worldObject.getPosition();
		this.visibility = visibility;
		this.ObjectList = world.objectsAtPoint(worldObject.getPosition());
		this.visibility = visibility;
		this.mapBoundary = mapBoundary;
		this.ObjectMap = new WorldObject[2*this.visibility+1][2*this.visibility+1];
	}

	public void	transfer1Dto2D(WorldObject[] ObjectList){
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
					ObjectMap[indexX][indexY] = w;
				}
			}
		}
	}
	
	private void spaceMapInitialization(){
		spaceMap = new boolean[2*this.visibility+1][2*this.visibility+1];
		for(int i = 0;i<mapBoundary.getMaxY()-mapBoundary.getMinY()+1;i++){
			for(int j = 0; j <(int) (mapBoundary.getMaxX()-mapBoundary.getMinX()+1);j++){
				spaceMap[i][j] = false;
			}
		}
	}
	
	public boolean[][] checkOccupation(){
		for (int i = 0; i < (int) (mapBoundary.getMaxY()-mapBoundary.getMinY()+1); i++) {
			for (int j = 0; j <(int) (mapBoundary.getMaxX()-mapBoundary.getMinX()+1); j++) {
				if(this.ObjectMap[i][j]!=null){
					spaceMap[i][j] = true;
				}else{
					spaceMap[i][j] = false;
				}
			}
		}
		return spaceMap;
	}

	public boolean[][] getSpaceMap(){
		this.spaceMapInitialization();
		this.transfer1Dto2D(ObjectList);
		return this.checkOccupation();
	}
	
}
