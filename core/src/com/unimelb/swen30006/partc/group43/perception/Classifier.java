package com.unimelb.swen30006.partc.group43.perception;
import com.unimelb.swen30006.partc.group43.perception.ClassifierAccess;
import com.unimelb.swen30006.partc.group43.perception.CombinedCell;

public interface Classifier{
  public void classify(ClassifierAccess mapObject,CombinedCell[][] map);
}
