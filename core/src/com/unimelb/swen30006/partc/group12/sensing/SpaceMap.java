package com.unimelb.swen30006.partc.group12.sensing;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;


public class SpaceMap {

	private boolean[][] spaceMap;
	private int visibility;
	private World world=new World();
	private WorldObject[] worldObjects;
	Point2D.Double pos;
	private Rectangle2D.Double mapBoundary;

	
	public SpaceMap(World world,int visibility,Point2D.Double pos,Rectangle2D.Double mapBoundary){
		this.world=world;
		this.visibility=visibility;
		this.pos=pos;
		this.mapBoundary=mapBoundary;
	}
	private void initSpaceMap(){
		this.spaceMap=new boolean[(int) Math.ceil(mapBoundary.height)][(int) Math.ceil(mapBoundary.width)];
		for(int i=0;i<(int) Math.ceil(mapBoundary.height);i++){
			for(int j=0;j<(int) Math.ceil(mapBoundary.width);j++){
				//not occupied;
				spaceMap[i][j]=false; 
			}
		}
	}
	
	private void updateSpaceMap(WorldObject object){
		
		int xStartIndex,xEndIndex,yStartIndex,yEndIndex;
		
		Vector2[] vectors=Calculator.getStartEndIndex(object.boundary,mapBoundary,visibility);
		xStartIndex=(int) vectors[0].x;
		yStartIndex=(int) vectors[0].y;
		xEndIndex=(int) vectors[1].x;
		yEndIndex=(int) vectors[1].y;
		
		for(int x=xStartIndex;x<xEndIndex;x++){
			for(int y=yStartIndex;y<yEndIndex;y++){
				spaceMap[y][x]=true;
			}
		}
	}
	
	public boolean[][] generateSpaceMap(){
	
		initSpaceMap();
		worldObjects=Calculator.objects(mapBoundary, world);
		for(WorldObject object:worldObjects){
			updateSpaceMap(object);
		}
		return spaceMap;
	}
}
