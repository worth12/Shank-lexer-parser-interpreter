package Nodes;
public class characterNode extends Node {

  private char value;

  public characterNode(char value){
    this.value = value;
  }

  @Override
  public String toString() {
    return "character(" + this.value + ")";
  }

  public char getValue() { // returns value of char node
    return value;
  }
  
}
