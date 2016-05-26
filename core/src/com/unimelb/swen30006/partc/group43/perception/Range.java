package com.unimelb.swen30006.partc.group43.perception;

public class  Range<T extends Comparable<T>>{
  private T min;
  private T max;

  Range(T min,T max){
    this.min = min;
    this.max = max;
  }

  public boolean isInRange(T inspected){
    return ((inspected.compareTo(min) >=0 && inspected.compareTo(max) <= 0 ) ? true : false);
  }

}
