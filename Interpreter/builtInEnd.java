package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInEnd extends functionNode {

  public builtInEnd(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    if (parameters.size() != 2){
      throw new IllegalArgumentException();
    }
    if ((parameters.get(0) instanceof arrayDataType)){
      int index = 0;
      for(int i = ((arrayDataType)parameters.get(0)).getValue().size(); i >= 0; i--){
        if (((arrayDataType)parameters.get(0)).getValue().get(i) == null){
          index = i;
          break;
        }
      }
      ((integerDataType) parameters.get(1)).setValue(index);
    }
    throw new IllegalArgumentException("Incorrect paramter type for squareRoot" + parameters.toString());
  }

  public boolean isBuiltin(){
    return true;
  }
}