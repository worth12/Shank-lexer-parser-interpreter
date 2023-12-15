package Interpreter;
import java.util.List;
import java.util.Scanner;

import Nodes.functionNode;
import Nodes.statementNode;
import Nodes.variableNode;

public class builtInRead extends functionNode {
  
  public builtInRead(String name, List<variableNode> parameters, List<variableNode> constants_variables,
  List<statementNode> statements){
    super(name, parameters, constants_variables, statements);
  }


  public void execute(List<interpreterDataType> parameters){
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object Output user input
    for (int i = 0; i < parameters.size(); i++){
      String input;
      if (myObj.hasNextLine()){
        input = myObj.nextLine();
      }
      else {
        input = "Not available";
      }
      parameters.get(i).fromString(input);
    }
  }

  public boolean isVariadic(){
    return true;
  }

  public boolean isBuiltin(){
    return true;
  }
}
