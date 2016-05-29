package com.unimelb.swen30006.partc.group34.sensing;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class VelocityMap extends Map {
	// map instance
	private Vector2[][] velocityMap;
	
	public VelocityMap(int visibility) {
		super(visibility);
	}

	// method to generate velocity map with given parameters
	public void generateVelocityMap(Double pos, Vector2 carVelocity,
			ArrayList<ArrayList<ArrayList<WorldObject>>> objectsMap) {
		// initialize map with given size
		velocityMap = new Vector2[getVisibility()][getVisibility()];
		
		// loop through objects map to calculate velocity map
		for (int i = 0; i < getVisibility(); i++) {
			for (int j = 0; j < getVisibility(); j++) {
				// get list of objects in one cell
				ArrayList<WorldObject> objList = objectsMap.get(i).get(j);
				
				// pick out the one with more important velocity
				// highest priority will be car
				for (int k = 0; k < objList.size(); k++) {
					if (objList.get(k) instanceof Car) {
						velocityMap[i][j] = ((Car) 
								objList.get(k)).getVelocity().sub(carVelocity);
						break;
					}
					// in case no car
					velocityMap[i][j] = carVelocity.scl(-1);
				}
			}
		}
	}

	// setter and getter
	public Vector2[][] getVelocityMap() {
		return velocityMap;
	}

	public void setVelocityMap(Vector2[][] velocityMap) {
		this.velocityMap = velocityMap;
	}
}
