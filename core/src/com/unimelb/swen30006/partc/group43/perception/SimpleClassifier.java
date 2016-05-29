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
  ArrayList<Color> trafficColors = new ArrayList<Color>();

  public SimpleClassifier(){
    trafficColors.add(Color.RED);
    trafficColors.add(Color.GREEN);
    trafficColors.add(Color.YELLOW);

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
    signColor.add(new Color(180.0f/255f,209.0f/255f,18.0f/255f,1.0f));
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
    ObjectTemplate lighTemplate =  new ObjectTemplate( new Range<Float>(2.0f,3.0f),
                                   new Range<Float>(2.0f,3.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   trafficColors,
                                   Classification.TrafficLight);
    //lighTemplate.setAllowAnyColorTrue();
    objectTemplates.add(lighTemplate);


    // Car
    ObjectTemplate carTemplate = new ObjectTemplate( new Range<Float>(4.0f,20.0f),
                                   new Range<Float>(4.0f,20.0f),
                                   new Range<Float>(0.0f,200.0f),
                                   new ArrayList<Color>(),
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
    objectTemplates.add(laneMarkingTemplate);

    // RoadMarking
    ArrayList<Color> roadMarkingColor = new ArrayList<Color>();
    roadMarkingColor.add(Color.DARK_GRAY);
    ObjectTemplate RoadMarkingTemplate = new ObjectTemplate( new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,2.0f),
                                   new Range<Float>(0.0f,0.0f),
                                   roadMarkingColor,
                                   Classification.RoadMarking);
    objectTemplates.add(RoadMarkingTemplate);

  }


  /**
   * Classify the object given
   */
  public void classify(ClassifierAccess mapObject,CombinedCell[][] map){

    for(ObjectTemplate m : objectTemplates ){
      if( m.isWithinRange(mapObject.getWidth(),
                          mapObject.getLength(),
                          mapObject.getVelocity().len(),
                          colorsAtMapObject(mapObject,map))){
        Classification type  = m.getType();
        if( type == Classification.TrafficLight) handleTrafficLight(mapObject,map);

        /*( DELETE COMMENTS
        if( type == Classification.TrafficLight){
          System.out.println("Traffic Light Detected");
          System.out.println(((MapObject) mapObject).information.get("State"));
        }
        if(true){
          System.out.println(type);
          System.out.println(mapObject);
          System.out.println("-------------");
          for(Color c : colorsAtMapObject(mapObject,map)){
            //System.out.print(c +",");
          }
        }
        //)*/

        mapObject.setObjectType(m.getType());
        break;
      }
    }

  }

  private boolean colorArrayContains(ArrayList<Color> colors,Color c){

    for(Color col : colors){
        if( col.toString().equals(c.toString())) return true;
    }

    return false;
  }


  /**
   * Determine the state which the traffic light is in.
   */
  private boolean handleTrafficLight(ClassifierAccess mapObject, CombinedCell[][] map){
    ArrayList<Color> colors = colorsAtMapObject(mapObject, map);
    if(colorArrayContains(colors,Color.YELLOW)){
      mapObject.setInformation("State",TrafficLight.State.Amber);
      return true;
    }

    if( colorArrayContains(colors,Color.RED)){
      mapObject.setInformation("State",TrafficLight.State.Red);
      return true;
    }

    if( colorArrayContains(colors,Color.GREEN)){
      mapObject.setInformation("State",TrafficLight.State.Green);
      return true;
    }

    return false;

  }


  /**
   * Return an arrary of colors wich describe the
   * shape of the current object.
   */
  private ArrayList<Color> colorsAtMapObject(ClassifierAccess mapObject,CombinedCell[][] map){
    ArrayList<Point> shape = mapObject.getShape();
    ArrayList<Color> colors = new ArrayList<Color>();
    for(Point p : shape){
      colors.add(map[p.x][p.y].color);
    } 
    return colors;
  }

}


