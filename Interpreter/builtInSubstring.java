package Interpreter;
import java.lang.reflect.Parameter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInSubstring extends functionNode {

  public builtInSubstring(String name, List<variableNode> parameters, List<variableNode> constants_variables,
      List<statementNode> statements) {
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    checkParameters(parameters);
    String oldString = ((stringDataType)parameters.get(0)).getValue();
    int index = ((integerDataType)parameters.get(1)).getValue();
    int length = ((integerDataType)parameters.get(2)).getValue();
    String newString = oldString.substring(index - 1, index + length - 1);
    ((stringDataType)parameters.get(3)).setValue(newString);
  }

  private void checkParameters(List<interpreterDataType> parameters){
    if (parameters.size() != 4){
      throw new IllegalArgumentException("Incorrect number of parameters.");
    }
    if(!(parameters.get(0) instanceof stringDataType)){
      throw new IllegalArgumentException("Expected parameter of type String");
    }

    if(!(parameters.get(1) instanceof integerDataType)){
      throw new IllegalArgumentException("Expected parameter of type Integer");
    }

    if(!(parameters.get(2) instanceof integerDataType)){
      throw new IllegalArgumentException("Expected parameter of type Integer");
    }

    if(!(parameters.get(3) instanceof stringDataType)){
      throw new IllegalArgumentException("Expected parameter of type String");
    }

  }

  public boolean isBuiltin(){
    return true;
  }
}