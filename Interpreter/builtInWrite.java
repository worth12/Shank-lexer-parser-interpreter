package Interpreter;
import java.util.List;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInWrite extends functionNode {
  
  public builtInWrite(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }

  public void execute(List<interpreterDataType> parameters){
    for (int i = 0; i < parameters.size(); i++){
      System.out.print(parameters.get(i).toString());
    }
    System.out.println();
  }

  public boolean isVariadic(){
    return true;
  }

  public boolean isBuiltin(){
    return true;
  }

}
