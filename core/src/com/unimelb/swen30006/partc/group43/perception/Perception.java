package com.unimelb.swen30006.partc.group43.perception;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.core.objects.Car;

import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SimpleSpaceAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyser;
import com.unimelb.swen30006.partc.group43.perception.KinematicAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SimpleKinematicAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SimpleClassifier;
import com.unimelb.swen30006.partc.group43.perception.MapObject;
import com.unimelb.swen30006.partc.group43.perception.Range;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;

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
  SimpleClassifier classifier = new SimpleClassifier(); 
  ArrayList<MapObject> objects = new ArrayList<MapObject>();
  private Car car;

  public Perception(Car car){
    this.car = car;
  }



	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap){


    /*( DCOMMENTS
    String filePathString = "colorout.txt";
    File f = new File(filePathString);
    //if(!f.exists() && !f.isDirectory()) { 
    //System.out.println("RED is : " + Color.RED);
    if(false) { 
        // do something
        try{
          PrintWriter writer = new PrintWriter(filePathString, "UTF-8");
          for(int i = 0 ; i <colorMap.length ; i++){
            for(int j = 0 ; j <colorMap[0].length ; j ++){
                writer.print(colorMap[i][j] + ",");
            }
              writer.print("\n");
          }
        }catch(Exception e){}
    }
    )*/

    CombinedCell[][] map = new CombinedCell[spaceMap.length][spaceMap[0].length];
    for(int i = 0 ; i < spaceMap.length ; i++){
      for(int j = 0 ; j < spaceMap[0].length ; j ++){
        map[i][j] = new CombinedCell(spaceMap[i][j], velMap[i][j], colorMap[i][j]);
      }
    }

    // Identify Objects
    ArrayList<SpaceAnalyserReturn> returns = spaceAnalyser.getObjects(map);

    // Create the objects
    objects = createObjects(returns);

    // Calculate Kinematic properties of the objects
    for(MapObject object : objects){
      kinematicAnalyser.calculateKinematics(object,map, this.car.getVelocity());
    }

    // Classfiy the Objects
    for(MapObject object: objects){
      classifier.classify(object,map);
    }

    PerceptionResponse[] ret = new PerceptionResponse[objects.size()];

    for(int i = 0; i < objects.size(); i++){
      ret[i] = objects.get(i).convertToPerceptionResponse();
    }

    return ret;

  }

  private ArrayList<MapObject> createObjects(ArrayList<SpaceAnalyserReturn> returns){
    ArrayList<MapObject> newObjects = new ArrayList<MapObject>();
    // Create the objects
    for(SpaceAnalyserReturn s : returns){
      newObjects.add(new MapObject(s.pos,s.getShape(),s.width,s.length,s.distance));
    }

    return newObjects;
  }

  public void test(){
    Range<Integer> a = new Range<Integer>(2,4);

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
    for(MapObject object: objects){
      classifier.classify(object,map);
    }

    PerceptionResponse[] ret = new PerceptionResponse[objects.size()];

    for(int i = 0; i < objects.size(); i++){
      ret[i] = objects.get(i).convertToPerceptionResponse();
    }
  }

}
