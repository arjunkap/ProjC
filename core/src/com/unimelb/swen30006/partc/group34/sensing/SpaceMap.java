package com.unimelb.swen30006.partc.group34.sensing;

import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.WorldObject;

import java.awt.geom.Point2D;

public class SpaceMap extends Map {

	private boolean spaceMap[][];

	public SpaceMap(int visibility) {
		super(visibility);
		spaceMap = new boolean[visibility][visibility];
	}

	public void generateSpaceMap(Point2D.Double pos, ArrayList<ArrayList<ArrayList<WorldObject>>> objectsMap) {

		for (int i = 0; i < getVisibility(); i++) {
			for (int j = 0; j < getVisibility(); j++) {
				spaceMap[i][j] = false;
			}
		}

		for (int i = 0; i < getVisibility(); i++) {
			for (int j = 0; j < getVisibility(); j++) {
				ArrayList<WorldObject> objects = objectsMap.get(i).get(j);
				if (!objects.isEmpty()) {
					spaceMap[i][j] = true;
				}
			}
		}
	}

	public boolean[][] getSpaceMap() {
		return spaceMap;
	}

	public void printMap() {
		for (int i = 0; i < getVisibility(); i++) {
			for (int j = 0; j < getVisibility(); j++) {
				System.out.print(((spaceMap[i][j] ? "1" : "0") + " "));
			}
			System.out.println();
		}

	}
}
