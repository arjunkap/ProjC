package com.unimelb.swen30006.partc.group43.perception;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class SpaceAnalyserReturn{

  // The pos member variable had the incorrect type in the final design. 
  public final Point2D.Float pos;
  // The direction member variable was not in the  final design but is required.
  public final Vector2 direction;
  // The shape member variable type has been changed from the final design
  private final ArrayList<Point> shape;
  public final Float width;
  public final Float length;
  public final Float distance;

  public SpaceAnalyserReturn(Point2D.Float pos,ArrayList<Point> shape, Float width, Float length, Float distance, Vector2 direction){

    this.pos = pos;
    this.shape = shape;
    this.width = width;
    this.length = length;
    this.distance = distance;
    this.direction = direction;
  }

  public ArrayList<Point> getShape(){

    ArrayList<Point> ret = new ArrayList<Point>();
    for(Point p : shape){
      ret.add(new Point(p));
    }
    return ret;
  }

  @Override
  public String toString(){
    return "Width : " + String.valueOf(width) + " Length : " + String.valueOf(length) + " Position : " + pos.toString() 
        + " Distance Vector : " + direction + " Vec length : " + direction.len();
  }

}
