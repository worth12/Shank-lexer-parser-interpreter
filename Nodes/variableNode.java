package Nodes;
public class variableNode<T> extends Node {// extends node abstract class

  private T value; // generic value object
  private String name; // name of the variable
  private boolean changeable; // whether the variable is changeable
  private String type;
  private T from;
  private T to;


  public variableNode(String name,String type, boolean changeable,T value ) { // constructor for int node
    this.value = value;
    this.name = name;
    this.changeable = changeable;
    this.type = type;
  }

  public variableNode(String name,String type, boolean changeable,T value, T from, T to) { // constructor for int node
    this.value = value;
    this.name = name;
    this.changeable = changeable;
    this.type = type;
    this.from = from;
    this.to = to;
  }

  public String toString() { // overriden tostring to print node
    return "Variable(" + this.name + "," + this.value.toString() + "," + this.changeable + ")";
  }

  public T getValue() { // returns value of int node
    return this.value;
  }

  public String getName() { // returns value of int node
    return this.name;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public String getType(){
    if (this.type == null){
      if (this.value instanceof Integer){
        this.type = "INTEGER";
      } else if (this.value instanceof Float){
        this.type = "REAL";
      } else if (this.value instanceof String){
        this.type = "STRING";
      } else if (this.value instanceof Character){
        this.type = "CHARACTER";
      } else if (this.value instanceof Boolean){
        this.type = "BOOLEAN";
      }
    }
    return this.type;
  }

  public boolean isChangeable(){
    return this.changeable;
  }

  public T getFrom(){
    return this.from;
  }

  public T getTo(){
    return this.to;
  }

}
