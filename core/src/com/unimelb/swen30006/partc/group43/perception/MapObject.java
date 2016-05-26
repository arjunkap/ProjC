package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.group43.perception.ClassifierAccess;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

public class MapObject implements KinematicAccess,ClassifierAccess{
	 
private final Point2D.Float pos;
private final ArrayList<Point> shape;
private float width;
private float length;
private float distance;
private Vector2 velocity;
private Vector2 relativeVelocity;
private float timeToCollision;
private HashMap<String,Object> information = new HashMap<String,Object>();
private Classification objectType;

public MapObject(Point2D.Float pos, ArrayList<Point> shape, float width, float length, float distance){
	this.pos = pos;
	this.shape = shape;
	this.width = width;
	this.length = length;
	this.distance = distance;
}

public PerceptionResponse convertToPerceptionResponse(){
	return new PerceptionResponse(this.distance, 
                                this.relativeVelocity, 
                                new Vector2(pos.x,pos.y), 
                                this.timeToCollision,
                                objectType, 
                                information);
}


@Override
public Point2D.Float getPos() {
	// TODO Auto-generated method stub
	return this.pos;
}
@Override
public ArrayList<Point> getShape() {
	// TODO Auto-generated method stub
	return this.shape;
}
@Override
public Float getWidth() {
	// TODO Auto-generated method stub
	return this.width;
}
@Override
public Float getLength() {
	// TODO Auto-generated method stub
	return this.length;
}

@Override
public Float getWidthKin() {
	// TODO Auto-generated method stub
	return this.width;
}
@Override
public Float getLengthKin() {
	// TODO Auto-generated method stub
	return this.length;
}
@Override
public Float getDistance() {
	// TODO Auto-generated method stub
	return this.distance;
}
@Override
public Vector2 getVelocity() {
	// TODO Auto-generated method stub
	return this.velocity;
}
@Override
public Float getTimeToCollision() {
	// TODO Auto-generated method stub
	return this.timeToCollision;
}

@Override
public void setObjectType(Classification type){
  this.objectType = type;
}

public void setRelativeVelocity(Vector2 velocity){
  this.relativeVelocity = velocity;
}

public Classification getObjectType(){
	return this.objectType;
}
public Point2D.Float getPosKin(){
	return this.pos;
}
public Float getDistanceKin(){
	return this.distance;
}
public void setInformation(String name,Object mapobject){
	this.information.put(name, mapobject);
}
public void setVelocity(Vector2 velocity){
	this.velocity = velocity;
}
public void setTimeToCollision(Float timeToCollision){
	this.timeToCollision = timeToCollision;
}

}
