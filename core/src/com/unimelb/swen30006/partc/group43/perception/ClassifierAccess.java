package com.unimelb.swen30006.partc.core.group43.perception;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
package com.unimelb.swen30006.partc.core.group43.perception;

public interface ClassifierAccess {

  public void setVelocity(Vector2);
  public void setTimeToCollision(Float);
  public Classification getObjectType();
  public Vector2 getPosKin();
  public Float getDistanceKin();

}
