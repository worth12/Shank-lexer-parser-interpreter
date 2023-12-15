package Nodes;
public class realNode extends Node { // extends node abstract class

  private float value; // value of the real node

  public realNode(float value){ // constructor for the real node
    this.value = value;
  } 

  @Override
  public String toString() { // overriden tostring method to print node
    return "real(" +  value + ")";
  }

  public float getValue() { // returns the value of the real node
    return value;
  }
  
}
