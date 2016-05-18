package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public interface SpaceAnalyser{

  public void calculateKinematics(KinematicAccess mapObject,CombinedCell[][] map,Vector2 currentVelocity);
}
