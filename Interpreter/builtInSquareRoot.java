package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInSquareRoot extends functionNode {

  public builtInSquareRoot(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    if (parameters.size() != 2){
      throw new IllegalArgumentException();
    }
    if ((parameters.get(0) instanceof integerDataType)){
      float newNum = (float)Math.sqrt(((integerDataType)parameters.get(0)).getValue());
      ((realDataType) parameters.get(1)).setValue(newNum);
    }
    else if ((parameters.get(0) instanceof realDataType)){
      float newNum = (float)Math.sqrt(((realDataType)parameters.get(0)).getValue());
      ((realDataType) parameters.get(1)).setValue(newNum);
    }
    throw new IllegalArgumentException("Incorrect paramter type for squareRoot" + parameters.toString());
  }

  public boolean isBuiltin(){
    return true;
  }
}
