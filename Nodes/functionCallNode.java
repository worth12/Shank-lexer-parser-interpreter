package Nodes;
import java.util.List;

public class functionCallNode extends Node{

  private String name;
  List<parameterNode> parameters;

  public functionCallNode(String name, List<parameterNode> parameters){
    this.name = name;
    this.parameters = parameters;
  }

  public String getName(){
    return name;
  }

  public List<parameterNode> getParameters(){
    return parameters;
  }

  public String toString() {
    return "functionCallNode(name(" + name + "), parameters(" + parameters.toString() + ") )";
  }
  
}
