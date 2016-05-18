package com.unimelb.swen30006.partc.group43.perception;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;

public class CombinedCell{

  final Boolean space;
  final Vector2 velocity;
  final Color color;

  public CombinedCell(Boolean space, Vector2 velocity, Color color){
    this.space = space;
    this.velocity = velocity;
    this.color = color;
  }

}
