package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.core.objects.Car;
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

  final private Car car;

  public Perception(Car car){
    this.car = car;
  }

	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap){
    return new PerceptionResponse[0];
  }

}
