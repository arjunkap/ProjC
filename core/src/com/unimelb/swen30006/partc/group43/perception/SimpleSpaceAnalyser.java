package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyserReturn;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

/**
 * SimpleSpaceAnalyser resolves objects in the provided map.
 * However no classification of the resolved shape is carried out.
 */
public class SimpleSpaceAnalyser implements SpaceAnalyser{

  /*
   * included cells keeps track of cells which have been
   * included in an object. Each cell in the map belongs to
   * at most one object.
   */
  private HashMap<Cell,Boolean> includedCells= new HashMap<Cell,Boolean>();
  private CombinedCell[][] map = null;
  private int mapHeight;
  private int mapWidth;
  private boolean logShow = true;

  public ArrayList<SpaceAnalyserReturn> getObjects(CombinedCell[][] map){

    ArrayList<SpaceAnalyserReturn> ret = new ArrayList<SpaceAnalyserReturn>();

    this.map = map;
    this.mapHeight = map.length;
    this.mapWidth = map[0].length;
    includedCells = new HashMap<Cell,Boolean>();

    if(logShow){
      System.out.println("-------------");
      System.out.println("Center x: " + this.mapWidth/2 +" y: " + this.mapHeight/2);
    }

    // Look for non-physical objects (can't be collided with)
    // RoadMarkings(Roads) and LaneMarkings
    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){
        if( map[i][j].space && (map[i][j].color == Color.DARK_GRAY || map[i][j].color == Color.DARK_GRAY)){
          LinkedList<Cell> shape = new LinkedList<Cell>();
          ret.add(calculateDimension(shape));
        }
      }
    }

    // Look for physical objects (which can be collided with)
    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){

        Cell cell = new Cell(i,j);
        
        if( map[i][j].space && !cellInIncluded(cell)){

          // toLookList are cells which we will look around
          LinkedList<Cell> toLookList = new LinkedList<Cell>();
          // shape defines the shape of this object
          LinkedList<Cell> shape = new LinkedList<Cell>();
          // lookedThisObject keeps track of cells which have
          // been searched while determining the shape of this
          // object.
          HashMap<Cell,Boolean> lookedThisObject = new HashMap<Cell,Boolean>();

          toLookList.push(cell);
          includedCells.put(cell,true);
          lookedThisObject.put(cell,true);

          while(toLookList.size() > 0){
            shape.push(toLookList.peek());
            // Find any other non empty space
            // directly above,below,to the left,
            // or to the right.
            searchAround(toLookList,lookedThisObject);
          }

          SpaceAnalyserReturn toAdd = calculateDimension(shape);
          ret.add(toAdd);

          if(logShow){
            System.out.println("Shape Found!");
            for(Cell c : shape){
              System.out.print("("+c +")"+ " ");
            }
            System.out.println(toAdd);
            System.out.println("\n-------------");
          }


        }
      }
    }
    return ret;
  }

  /**
   * Utility function to get the
   * CombinedCell Element referenced
   * by a cell.
   */
  private CombinedCell mapGet(Cell c){
    return this.map[c.y][c.x];
  }

  /**
   * If the cells exists, check the cells above,
   * below, to the left, and to the right, of the
   * cell given. Put any cells which are not empty
   * into the toLookList supplied so the caller
   * can search these objects and also add to
   * the global includedCells hash to ensure they
   * do not become part of another object.
   */
  private void searchAround(LinkedList<Cell> toLookList,HashMap<Cell,Boolean> lookedThisObject){

    Cell cell =  toLookList.pop();

    // Look at the cell to the left
    if( cellLValid(cell) && !cellInIncluded(cell.left()) && !cellInMap(cell.left(),lookedThisObject)  ){
      lookedThisObject.put(cell.left(),true);
      if (mapGet(cell.left()).space){
        toLookList.push(cell.left());
        addToIncludedCells(cell.left());
      }
    }

    // Look at the cell to the right
    if( cellRValid(cell) && !cellInIncluded(cell.right()) && !cellInMap(cell.right(),lookedThisObject) ){ 
      lookedThisObject.put(cell.right(),true);
      if (mapGet(cell.right()).space){
        toLookList.push(cell.right());
        addToIncludedCells(cell.right());
      }
    }

    // Look at the cell to the top
    if( cellTValid(cell) && !cellInIncluded(cell.top()) && !cellInMap(cell.top(),lookedThisObject) ){
      lookedThisObject.put(cell.top(),true);
      if (mapGet(cell.top()).space){
        toLookList.push(cell.top());
        addToIncludedCells(cell.top());
      }
    }

    // Look at the cell to the bottom
    if( cellBValid(cell) && !cellInIncluded(cell.bot()) && !cellInMap(cell.bot(),lookedThisObject) ){
      lookedThisObject.put(cell.bot(),true);
      if (mapGet(cell.bot()).space){
        toLookList.push(cell.bot());
        addToIncludedCells(cell.bot());
      }
    }

  }

  /*
   * Add the supplied cell to the global includedCells.
   */
  private void addToIncludedCells(Cell c){
    includedCells.put(c,true);
  }

  /*
   * Check if the supplied cell is in the supplied hashmap.
   */
  private boolean cellInMap(Cell c,HashMap<Cell,Boolean> map){
    return map.containsKey(c);
  }

  /*
   * Check if the supplied cell is in the global hashMap
   * keeping track of cells whcih have been included in an object. 
   */
  private boolean cellInIncluded(Cell c){
    return includedCells.containsKey(c);
  }

  private void printSearchCells(){
    for(Cell s : includedCells.keySet() ){
      System.out.print(s);
      System.out.print(" ");
    }
  }

  // TODO Replace all thsee with one CellValid funciton
  private boolean cellLValid (Cell c){
    return (c.x - 1 >= 0 ) ? true : false;
  }

  private boolean cellRValid (Cell c){
    return ( c.x +1 < mapWidth ) ? true : false ;
  }

  private boolean cellTValid (Cell c){
    return (c.y -1 >= 0 ) ? true :false;
  }

  private boolean cellBValid (Cell c){
    return (c.y +1 < mapHeight) ? true :false;
  }

  /**
   * Determine the length,width,shape, position, and direction
   * relative to the car of the supplied shape.
   * The width and length is the largest possible of the object
   * if it is irregular.
   */
  private SpaceAnalyserReturn calculateDimension(LinkedList<Cell> shape){

    ArrayList<Integer> rowHeight = new ArrayList<Integer>();
    ArrayList<Integer> colLength = new ArrayList<Integer>();
    ArrayList<Point> pointShape = new ArrayList<Point>();

    for(Cell c : shape){
      rowHeight.add(c.y);
      colLength.add(c.x);
      pointShape.add(new Point(c.x,c.y));
    }

    int rowMin = Collections.min(rowHeight);
    int rowMax = Collections.max(rowHeight);
    int colMin = Collections.min(colLength);
    int colMax = Collections.max(colLength);

    float width = Float.valueOf(colMax - colMin +1);
    float height = Float.valueOf(rowMax - rowMin +1);
    float posy = Float.valueOf(rowMin) + (Float.valueOf(rowMax-rowMin)/Float.valueOf(2));
    float posx = Float.valueOf(colMin) + (Float.valueOf(colMax-colMin)/Float.valueOf(2));
    // reverting to standard definition of bottom being a negative with respect to y cord
    Point2D.Float pos =  new Point2D.Float(posx,posy); 
    // Direction relative to the car
    Vector2 directionVec = new Vector2( (float)(pos.x -(mapHeight/2.0)),(float)((mapHeight/2.0))-pos.y );

    return new SpaceAnalyserReturn(pos,pointShape, width,height, directionVec.len(),directionVec);

  }

  /*
   * The cell class represents a cell within
   * the CombinedCell[][]. It facilitates easy
   * translation and identification of the
   * cells within the map.
   *
   * 0,0 is the top left corner of the map
   * x is positive to the right.
   * y is positive to the bottom.
   */
  private class Cell{

    // x are map columns
    private final int x;
    // y are map rows
    private final int y;

    public Cell(int y,int x){
      this.x = x;
      this.y = y;
    }

    public Cell(int[] a){
      this.x = a[0];
      this.y = a[1];
    }

    public Cell left (){
      return new Cell(y,x-1);
    }

    public Cell right(){
      return new Cell(y,x+1);
    }

    public Cell top(){
      return new Cell(y-1,x);
    }

    public Cell bot(){
      return new Cell(y+1,x);
    }

    public String toString(){
      return String.valueOf(x)+","+String.valueOf(y);
    }

    @Override
    public int hashCode(){
      return toString().hashCode();
    }

    @Override 
    public boolean equals(Object aThat) {

    if (aThat == null) return false;

    // Check for self-comparison
    if ( this == aThat ) return true;

    // Ensure class is of correcty type
    if ( !(aThat instanceof Cell) ) return false;

    //cast to native object is now safe
    Cell that = (Cell) aThat;

    return ( this.x == that.x && this.y == that.y);
    }

  }

}
