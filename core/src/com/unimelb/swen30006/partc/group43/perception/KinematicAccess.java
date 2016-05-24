package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.Classification;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;

import java.awt.geom.Point2D;
import com.badlogic.gdx.math.Vector2;
import java.awt.Point;
import java.util.ArrayList;

public interface KinematicAccess{

  public void setVelocity(Vector2 velocity);
  public void setTimeToCollision(Float timeToCollision);
  public ArrayList<Point> getShape();
  public Classification getObjectType();
  public Point2D.Float getPosKin();
  // Not in class diagram but required
  public Float getWidthKin();
  // Not in class diagram but required
  public Float getLengthKin();
  public Float getDistanceKin();

}
