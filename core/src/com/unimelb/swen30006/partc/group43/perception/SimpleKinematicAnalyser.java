package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.KinematicAnalyser;
import com.badlogic.gdx.math.Vector2;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * SimpleKinematicAnalyser determines the velocity and
 * the time to collision properties of the MapObject.
 */
public class SimpleKinematicAnalyser implements KinematicAnalyser{

  public Vector2 carVel = null;
  public int mapHeight = 0;
  public int mapWidth = 0;
  private boolean debugginOn = true;

  public void calculateKinematics(KinematicAccess mapObject,CombinedCell[][] map,Vector2 currentVelocity){

    this.carVel = currentVelocity;
    this.mapHeight = map.length;
    this.mapWidth = map[0].length;

    // Determine the velocity
    // Find the highest velocity
    float max = 0.0f;
    Vector2 vel = new Vector2(0,0);
    Point pCurr = null;
    for(Point p : mapObject.getShape()){
      if(map[p.y][p.x].velocity.len() >= max ){
        vel = map[p.y][p.x].velocity;
        max = map[p.y][p.x].velocity.len();
        pCurr = p;
      }
    }

    // Determine the time to collision
    Point2D.Float p = mapObject.getPosKin();
    Float objectWidth = (mapObject.getLengthKin() > mapObject.getWidthKin()) ? mapObject.getLengthKin() : mapObject.getWidthKin();
    Float timeToColl = timeTillCollision(new Point2D.Float(p.x,p.y*-1),
                                         vel,
                                         new Point2D.Float((float) mapWidth/2.0f,(float) mapHeight/-2.0f),
                                         currentVelocity,
                                         objectWidth/2);
    mapObject.setVelocity(vel);
    mapObject.setTimeToCollision(timeToColl);
  }


  private float timeToReachMapEnd(){
    return  (  ((float) mapHeight) / carVel.len() );
  }

  private float timeTillCollision(Point2D.Float a, Vector2 _aVel, Point2D.Float b, Vector2 _bVel,float minDist){

    // Only try for time required to reach the end of the map ( and safety factor of 2 )
    float alpha = 0.1f;
    float timeMax = timeToReachMapEnd();
    float time = 0.0f;
    float timeAtCollision = 0.0f;

    Vector2 aVec = new Vector2(a.x,a.y);
    Vector2 bVec = new Vector2(b.x,b.y);
    Vector2 aVel = _aVel.cpy();
    Vector2 bVel = _bVel.cpy();

    if(debugginOn){
      System.out.println(a);
      System.out.println("timeMax:" + timeMax);
      System.out.println("a"+a);
      System.out.println("b"+b);
      System.out.println("aVel"+aVel);
      System.out.println("bVel"+bVel);
    }


    while( time < timeMax){

      float dst = aVec.dst(bVec); 
      aVec.mulAdd(aVel.cpy(),alpha);
      bVec.mulAdd(bVel.cpy(),alpha);

      if(dst <= minDist){
        if(debugginOn){
          System.out.println("COLLIDE");
          System.out.println(dst);
        }
        return time;
      }

      time += alpha;
    }

    return -1.0f;
  }


}
