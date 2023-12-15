package Nodes;
public class parameterNode extends Node {

  private variableReferenceNode variableReference;
  private Node value;

  public parameterNode(variableReferenceNode variableReference, Node value){
    this.variableReference = variableReference;
    this.value = value;
  }

  public variableReferenceNode getVariableRef(){
    return variableReference;
  }

  public Node getValue(){
    return value;
  }

  public String toString() {
    if (this.variableReference == null){
      return "ParameterNode(" + value.toString() + ")";
    }
    return "ParameterNode(" + variableReference.toString() + ")";
  }
  
}
