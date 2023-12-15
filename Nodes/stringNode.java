package Nodes;
public class stringNode extends Node {// extends node abstract class

  private String value; // value of the string

  public stringNode(String value) { // constructor for string node
    this.value = value;
  }

  public String toString() { // overriden tostring to print node
    return "String(" + this.value + ")";
  }

  public String getValue() { // returns value of string node
    return value;
  }
  
}
