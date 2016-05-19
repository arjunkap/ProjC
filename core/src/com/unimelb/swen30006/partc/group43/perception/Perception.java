package com.unimelb.swen30006.partc.group43.perception;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.core.objects.Car;

import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SimpleSpaceAnalyser;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
/**
 * This class is the implementation of IPerception Interface.
 * It acts as a controller for cordination the different subcomponents
 * which work together to identify and classify objects in the map
 * @author Muditha Wanninayake
 * @version 1.0
 */
public class Perception implements IPerception{

  SpaceAnalyser spaceAnalyser = new SimpleSpaceAnalyser();

  public Perception(){
  }


	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap){
    return new PerceptionResponse[0];
  }

  public void test(){

    CombinedCell[][] map = new CombinedCell[4][4];
    boolean[][] b = new boolean[][]{{true,true,false,true},
                                    {true,false,true,false},
                                    {true,false,true,false},
                                    {true,false,true,true}};

    for(int i = 0 ; i < b.length ; i++){
      for(int j = 0 ; j < b[0].length ; j ++){
        map[i][j] = new CombinedCell(b[i][j],new Vector2(1,1), Color.BLACK);
      }
    }

    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){
        System.out.print(map[i][j]);
      }
      System.out.print("\n");
    }

    spaceAnalyser.getObjects(map);

  }

}
