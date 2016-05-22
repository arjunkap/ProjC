package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.Classification;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;

import java.awt.geom.Point2D;
import com.badlogic.gdx.math.Vector2;

public interface KinematicAccess{

  public void setVelocity(Vector2 velocity);
  public void setTimeToCollision(Float timeToCollision);
  public Classification getObjectType();
  public Point2D.Float getPosKin();
  public Float getDistanceKin();

}
