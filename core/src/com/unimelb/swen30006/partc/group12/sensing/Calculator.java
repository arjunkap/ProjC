package com.unimelb.swen30006.partc.group12.sensing;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;


public class Calculator {
	
	

	public static Vector2[] getStartEndIndex(Rectangle2D.Double boundary,Rectangle2D.Double mapBoundary,int visibility){
		
		double xStartIndex,xEndIndex,yStartIndex,yEndIndex;
		Vector2[] vector=new Vector2[2];
		vector[0]=new Vector2();
		vector[1]=new Vector2();

		
		if(boundary.x<mapBoundary.x)
			xStartIndex=0;
		else
			xStartIndex=Math.floor(boundary.x-mapBoundary.getMinX());
		
		if(boundary.y<mapBoundary.getMinY())
			yStartIndex=0;
		else
			yStartIndex=Math.floor(boundary.y-mapBoundary.y);
		
		if(boundary.getMaxX()>mapBoundary.getMaxX())
			xEndIndex=visibility*2;
		else
			xEndIndex=Math.floor(boundary.getMaxX()-mapBoundary.getMinX());
		
		if(boundary.getMaxY()>mapBoundary.getMaxY())
			yEndIndex=visibility*2;
		else
			yEndIndex=Math.floor(boundary.getMaxY()-mapBoundary.getMinY());
		
		vector[0].x=(float)xStartIndex;
		vector[0].y=(float) yStartIndex;
		vector[1].x=(float) xEndIndex;
		vector[1].y=(float) yEndIndex;
		
		return vector;
	}
	
	//return the objects overlap the scan grids
	public static WorldObject[] objects(Rectangle2D.Double mapBoundary,World world){
		ArrayList<WorldObject> objects=new ArrayList<WorldObject>();
		WorldObject[] objs;
		objs=world.getObjects();
		for(WorldObject o:objs){
			if(objOverlapGrids(o.boundary,mapBoundary))
				objects.add(o);
		}
		return objects.toArray(new WorldObject[objects.size()]);
	}
	
	//return the intersections overlap the scan grids
	public static Intersection[] intersections(Rectangle2D.Double mapBoundary,World world){
		Rectangle2D.Double boundary;
		ArrayList<Intersection> intersections=new ArrayList<Intersection>();
		Intersection[] intersecs;
		intersecs=world.getIntersections();
		for(Intersection intersection:intersecs){
			boundary=new Rectangle2D.Double(intersection.pos.x,intersection.pos.y,intersection.width,intersection.length);
			if(objOverlapGrids(boundary,mapBoundary))
				intersections.add(intersection);
		}
		return intersections.toArray(new Intersection[intersections.size()]);
	}
	
	//check whether the obj intersect with scanned range
	private static boolean objOverlapGrids(Rectangle2D.Double boundary,Rectangle2D.Double mapBoundary){
		
		double objMinX=boundary.getMinX(); //left-bottom corner
		double objMinY=boundary.getMinY();
		double objMaxX=boundary.getMaxX();
		double objMaxY=boundary.getMaxY();
		if((objMinX<mapBoundary.getMinX()) || (objMaxX>mapBoundary.getMaxX()) || (objMinY<mapBoundary.getMinY()) || (objMaxY>mapBoundary.getMaxY())){
			return false;
		}
		else
			return true;
	}
}
