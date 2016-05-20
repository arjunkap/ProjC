package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyserReturn;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

public class SimpleSpaceAnalyser implements SpaceAnalyser{

  HashMap<String,Boolean> includedCells= new HashMap<String,Boolean>();
  CombinedCell[][] map = null;

  public ArrayList<SpaceAnalyserReturn> getObjects(CombinedCell[][] map){

    this.map = map;
    includedCells = new HashMap<String,Boolean>();

    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){

        Cell cell = new Cell(i,j);
        
        if( map[i][j].space && !cellInIncluded(cell)){

          LinkedList<Cell> toLookList = new LinkedList<Cell>();
          LinkedList<Cell> shape = new LinkedList<Cell>();
          HashMap<String,Boolean> lookedThisObject = new HashMap<String,Boolean>();
          toLookList.push(cell);
          includedCells.put(toKey(i,j),true);
          lookedThisObject.put(cell.toString(),true);

          while(toLookList.size() > 0){
            //System.out.println(toLookList.size());
            shape.push(toLookList.peek());
            searchAround(toLookList,lookedThisObject);
          }

          System.out.println("Shape Found!");
          Rectangle s  = new Rectangle();
          for(Cell c : shape){
            System.out.print("("+c +")"+ " ");
            s.add(c.x,c.y);
          }
          System.out.println(s);
          System.out.println(calculateDimension(shape));
          System.out.println("\n-------------");

        }
      }
    }

    return new ArrayList<SpaceAnalyserReturn>();
  }

  private void searchAround(LinkedList<Cell> toLookList,HashMap<String,Boolean> lookedThisObject){

    Cell cell =  toLookList.pop();

    if( cellLValid(cell) && !cellInIncluded(cell.left()) && !cellInMap(cell.left(),lookedThisObject)  ){
      lookedThisObject.put(cell.left().toString(),true);
      if (map[cell.left().x][cell.left().y].space){
        toLookList.push(cell.left());
        addToIncludedCells(cell.left());
      }
    }

    if( cellRValid(cell) && !cellInIncluded(cell.right()) && !cellInMap(cell.right(),lookedThisObject) ){ 
      lookedThisObject.put(cell.right().toString(),true);
      if (map[cell.right().x][cell.right().y].space){
        toLookList.push(cell.right());
        addToIncludedCells(cell.right());
      }
    }

    if( cellTValid(cell) && !cellInIncluded(cell.top()) && !cellInMap(cell.top(),lookedThisObject) ){
      lookedThisObject.put(cell.top().toString(),true);
      if (map[cell.top().x][cell.top().y].space){
        toLookList.push(cell.top());
        addToIncludedCells(cell.top());
      }
    }

    if( cellBValid(cell) && !cellInIncluded(cell.bot()) && !cellInMap(cell.bot(),lookedThisObject) ){
      lookedThisObject.put(cell.bot().toString(),true);
      if (map[cell.bot().x][cell.bot().y].space){
        toLookList.push(cell.bot());
        addToIncludedCells(cell.bot());
      }
    }

  }

  private void addToIncludedCells(Cell c){
    includedCells.put(c.toString(),true);
  }

  private boolean cellInMap(Cell c,HashMap<String,Boolean> map){
    return map.containsKey(c.toString());
  }

  private boolean cellInIncluded(Cell c){
    return includedCells.containsKey(c.toString());
  }

  private String toKey(int i,int j){
    return String.valueOf(i) + "," + String.valueOf(j);
  }

  private int[] fromKey(String key){
    String[] s = key.split(",");
    int[] ret = new int[2];
    ret[0] = Integer.valueOf(s[0]);
    ret[1] = Integer.valueOf(s[1]);
    return ret;
  }

  private void printSearchCells(){
    for(String s : includedCells.keySet() ){
      System.out.print(s);
      System.out.print(" ");
    }
  }

  public boolean cellLValid (Cell c){
    return (c.y - 1 >= 0 ) ? true : false;
  }

  public boolean cellRValid (Cell c){
    return ( c.y +1 < map[0].length ) ? true : false ;
  }

  public boolean cellTValid (Cell c){
    return (c.x -1 >= 0 ) ? true :false;
  }

  public boolean cellBValid (Cell c){
    return (c.x +1 < map.length ) ? true :false;
  }

  private ObjectProperties calculateDimension(LinkedList<Cell> shape){

    ArrayList<Integer> rowHeight = new ArrayList<Integer>();
    ArrayList<Integer> colLength = new ArrayList<Integer>();

    for(Cell c : shape){
      rowHeight.add(c.x);
      colLength.add(c.y);
    }

    int rowMin = Collections.min(rowHeight);
    int rowMax = Collections.max(rowHeight);
    int colMin = Collections.min(colLength);
    int colMax = Collections.max(colLength);

    return new ObjectProperties(colMax - colMin +1,
                                rowMax - rowMin +1,
                         new Point2D.Float(Float.valueOf(rowMin) + (Float.valueOf(rowMax-rowMin)/Float.valueOf(2)), 
                           Float.valueOf(colMin) + (Float.valueOf(colMax-colMin)/Float.valueOf(2))));
  }

  private class Cell{
    // x are map rows
    public final int x;
    // y are map columns
    public final int y;

    public Cell(int x,int y){
      this.x = x;
      this.y = y;
    }

    public Cell(int[] a){
      this.x = a[0];
      this.y = a[1];
    }

    public Cell left (){
      return new Cell(x,y-1);
    }

    public Cell right(){
      return new Cell(x,y+1);
    }

    public Cell top(){
      return new Cell(x-1,y);
    }

    public Cell bot(){
      return new Cell(x+1,y);
    }

    public String toString(){
      return String.valueOf(x)+","+String.valueOf(y);
    }

  }

  private class ObjectProperties{

    public final int width;
    public final int height;
    public final  Point2D.Float pos;

    public ObjectProperties(int width, int height,Point2D.Float pos){
      this.height = height;
      this.width = width;
      this.pos = pos;
    }

    public String toString(){
      return "Width : " + String.valueOf(width) + " Height : " + String.valueOf(height) + " Position : " + pos.toString() ;

    }

  }

}
