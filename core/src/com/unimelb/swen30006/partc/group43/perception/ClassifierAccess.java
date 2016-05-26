package com.unimelb.swen30006.partc.group43.perception;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.ArrayList;

public interface ClassifierAccess {
  public Point2D.Float getPos();
  public ArrayList<Point> getShape();
  public Float getWidth();
  public Float getLength();
  public Float getDistance();
  public Vector2 getVelocity();
  public Float getTimeToCollision();
  public void setInformation(String key,Object obj);
  public void setObjectType(Classification type);
}
