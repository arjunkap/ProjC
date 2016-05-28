package com.unimelb.swen30006.partc.group12.sensing;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;


public class ColourMap {

	private Color[][] colourMap;
	private Rectangle2D.Double mapBoundary;
	Point2D.Double pos;
	private World world;
	private int visibility;
	
	private WorldObject[] worldObjects;
	
	
	public ColourMap(World world,Rectangle2D.Double mapBoundary,Point2D.Double pos,int visibility){
		this.world=world;
		this.mapBoundary=mapBoundary;
		this.pos=pos;
		this.visibility=visibility;
	}
	
	private void updateColourMap_worldObj(WorldObject object){
		updateColourMap(object.boundary,object.getColour());
	}
	
	//update roads' color
	private void updateColourMap_road(Road road){
		Rectangle2D.Double boundary;
		//vertical road
		if(road.getStartPos().x==road.getEndPos().x)
			boundary=new Rectangle2D.Double(road.getStartPos().x-road.getWidth()/2,road.getStartPos().y,road.getWidth(),road.getLength());
		//horizontal road
		else
			boundary=new Rectangle2D.Double(road.getStartPos().x,road.getStartPos().y-road.getWidth()/2,road.getLength(),road.getWidth());
		updateColourMap(boundary,Color.DARK_GRAY);
	}
	
	//update roadMarkings' color
	private void updateColourMap_roadMark(Road road,RoadMarking roadMarking){
		Rectangle2D.Double boundary;
		//vertical
		if(road.getStartPos().x==road.getEndPos().x)
			boundary=new Rectangle2D.Double(roadMarking.position.x-1f,roadMarking.position.y-1f,1f,2f);
		//horizontal
		else
			boundary=new Rectangle2D.Double(roadMarking.position.x-1f,roadMarking.position.y-1f,2,1);
		updateColourMap(boundary,Color.LIGHT_GRAY);
	}
	
	

		//update intersections's color
		private void updateColourMap_intersection(){
			Intersection[] intersections=Calculator.intersections(mapBoundary, world);
			for(Intersection intersection:intersections){
				Rectangle2D.Double boundary=new Rectangle2D.Double(intersection.pos.x,intersection.pos.y,intersection.width,intersection.length);
				updateColourMap(boundary,Color.DARK_GRAY);
				
			//update intersections' boundaryLine color
				Rectangle2D.Double[] boundaryLines=new Rectangle2D.Double[4];
				boundaryLines[0]=new Rectangle2D.Double(intersection.pos.x,intersection.pos.y,intersection.width,1);
				boundaryLines[1]=new Rectangle2D.Double(intersection.pos.x,intersection.pos.y,1,intersection.length);
				boundaryLines[2]=new Rectangle2D.Double(intersection.pos.x,intersection.pos.y+intersection.length-1,intersection.width,1);
				boundaryLines[3]=new Rectangle2D.Double(intersection.pos.x+intersection.width-1,intersection.pos.y,1,intersection.length);
				for(Rectangle2D.Double boundaryLine:boundaryLines){
					if(boundaryLine.intersects(mapBoundary))
						updateColourMap(boundaryLine,Color.LIGHT_GRAY);
				}
			}
		}
		
		//update environment's color
		private void updateColourMap_environment(){
			updateColourMap(mapBoundary,world.getEnvironmentColour());
		}
		
		private void updateColourMap(Rectangle2D.Double boundary,Color colour){
			int iStartIndex,iEndIndex,jStartIndex,jEndIndex;
			Vector2[] vectors=Calculator.getStartEndIndex(boundary, mapBoundary, visibility);
			iStartIndex=(int) vectors[0].x;
			jStartIndex=(int) vectors[0].y;
			iEndIndex=(int) vectors[1].x;
			jEndIndex=(int) vectors[1].y;
			
			for(int i=iStartIndex;i<iEndIndex;i++){
				for(int j=jStartIndex;j<jEndIndex;j++){
					colourMap[i][j].add(colour);
				}
			}
		}
		
		private void initColourMap(){
			this.colourMap=new Color[(int) Math.ceil(mapBoundary.height)][(int) Math.ceil(mapBoundary.width)];
			for(int i=0;i<(int) Math.ceil(mapBoundary.height);i++){
				for(int j=0;j<(int) Math.ceil(mapBoundary.width);j++){
					colourMap[i][j]=Color.GRAY;
				}
			}
		}
		public Color[][] generateColourMap(){
			//update worldObj
			initColourMap();
			
			worldObjects=Calculator.objects(mapBoundary, world);
			for(WorldObject object:worldObjects){
				updateColourMap_worldObj(object);
			}
			//update road and roadMarkings on the relative road
			Road[] roads=world.roadsAroundPoint(pos);
			for(Road road:roads){
				updateColourMap_road(road);
				RoadMarking[] roadMarkings=road.getMarkers();
				for(RoadMarking roadMarking:roadMarkings){
					updateColourMap_roadMark(road,roadMarking);
				}
			}
			
			//update intersections
			updateColourMap_intersection();
			
			//update environment
			updateColourMap_environment();
			
			return colourMap;
		}
		
		
}
