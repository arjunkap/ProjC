package com.unimelb.swen30006.partc.core.group43.perception;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Rectangle2D;

public interface ClassifierAccess {

public Vector2 getPos();
public Rectange2D.Double getShape();
public Float getWidth();
public Float getLength();
public Float getDistance();
public void getVelocity();
public void getTimeToCollision();
public void setObjectType(Classification type);
public void setInformation(String key,Object obj);

}
