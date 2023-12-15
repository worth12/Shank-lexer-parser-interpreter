package Nodes;
public class booleanNode extends Node {

  private boolean value;

  public booleanNode(boolean value) {
    this.value = value;
  }

  public booleanNode(String value) {
    if (value.equals("false")){
      this.value = false;
    } else {
      this.value = true;
    }
  }

  public String toString() {
    return "boolean(" + this.value + ")";
  }

  public boolean getValue() { // returns value of boolean node
    return value;
  }
  
}
