package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInRealToInteger extends functionNode {

  public builtInRealToInteger(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    if (parameters.size() != 2){
      throw new IllegalArgumentException();
    }
    if (((parameters.get(0) instanceof realDataType)) & ((parameters.get(1) instanceof integerDataType))){
      Integer newNum = Math.round(((realDataType)parameters.get(0)).getValue());
      ((integerDataType) parameters.get(1)).setValue(newNum);
    }
    else{
      throw new IllegalArgumentException("Incorrect paramter type for real to integer" + parameters.toString());
    }
  }

  public boolean isBuiltin(){
    return true;
  }
}
