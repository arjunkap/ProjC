package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.badlogic.gdx.math.Vector2;

public interface KinematicAnalyser{

  public void calculateKinematics(KinematicAccess mapObject,CombinedCell[][] map,Vector2 currentVelocity);

}
