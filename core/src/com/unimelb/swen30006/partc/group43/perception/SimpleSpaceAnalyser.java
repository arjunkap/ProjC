package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.KinematicAccess;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;
import com.unimelb.swen30006.partc.group43.perception.SpaceAnalyserReturn;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SimpleSpaceAnalyser implements SpaceAnalyser{

  HashMap<String,Boolean> searchedCells = new HashMap<String,Boolean>();

  CombinedCell[][] map = null;

  public ArrayList<SpaceAnalyserReturn> getObjects(CombinedCell[][] map){

    this.map = map;

    for(int i = 0 ; i < map.length ; i++){
      for(int j = 0 ; j < map[0].length ; j ++){
        
        if( map[i][j].space ){
          LinkedList<String> toLookList = new LinkedList<String>();
          toLookList.push(toKey(i,j));
          searchedCells.put(toKey(i,j),true);

          searchAround(searchedCells,toLookList);

        }
      }
    }

    return new ArrayList<SpaceAnalyserReturn>();
  }

  private void searchAround(HashMap<String,Boolean> searchedCells,LinkedList<String> toLookList){

    String cellString = toLookList.pop();
    Cell cell = new Cell(fromKey(cellString));

    if( cellLValid(cell) ){
      if (map[cell.left().x][cell.left().y].space){
      }
    }

    if( cellRValid(cell) ){
      if (map[cell.right().x][cell.right().y].space){
      }
    }

    if( cellTValid(cell) ){
      if (map[cell.top().x][cell.top().y].space){
      }
    }

    if( cellBValid(cell) ){
      if (map[cell.bot().x][cell.bot().y].space){

      }
    }

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

  private class Cell{
    public final int x;
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
      return String.valueOf(x)+"-"+String.valueOf(y);
    }

  }

}
