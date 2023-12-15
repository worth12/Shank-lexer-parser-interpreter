package Nodes;
import java.util.List;

import Interpreter.interpreterDataType;

public class functionNode extends Node {

  private String name;
  private List<variableNode> parameters;
  private List<variableNode> constants_variables;
  private List<statementNode> statements;

  public functionNode(String name, List<variableNode> parameters, List<variableNode> constants_variables,
      List<statementNode> statements) {
    // node for defining functions and storing the internal statements
    this.name = name;
    this.parameters = parameters;
    this.constants_variables = constants_variables;
    this.statements = statements;
  }



  @Override
  public String toString() { // returns string of a function, able to print indefinite params, etc.
    StringBuilder paramString = new StringBuilder();
    StringBuilder varString = new StringBuilder();
    StringBuilder constString = new StringBuilder();
    StringBuilder statementString = new StringBuilder();
    for (variableNode parameter : this.parameters) {
      paramString.append(parameter.getName() + ",");
    }
    if (paramString.length() > 0) {
      paramString.deleteCharAt(paramString.length() - 1);
    }
    for (variableNode constant_var : this.constants_variables) {
      if (constant_var.isChangeable()) {
        if (constant_var.getFrom() != null) {
        varString.append(constant_var.getName() + "(range:" + constant_var.getFrom().toString() + "-" + constant_var.getTo().toString() + ")");

        }
        varString.append(constant_var.getName() + ",");
      } else {
        constString.append(constant_var.getName() + ",");

      }
    }
    if (constString.length() > 0) {
      constString.deleteCharAt(constString.length() - 1);
    }
    if (varString.length() > 0) {
      varString.deleteCharAt(varString.length() - 1);
    }

    for (Node statement : statements) {
      statementString.append(statement.toString() + ",");
    }
    if (statementString.length() > 0) {
      statementString.deleteCharAt(statementString.length() - 1);
    }

    return "function(name(" + this.name + "), parameters(" + paramString + "), constants(" + constString
        + "), variables(" + varString + "), statements(" + statementString + "))";
  }

  public List<variableNode> getParameters() {
    return this.parameters;
  }

  public List<variableNode> getConstants_variables() {
    return this.constants_variables;
  }

  public List<statementNode> getStatements() {
    return this.statements;
  }

  public String getName() {
    return this.name;
  }

  public boolean isVariadic() {
    return false;
  }

  public boolean isBuiltin(){
    return false;
  }

  public void execute(List<interpreterDataType> parameters) {
  }

}
