package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.Classifier;
import com.unimelb.swen30006.partc.group43.perception.SimpleClassifier;
import com.unimelb.swen30006.partc.group43.perception.Range;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.awt.Point;

/**
 * SimpleClassifier Identifies objects and determines their
 * type along with any other miscelanious properties.
 */
public class SimpleClassifier implements Classifier {

  ArrayList<ObjectTemplate> objectTemplates = new ArrayList<ObjectTemplate> ();

  public SimpleClassifier(){

    /*
    For reference :
      Length
      Width
      Speed
      Color
      Type
    */

    //Sign
    ArrayList<Color> signColor = new ArrayList<Color>();
    signColor.add(new Color(180.0f,209.0f,18.0f,1.0f));
    objectTemplates.add( new ObjectTemplate( new Range<Float>(1.0f,3.0f),
                                   new Range<Float>(1.0f,10.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   signColor,
                                   Classification.Sign));
    // Building
    objectTemplates.add( new ObjectTemplate( new Range<Float>(20.0f,90.0f),
                                   new Range<Float>(20.0f,90.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   new ArrayList<Color>(),
                                   Classification.Building));

    // Traffic Light
    ArrayList<Color> trafficColors = new ArrayList<Color>();
    trafficColors.add(Color.RED);
    trafficColors.add(Color.GREEN);
    trafficColors.add(Color.YELLOW);
    objectTemplates.add( new ObjectTemplate( new Range<Float>(20.0f,90.0f),
                                   new Range<Float>(20.0f,90.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   trafficColors,
                                   Classification.TrafficLight));

    // Car
    ObjectTemplate carTemplate = new ObjectTemplate( new Range<Float>(2.0f,20.0f),
                                   new Range<Float>(2.0f,20.0f),
                                   new Range<Float>(0.0f,200.0f),
                                   trafficColors,
                                   Classification.Car);

    carTemplate.setAllowAnyColorTrue();
    objectTemplates.add(carTemplate);

    // LaneMarking
    ArrayList<Color> laneMarkingColor = new ArrayList<Color>();
    laneMarkingColor.add(Color.LIGHT_GRAY);
    ObjectTemplate laneMarkingTemplate = new ObjectTemplate( new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   laneMarkingColor,
                                   Classification.LaneMarking);

    // RoadMarking
    ArrayList<Color> roadMarkingColor = new ArrayList<Color>();
    roadMarkingColor.add(Color.DARK_GRAY);
    ObjectTemplate RoadMarkingTemplate = new ObjectTemplate( new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   roadMarkingColor,
                                   Classification.RoadMarking);

  }


  public void classify(ClassifierAccess mapObject,CombinedCell[][] map){

    for(ObjectTemplate m : objectTemplates ){
      if( m.isWithinRange(mapObject.getWidth(),
                          mapObject.getLength(),
                          mapObject.getVelocity().len(),
                          colorsAtMapObject(mapObject,map))){
        Classification type  = m.getType();
        if( type == Classification.TrafficLight) handleTrafficLight(mapObject,map);
        mapObject.setObjectType(m.getType());
        break;
      }
    }

  }

  public boolean handleTrafficLight(ClassifierAccess mapObject, CombinedCell[][] map){
    ArrayList<Color> colors = colorsAtMapObject(mapObject, map);
    if( colors.contains(Color.YELLOW)){
      mapObject.setInformation("State",TrafficLight.State.Amber);
      return true;
    }

    if( colors.contains(Color.RED)){
      mapObject.setInformation("State",TrafficLight.State.Red);
      return true;
    }

    if( colors.contains(Color.GREEN)){
      mapObject.setInformation("State",TrafficLight.State.Green);
      return true;
    }

    return false;

  }


  private ArrayList<Color> colorsAtMapObject(ClassifierAccess mapObject,CombinedCell[][] map){
    ArrayList<Point> shape = mapObject.getShape();
    ArrayList<Color> colors = new ArrayList<Color>();
    for(Point p : shape){
      colors.add(map[p.x][p.y].color);
    } 
    return colors;
  }

}


