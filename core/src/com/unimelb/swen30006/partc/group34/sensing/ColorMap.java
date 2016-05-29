package com.unimelb.swen30006.partc.group34.sensing;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class ColorMap extends Map {
	// map instance
	private Color[][] colorMap;

	public ColorMap(int visibility) {
		super(visibility);
	}

	// method to generate color map with given parameters
	public void generateColorMap(Double pos, ArrayList<ArrayList<ArrayList<WorldObject>>> objectsMap) {
		// initialize map with given size
		colorMap = new Color[getVisibility()][getVisibility()];
		for (int i = 0; i < getVisibility(); i++) {
			for (int j = 0; j < getVisibility(); j++) {
				ArrayList<WorldObject> objList = objectsMap.get(i).get(j);

				for (int k = 0; k < objList.size(); k++) {
					if (colorMap[i][j] == null)
						colorMap[i][j] = (objList.get(k)).getColour();
					else
						colorMap[i][j] = (objList.get(k)).getColour().add(colorMap[i][j]);
				}
			}
		}
	}

	// setter and getter
	public Color[][] getColorMap() {
		return colorMap;
	}

	public void setColorMap(Color[][] colorMap) {
		this.colorMap = colorMap;
	}
}
