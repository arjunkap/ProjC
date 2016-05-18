package com.unimelb.swen30006.partc.group43.perception;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Rectangle2D;

public class SpaceAnalyserReturn{

  public final Vector2 pos;
  public final Rectangle2D.Double shape;
  public final Float width;
  public final Float length;
  public final Float distance;

  public SpaceAnalyserReturn(Vector2 pos,Rectangle2D.Double shape, Float width, Float length, Float distance){

    this.pos = pos;
    this.shape = shape;
    this.width = width;
    this.length = length;
    this.distance = distance;

  }
}
