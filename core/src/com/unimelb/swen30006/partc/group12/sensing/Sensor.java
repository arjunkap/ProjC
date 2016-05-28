package com.unimelb.swen30006.partc.group12.sensing;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.ISensing;
import com.unimelb.swen30006.partc.core.World;


public class Sensor implements ISensing{
		private Rectangle2D.Double mapBoundary=new Rectangle2D.Double();
		Point2D.Double pos;
		private float delta;
		World world;
		private VelocityMap velocityMap;
		private ColourMap colourMap;
		private SpaceMap spaceMap;
		private int visibility;
		
		public Sensor(World world){
			this.world=world;
		}
		@Override
		public boolean update(Point2D.Double pos, float delta, int visibility) {
			// TODO Auto-generated method stub
			try{
				//
				if(pos.x-visibility<0)
					this.mapBoundary.x=0;
				else
					this.mapBoundary.x=pos.x-visibility;
				
				if(pos.y-visibility<0)
					this.mapBoundary.y=0;
				else
					this.mapBoundary.y=pos.y-visibility;
				
				if(pos.x+visibility>800)
					this.mapBoundary.width=visibility+(800-pos.x);
				else
					this.mapBoundary.width=2*visibility;
				
				if(pos.y+visibility>800)
					this.mapBoundary.y=visibility+(800-pos.y);
				else
					this.mapBoundary.y=2*visibility;
				
				this.pos=pos;
				this.delta=delta;
				this.visibility=visibility;
				
				return true;
			}
			catch(IndexOutOfBoundsException e){
				return false;
			}
				
		}
		@Override
		public Vector2[][] getVelocityMap() {
			// TODO Auto-generated method stub
			this.velocityMap=new VelocityMap(world,visibility,mapBoundary,pos,delta);
			
			return velocityMap.generateVelocityMap();
		}
		@Override
		public boolean[][] getSpaceMap() {
			// TODO Auto-generated method stub
			this.spaceMap=new SpaceMap(world,visibility,pos,mapBoundary);
			return spaceMap.generateSpaceMap();
		}
		@Override
		public Color[][] getColourMap() {
			// TODO Auto-generated method stub
			this.colourMap=new ColourMap(world,mapBoundary,pos,visibility);
			return colourMap.generateColourMap();
		}
		
		
}
