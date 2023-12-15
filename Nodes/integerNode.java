package Nodes;
public class integerNode extends Node {// extends node abstract class

  private int value; // value of the int

  public integerNode(int value) { // constructor for int node
    this.value = value;
  }

  public String toString() { // overriden tostring to print node
    return "integer(" + this.value + ")";
  }

  public int getValue() { // returns value of int node
    return value;
  }
  
}
