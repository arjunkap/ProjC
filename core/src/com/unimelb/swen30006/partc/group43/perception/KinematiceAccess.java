package com.unimelb.swen30006.partc.core.group43.perception;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public interface KinematiceAccess{

  public void setVelocity(Vector2 velocity);
  public void setTimeToCollision(Float timeToCollision);
  public Classification getObjectType();
  public Vector2 getPosKin();
  public Float getDistanceKin();

}
