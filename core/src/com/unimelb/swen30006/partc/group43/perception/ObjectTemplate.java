package com.unimelb.swen30006.partc.group43.perception;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.group43.perception.Range;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

/**
 * Represents a general description of an object type.
 * Used by the classifier to determine if a candidate object
 * is of the type described by this class.
 */
public class ObjectTemplate{

  private Range<Float> lengthRange = null;
  private Range<Float> widthRange = null;
  private Range<Float> speedRange = null;
  private ArrayList<Color> colorRange = null;
  private Classification typeWithinRange = null;
  private boolean allowAnyColor = false;

  public ObjectTemplate(Range<Float> lengthRange, Range<Float> widthRange, 
                        Range<Float> speedRange, ArrayList<Color> colorRange,
                        Classification typeWithinRange){
    this.lengthRange = lengthRange;
    this.widthRange = widthRange;
    this.speedRange = speedRange;
    this.colorRange = colorRange;
    this.typeWithinRange = typeWithinRange;
  }

  public ObjectTemplate(Classification typeWithinRange){
    this.typeWithinRange = typeWithinRange;
  }

  public void setAllowAnyColorTrue(){
    this.allowAnyColor = true;
  }

  public boolean isWithinRange(Float length, Float width, Float speed, ArrayList<Color> color){

    if( lengthRange.isInRange(length) &&
        widthRange.isInRange(width) &&
        speedRange.isInRange(speed) &&
        (isColorsInRange(color) || allowAnyColor )){
      return true;
    }else{
      return false;
    }

  }

  public Classification getType(){
    return this.typeWithinRange;
  }

  /*
   * Returns true if any one of the colors given
   * is within the colors specified for this template.
   */
  private boolean isColorsInRange(ArrayList<Color> colors){

    for(Color col : colors){
      for(Color c : colorRange){
        if( col == c) return true;
      }
    }

    return false;
  }


  private boolean isColorInRange(Color color){

    for(Color c : colorRange){
      if( c == color) return true;
    }

    return false;
  }

}
