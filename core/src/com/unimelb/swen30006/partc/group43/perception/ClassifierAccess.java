package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.Classification;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Rectangle2D;

public interface ClassifierAccess {
  public Vector2 getPos();
  public Rectangle2D.Double getShape();
  public Float getWidth();
  public Float getLength();
  public Float getDistance();
  public void getVelocity();
  public void getTimeToCollision();
  public void setInformation(String key,Object obj);
}
