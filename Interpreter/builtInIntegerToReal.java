package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInIntegerToReal extends functionNode {

  public builtInIntegerToReal(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    if (parameters.size() != 2){
      throw new IllegalArgumentException();
    }
    if (((parameters.get(0) instanceof integerDataType)) & ((parameters.get(1) instanceof realDataType))){
      float newNum = (float)(((integerDataType) parameters.get(0)).getValue());
      ((realDataType) parameters.get(1)).setValue(newNum);
    }
    else{
      throw new IllegalArgumentException("Incorrect paramter type for InIntegerToReal" + parameters.toString());
    }
  }

  public boolean isBuiltin(){
    return true;
  }
}
