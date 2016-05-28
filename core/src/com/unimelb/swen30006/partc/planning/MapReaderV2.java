package com.unimelb.swen30006.partc.vectormap;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


public class MapReaderV2 {

	private String fileName;

	// Private data structures for loading things
	private HashMap<String, Node> intersections;

	public MapReaderV2(String file) {
		this.fileName = file;
		this.intersections = new HashMap<String, Node>();
	}

	public ArrayList<Node> makeGraph(Array<Element> iList,Array<Element> rList){
		ArrayList<Node> thisOne = new ArrayList<Node>();

		for(Element e:iList){
			processIntersection(e);
		}

		for(Element e:rList){
			completeNode(e);
		}

		for(Node n:intersections.values()){
			thisOne.add(n);
		}


		return thisOne;
	}

	private void completeNode(Element e) {
		Element intersection = e.getChildByName("intersections");
		Element startIntersection = intersection.getChildByName("start");
		Element endIntersection = intersection.getChildByName("end");
		

		if(startIntersection != null && endIntersection != null){
			intersections.get(startIntersection.get("id")).addChildren(intersections.get(endIntersection.get("id")));
			intersections.get(endIntersection.get("id")).addChildren(intersections.get(startIntersection.get("id")));
		}
	}

	private void processIntersection(Element intersectionElement){
		// Retrieve all the data
		String roadID = intersectionElement.get("intersection_id");
		float x_pos = intersectionElement.getFloat("start_x");
		float y_pos = intersectionElement.getFloat("start_y");
		float width = intersectionElement.getFloat("width");
		float height = intersectionElement.getFloat("height");

		this.intersections.put(roadID, new Node(x_pos+width/2,y_pos+height/2,width,height));
	}

	public ArrayList<Node> buildIt(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal(fileName);			
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			// Setup data structures
			Element roads = root.getChildByName("roads");
			Element intersections = root.getChildByName("intersections");

			Array<Element> intersectionList = intersections.getChildrenByName("intersection");			
			Array<Element> roadList = roads.getChildrenByName("road");


			return makeGraph(intersectionList,roadList);
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}


}
