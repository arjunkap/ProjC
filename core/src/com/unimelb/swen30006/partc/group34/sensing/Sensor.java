package com.unimelb.swen30006.partc.group34.sensing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.ISensing;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class Sensor implements ISensing {

	private boolean DEBUG = true;

	private Car car; // instance of current car
	private SpaceMap spaceMap; // space map
	private ColorMap colorMap; // color map
	private VelocityMap velocityMap; // velocity map
	private World world; // instance of World object

	// default constructor
	public Sensor(World world, Car car) {
		this.world = world;
		this.car = car;
	}

	@Override
	public boolean update(Point2D.Double pos, float delta, int visibility) {
		// return value
		boolean updated = false;

		// initialize three maps
		spaceMap = new SpaceMap(visibility);
		colorMap = new ColorMap(visibility);
		velocityMap = new VelocityMap(visibility);

		// get all objects within visibility
		WorldObject objects[] = world.objectsAtPoint(pos);
		// map the objects into 2D map
		ArrayList<ArrayList<ArrayList<WorldObject>>> objectsMap = populate2DMap(objects, pos, visibility);

		// generate three maps from the 2Dmap of objects
		spaceMap.generateSpaceMap(pos, objectsMap);
		colorMap.generateColorMap(pos, objectsMap);
		velocityMap.generateVelocityMap(pos, car.getVelocity(), objectsMap);

		if (DEBUG) {
			spaceMap.printMap();
			DEBUG = false;
		}

		// finished updating
		updated = true;
		return updated;
	}

	// method to map all objects within visibility to a 2D map
	private ArrayList<ArrayList<ArrayList<WorldObject>>> populate2DMap(WorldObject[] objects, Point2D.Double pos,
			int visibility) {
		// create 2D map of size NxN
		ArrayList<ArrayList<ArrayList<WorldObject>>> map = new ArrayList<ArrayList<ArrayList<WorldObject>>>();
		// N row
		for (int i = 0; i < visibility; i++) {
			ArrayList<ArrayList<WorldObject>> row = new ArrayList<ArrayList<WorldObject>>();

			// N column
			for (int j = 0; j < visibility; j++) {
				ArrayList<WorldObject> cell = new ArrayList<WorldObject>();
				// add new cell
				row.add(cell);
			}

			// add new row
			map.add(row);
		}

		// the boundary for objects within visibility
		double xMax = pos.getX() + visibility / 2;
		double xMin = pos.getX() - visibility / 2;
		double yMax = pos.getY() + visibility / 2;
		double yMin = pos.getY() - visibility / 2;

		// loop through all objects and assign them to corresponding cell(s)
		for (int i = 0; i < objects.length; i++) {
			// find the x and y range of current object relative to the map
			double halfWidth = objects[i].getWidth() / 2;
			double halfLength = objects[i].getLength() / 2;
			double objXmax = objects[i].getPosition().getX() + halfWidth;
			double objXmin = objects[i].getPosition().getX() - halfWidth;
			double objYmax = objects[i].getPosition().getY() + halfLength;
			double objYmin = objects[i].getPosition().getY() - halfLength;

			if (objXmin > xMax || objYmin > yMax || objXmax < xMin || objYmax < yMin) {
				continue;
			}

			// highest x

			int xMaxMap = visibility - 1;
			if (xMax > objXmax) {
				xMaxMap = (int) Math.floor((visibility - (xMax - objXmax)) - 1);
			}

			// lowest x
			int xMinMap = 0;
			if (xMin < objXmin) {
				xMinMap = (int) Math.floor(objXmin - xMin);
			}

			// highest y
			int yMaxMap = visibility - 1;
			if (yMax > objYmax) {
				yMaxMap = (int) Math.floor((visibility - (yMax - objYmax)) - 1);
			}

			// lowest y
			int yMinMap = 0;
			if (yMin < objYmin) {
				yMinMap = (int) Math.floor(objYmin - yMin);
			}

			// assign objects to cell(s)
			for (int j = xMinMap; j <= xMaxMap; j++) {
				for (int k = yMinMap; k <= yMaxMap; k++) {
					int l = visibility - 1 - k;
					map.get(l).get(j).add(objects[i]);
				}
			}
		}

		return map;
	}

	// return generated velocity map
	@Override
	public Vector2[][] getVelocityMap() {
		return velocityMap.getVelocityMap();
	}

	// return generated space map
	@Override
	public boolean[][] getSpaceMap() {
		return spaceMap.getSpaceMap();
	}

	// return generated color map
	@Override
	public Color[][] getColourMap() {
		return colorMap.getColorMap();
	}

}
