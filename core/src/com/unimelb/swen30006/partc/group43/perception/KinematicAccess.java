package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.Classification;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public interface KinematicAccess{

  public void setVelocity(Vector2 velocity);
  public void setTimeToCollision(Float timeToCollision);
  public Classification getObjectType();
  public Vector2 getPosKin();
  public Float getDistanceKin();

}
