package com.unimelb.swen30006.partc.group43.perception;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.core.objects.Car;

import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SimpleSpaceAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyser;
import com.unimelb.swen30006.partc.group43.perception.KinematicAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SimpleKinematicAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SimpleKinematicAnalyser;
import com.unimelb.swen30006.partc.group43.perception.MapObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.ArrayList;
/**
 * This class is the implementation of IPerception Interface.
 * It acts as a controller for cordination the different subcomponents
 * which work together to identify and classify objects in the map
 * @author Muditha Wanninayake
 * @version 1.0
 */
public class Perception implements IPerception{

  SpaceAnalyser spaceAnalyser = new SimpleSpaceAnalyser();
  KinematicAnalyser kinematicAnalyser = new SimpleKinematicAnalyser();
  ArrayList<MapObject> objects = new ArrayList<MapObject>();

  public Perception(){
  }


	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap){
     return new PerceptionResponse[0];
  }

  public void test(){

    CombinedCell[][] map = new CombinedCell[7][7];
    boolean[][] b = new boolean[][]{{true,true,false,true,true,true,false},
                                    {true,false,true,false,true,true,true},
                                    {true,false,true,false,true,true,true},
                                    {false,false,true,false,true,true,true},
                                    {true,false,true,false,true,true,true},
                                    {true,false,true,false,true,true,true},
                                    {true,false,true,true,false,false,false}};

    for(int i = 0 ; i < b.length ; i++){
      for(int j = 0 ; j < b[0].length ; j ++){
        map[i][j] = new CombinedCell(b[i][j],new Vector2(1,-1), Color.BLACK);
      }
    }

    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){
        System.out.print(map[i][j]);
      }
      System.out.print("\n");
    }

    // Identify Objects
    ArrayList<SpaceAnalyserReturn> returns = spaceAnalyser.getObjects(map);

    // Create the objects
    objects = createObjects(returns);

    // Calculate Kinematic properties of the objects
    for(MapObject object : objects){
      kinematicAnalyser.calculateKinematics(object,map, new Vector2(0,1));
    }

    // Classfiy the Objects

  }

  private ArrayList<MapObject> createObjects(ArrayList<SpaceAnalyserReturn> returns){
    ArrayList<MapObject> newObjects = new ArrayList<MapObject>();
    // Create the objects
    for(SpaceAnalyserReturn s : returns){
      newObjects.add(new MapObject(s.pos,s.getShape(),s.width,s.length,s.distance));
    }

    return newObjects;
  }

  public void test2(){

    Point2D.Float aP = new Point2D.Float(0,0);
    Point2D.Float bP = new Point2D.Float(2,0);
    Vector2 aVel = new Vector2(1,-1);
    Vector2 bVel = new Vector2(-1,-1);
    aVel.scl(1/aVel.len());
    bVel.scl(1/bVel.len());
    ((SimpleKinematicAnalyser) kinematicAnalyser).carVel = new Vector2(2,2);
    ((SimpleKinematicAnalyser) kinematicAnalyser).mapHeight = 20;

    System.out.println(((SimpleKinematicAnalyser) kinematicAnalyser).timeTillCollision(aP, aVel, bP, bVel, 0.4f));

  }

}
