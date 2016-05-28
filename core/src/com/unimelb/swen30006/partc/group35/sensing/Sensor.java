package com.unimelb.swen30006.partc.group35.sensing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.ISensing;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Road;

public class Sensor implements ISensing {
	
	private SpaceMap spaceMap;
	private VelocityMap velocityMap;
	private ColourMap colourMap;
	private Point2D.Double pos;
	private int visibility;
	private World world;
	private WorldObject worldObject;
	private Road road;
	private Rectangle2D.Double WorldBoundary, MapBoundary;
	private double startX; 
	private double startY; 
	private double endX;
	private double endY;
	private float delta;
	
	public Sensor(World world, WorldObject worldObject){
		this.world = world;
		this.worldObject = worldObject;
		
	}
	@Override
	public boolean update(Double pos, float delta, int visibility) {
		// TODO Auto-generated method stub
		this.visibility = visibility;
		this.pos = pos;
		this.delta = delta;
		this.WorldBoundary = new Rectangle2D.Double(0,0,800,800);
		startX = pos.x-1/2*visibility;
		startY = pos.y-1/2*visibility;
		endX = pos.x+1/2*visibility;
		endY = pos.y+1/2*visibility;
		if(startX<WorldBoundary.getMinX()){
			startX = WorldBoundary.getMinX();
		}
		if(startY<WorldBoundary.getMinY()){
			startY =WorldBoundary.getMinY();
		}
		if(endX>WorldBoundary.getMaxX()){
			endX = WorldBoundary.getMaxX();
		}
		if(endY>WorldBoundary.getMaxY()){
			endY = WorldBoundary.getMaxY();
		}
		this.MapBoundary = new Rectangle2D.Double(startX,startY,endX,endY);
		return false;
	}

	@Override
	public Vector2[][] getVelocityMap() {
		// TODO Auto-generated method stub
		velocityMap = new VelocityMap(this.world, this.worldObject, delta,MapBoundary,visibility);
		return velocityMap.getVelocityMap();
	}

	@Override
	public boolean[][] getSpaceMap() {
		// TODO Auto-generated method stub
		spaceMap = new SpaceMap(this.world, this.worldObject,visibility, MapBoundary);
		return spaceMap.getSpaceMap();
	}

	@Override
	public Color[][] getColourMap() {
		// TODO Auto-generated method stub
		colourMap = new ColourMap(this.world, this.worldObject,MapBoundary,visibility);
		return colourMap.getColourMap();
	}

}
