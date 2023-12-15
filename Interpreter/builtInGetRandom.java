package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInGetRandom extends functionNode {

  public builtInGetRandom(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    if (parameters.size() != 1){
      throw new IllegalArgumentException();
    }
    if ((parameters.get(0) instanceof integerDataType)){
      ((integerDataType)parameters.get(0)).setValue((int)Math.random());
    }
    else{
      throw new IllegalArgumentException("Incorrect paramter type for get random" + parameters.toString());
    }
  }

  public boolean isBuiltin(){
    return true;
  }
}