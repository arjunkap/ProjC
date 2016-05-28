package com.unimelb.swen30006.partc.group35.sensing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

public class ColourMap {

	private World world;
	private WorldObject worldObject;
	private Point2D.Double trackingObject;
	private WorldObject[] ObjectList;
	private WorldObject[][] ObjectMap;
	private Color[][] ColourMap;
	private int visibility;
	private Road road;
	private Rectangle2D.Double mapBoundary;
	private RoadMarking[] markers;
	
	public ColourMap(World world, WorldObject worldObject, Double mapBoundary,int visibility){
		this.world = world;
		this.worldObject = worldObject;
		this.visibility = visibility;
		this.trackingObject = worldObject.getPosition();
		this.ObjectList=world.objectsAtPoint(worldObject.getPosition());
		this.mapBoundary = mapBoundary;
		ObjectMap = new WorldObject[2*this.visibility+1][2*this.visibility+1];
		ColourMap = new Color[2*this.visibility+1][2*this.visibility+1];
	}

	public Color[][] getColourMap(){
		int indexX = 0;
		int indexY = 0;

		
		for(int k=0;k<ObjectList.length;k++){
			for(double i = ObjectList[k].boundary.getMinY(); i <= ObjectList[k].boundary.getMaxY();i++){
				for(double j = ObjectList[k].boundary.getMinX(); j <=ObjectList[k].boundary.getMaxX();j++){
					boolean horizontal = true;
					if((i<=mapBoundary.getMaxY())&&(i>=mapBoundary.getMinY())){
						indexY = (int) Math.round(i-this.worldObject.getPosition().getY()+this.visibility);
						if((j <= mapBoundary.getMaxX()) && (j >=mapBoundary.getMinX())){
							indexX = (int) Math.round(j-this.worldObject.getPosition().getX()+this.visibility);
							ObjectMap[indexX][indexY] = ObjectList[k];
							ColourMap[indexX][indexY] = ColourMap[indexX][indexY].add(ObjectMap[indexX][indexY].getColour());
							Point2D.Double pos = new Point2D.Double();
							pos.x = mapBoundary.getMinX()+j;
							pos.y = mapBoundary.getMinY()+i;
							road = this.world.roadAtPoint(pos);
							Road checkdirection;
							Point2D.Double checkHorizontal = new Point2D.Double();
							checkHorizontal.x = mapBoundary.getMaxX();
							checkHorizontal.y = mapBoundary.getMinY()+i;
							checkdirection = this.world.roadAtPoint(checkHorizontal);
							markers = road.getMarkers();
							if(road!=null){
								ColourMap[indexX][indexY] = ColourMap[indexX][indexY].add(Color.DARK_GRAY);
								if(road != checkdirection){
									horizontal = false;
								}
								MarkingRender(horizontal,markers, this.ColourMap,indexX);
							}
						}
					}
				}
			}
		}
		return ColourMap;
	}

	private void MarkingRender(boolean horizontal, RoadMarking[] markers, Color[][] colourMap, int index) {
		// TODO Auto-generated method stub
		for(int i = 0; i<markers.length;i++){
			int indexX;
			int indexY;
			if(horizontal){
				Rectangle2D.Double boundary = new Rectangle2D.Double(markers[i].position.getX()-1f,markers[i].position.getY()-1f,2f,1f);
				for(double a = boundary.getMinY(); a<= boundary.getMaxY();a++){
					for(double b = boundary.getMinX(); b<= boundary.getMaxX();b++){
						if(a >=this.mapBoundary.getMinY()&&a<=this.mapBoundary.getMaxY()){
							indexY = (int) Math.round(a-this.worldObject.getPosition().getY()+this.visibility);
							if(b>=this.mapBoundary.getMinX()&&b<=this.mapBoundary.getMaxX()){
								indexX = (int) Math.round(b-this.worldObject.getPosition().getX()+this.visibility);
								ColourMap[indexX][indexY] = ColourMap[indexX][indexY].add(Color.LIGHT_GRAY);
							}
						}
					}
				}
			}else{
				Rectangle2D.Double boundary = new Rectangle2D.Double(markers[i].position.getX()-1f,markers[i].position.getY()-1f,1f,2f);
				for(double a = boundary.getMinY(); a<= boundary.getMaxY();a++){
					for(double b = boundary.getMinX(); b<= boundary.getMaxX();b++){
						if(a >=this.mapBoundary.getMinY()&&a<=this.mapBoundary.getMaxY()){
							indexY = (int) Math.round(a-this.mapBoundary.getMinY());
							if(b>=this.mapBoundary.getMinX()&&b<=this.mapBoundary.getMaxX()){
								indexX = (int) Math.round(b-this.mapBoundary.getMinY());
								ColourMap[indexX][indexY] = ColourMap[indexX][indexY].add(Color.LIGHT_GRAY);
							}
						}
					}
				}
			}
		}
	}
}
