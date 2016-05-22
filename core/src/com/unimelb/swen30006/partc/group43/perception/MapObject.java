package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
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
private float timeToCollision;
private HashMap<String,Object> information = new HashMap<String,Object>();
private Classification objectType;
private MapObject object;

public MapObject(Point2D.Float pos, ArrayList<Point> shape, float width, float length, float distance){
	this.pos = pos;
	this.shape = shape;
	this.width = width;
	this.length = length;
	this.distance = distance;
}
public PerceptionResponse[] converToPerceptionResponse(ArrayList<SpaceAnalyserReturn> ret){
	PerceptionResponse response[] = new PerceptionResponse[ret.size()];
	for(int i = 0; i<=ret.size(); i++){
		response[i] = (PerceptionResponse) ret.toArray()[i]; 
	}
	return response;
	
}
@Override
public Point2D.Float getPos() {
	// TODO Auto-generated method stub
	return object.pos;
}
@Override
public ArrayList<Point> getShape() {
	// TODO Auto-generated method stub
	return object.shape;
}
@Override
public Float getWidth() {
	// TODO Auto-generated method stub
	return object.width;
}
@Override
public Float getLength() {
	// TODO Auto-generated method stub
	return object.length;
}
@Override
public Float getDistance() {
	// TODO Auto-generated method stub
	return object.distance;
}
@Override
public Vector2 getVelocity() {
	// TODO Auto-generated method stub
	return object.velocity;
}
@Override
public Float getTimeToCollision() {
	// TODO Auto-generated method stub
	return object.timeToCollision;
}

public void setObjectType(Classification objecttype){
	this.objectType = objecttype;
}
public Classification getObjectType(){
	return object.objectType;
}
public Point2D.Float getPosKin(){
	return object.pos;
}
public Float getDistanceKin(){
	return object.distance;
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
