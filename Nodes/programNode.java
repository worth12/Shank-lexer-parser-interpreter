package Nodes;
import java.util.HashMap;
import java.util.function.Function;

public class programNode extends Node {// extends node abstract class

  private HashMap<String, functionNode> program; // value of the int

  public programNode() {
    this.program = new HashMap<String, functionNode>();
  }

  public void addFunction(functionNode function){
    this.program.put(function.getName(), function);
  }

  
  public functionNode getFunction(String name) { // returns value of int node
    return this.program.get(name);
  }

  public HashMap<String, functionNode> getFunctions(){
    return this.program;
  }
  
  
  public String toString() { // overriden tostring to print node
    return "";
  }
  
}
