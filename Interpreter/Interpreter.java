package Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Nodes.*;

public class Interpreter { // interprets program nodes of functions

  private HashMap<String, interpreterDataType> localVariables = new HashMap<String, interpreterDataType>(); // local
                                                                                                            // variables
                                                                                                            // hashmaps
  private HashMap<String, functionNode> functions = new HashMap<String, functionNode>();

  public Interpreter(HashMap<String, functionNode> builtInFunctions) {
    this.functions = builtInFunctions;
  }

  public Interpreter(programNode program, HashMap<String, functionNode> builtInFunctions) {
    this.functions = builtInFunctions;
    functions.putAll(program.getFunctions());
  }

  public List<interpreterDataType> interpretFunction(functionNode function, List<interpreterDataType> parameters) { // takes
                                                                                                                    // function
                                                                                                                    // node
    // and interprets
    functions.put(function.getName(), function);

    for (variableNode variable : function.getConstants_variables()) { // register each local vaariable into hashmap
      interpreterDataType interpreterVar = createIDT(variable);
      localVariables.put(variable.getName(), interpreterVar);
    }

    List<variableNode> parameterVars = function.getParameters();

    if (!function.isVariadic()) {
      if (function.getParameters().size() != ((function).getParameters()).size()) {
        throw new IllegalArgumentException(
            "Incorrect number of paramters for function" + ((function).getName()));
      }
    }
    int i = 0;
    for (interpreterDataType parameter : parameters) { // places each passed in parameter as local variable
      localVariables.put(parameterVars.get(i++).getName(), parameter);
    }

    // System.out.println("Local variables before block interpret:");
    // for (Map.Entry<String, interpreterDataType> entry :
    // localVariables.entrySet()) {
    // System.out.println(entry.getValue().toString());
    // }
    // System.out.println("");
    interpretBlock(function.getStatements()); // starts interpreting the statements
    // System.out.println("Local variables after block interpret:");
    // for (Map.Entry<String, interpreterDataType> entry :
    // localVariables.entrySet()) {
    // System.out.println(entry.getValue().toString());
    // }

    List<interpreterDataType> updatedParams = new ArrayList<>();
    int j = 0;
    for (variableNode parameter : function.getParameters()) { // places each passed in parameter as local variable
      if (parameter.isChangeable()) {
        updatedParams.add(localVariables.get(parameterVars.get(j).getName()));
      }
      j++;
    }
    return updatedParams;
  }

  public void interpretBlock(List<statementNode> statements) { // interprets block of statements
    for (statementNode statement : statements) {// mathOpNode
      if (statement.getValue() instanceof mathOpNode) {
        mathOpNode(statement.getValue());
      } else if (statement.getValue() instanceof booleanCompareNode) {
        booleanCompareNode(statement.getValue());// booleanCompareNode
      } else if (statement.getValue() instanceof assignmentNode) {
        assignmentNode(statement.getValue());// assignmentNode
      } else if (statement.getValue() instanceof ifNode) {
        ifNode(statement.getValue());// ifNode
      } else if (statement.getValue() instanceof forNode) {
        forNode(statement.getValue());// forNode
      } else if (statement.getValue() instanceof repeatNode) {
        repeatNode(statement.getValue());// repeatNode
      } else if (statement.getValue() instanceof whileNode) {
        whileNode(statement.getValue());// whileNode
      } else if (statement.getValue() instanceof functionCallNode) {
        functionCallNode(statement.getValue());
      }
    }
  }

  private interpreterDataType mathOpNode(Node node) { // interprets mathopnodes and returns IDT with the value
    Node left = expression(((mathOpNode) node).getLeftNode());
    Node right = expression(((mathOpNode) node).getRightNode());
    mathOpNode.operationType operation = ((mathOpNode) node).getOperationType();
    if (left instanceof integerNode & right instanceof integerNode) {
      int value = 0;
      switch (operation) {
        case PLUS:
          value = ((integerNode) left).getValue() + ((integerNode) right).getValue();
          break;
        case MINUS:
          value = ((integerNode) left).getValue() - ((integerNode) right).getValue();
          break;
        case TIMES:
          value = ((integerNode) left).getValue() * ((integerNode) right).getValue();
          break;
        case DIVIDE:
          value = ((integerNode) left).getValue() / ((integerNode) right).getValue();
          break;
        case MOD:
          value = ((integerNode) left).getValue() % ((integerNode) right).getValue();
          break;
      }
      return new integerDataType(value);
    } else if (left instanceof realNode & right instanceof realNode) {
      float value = 0;
      switch (operation) {
        case PLUS:
          value = ((realNode) left).getValue() + ((realNode) right).getValue();
          break;
        case MINUS:
          value = ((realNode) left).getValue() - ((realNode) right).getValue();
          break;
        case TIMES:
          value = ((realNode) left).getValue() * ((realNode) right).getValue();
          break;
        case DIVIDE:
          value = ((realNode) left).getValue() / ((realNode) right).getValue();
          break;
        case MOD:
          value = ((realNode) left).getValue() % ((realNode) right).getValue();
          break;
      }
      return new realDataType(value);
    } else if (left instanceof stringNode & right instanceof stringNode) {
      String value = "";
      switch (operation) {
        case PLUS:
          value = ((stringNode) left).getValue() + ((stringNode) right).getValue();
          break;
        default:
          throw new IllegalArgumentException("Only supported operation for String is addidtion.");
      }
      return new stringDataType(value);
    }
    throw new IllegalArgumentException("Operations may only be on same typed values.");
  }

  private Node mathOpNodeRec(Node node) {// interprets mathopnodes and returns node with the value
    Node left = expression(((mathOpNode) node).getLeftNode());
    Node right = expression(((mathOpNode) node).getRightNode());
    mathOpNode.operationType operation = ((mathOpNode) node).getOperationType();

    if (left instanceof integerNode & right instanceof integerNode) {
      int value = 0;
      switch (operation) {
        case PLUS:
          value = ((integerNode) left).getValue() + ((integerNode) right).getValue();
          break;
        case MINUS:
          value = ((integerNode) left).getValue() - ((integerNode) right).getValue();
          break;
        case TIMES:
          value = ((integerNode) left).getValue() * ((integerNode) right).getValue();
          break;
        case DIVIDE:
          value = ((integerNode) left).getValue() / ((integerNode) right).getValue();
          break;
        case MOD:
          value = ((integerNode) left).getValue() % ((integerNode) right).getValue();
          break;
      }
      return new integerNode(value);
    } else if (left instanceof realNode & right instanceof realNode) {
      float value = 0;
      switch (operation) {
        case PLUS:
          value = ((realNode) left).getValue() + ((realNode) right).getValue();
          break;
        case MINUS:
          value = ((realNode) left).getValue() - ((realNode) right).getValue();
          break;
        case TIMES:
          value = ((realNode) left).getValue() * ((realNode) right).getValue();
          break;
        case DIVIDE:
          value = ((realNode) left).getValue() / ((realNode) right).getValue();
          break;
        case MOD:
          value = ((realNode) left).getValue() % ((realNode) right).getValue();
          break;
      }
      return new realNode(value);
    } else if (left instanceof stringNode & right instanceof stringNode) {
      String value = "";
      switch (operation) {
        case PLUS:
          value = ((stringNode) left).getValue() + ((stringNode) right).getValue();
          break;
        default:
          throw new IllegalArgumentException("Only supported operation for String is addidtion.");
      }
      return new stringNode(value);
    }
    throw new IllegalArgumentException("Operations may only be on same typed values.");
  }

  private boolean booleanCompareNode(Node node) { // evaluates boolean comparisons and returns the result
    Node left = expression(((booleanCompareNode) node).getLeftNode());
    Node right = expression(((booleanCompareNode) node).getRightNode());
    booleanCompareNode.NodeType operation = ((booleanCompareNode) node).getNodeType();
    if (left instanceof booleanNode && right instanceof booleanCompareNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case OR:
          return ((booleanNode) left).getValue() | booleanCompareNode(right);
        case AND:
          return ((booleanNode) left).getValue() & booleanCompareNode(right);
        case EQUAL:
          return ((booleanNode) left).getValue() == booleanCompareNode(right);
        case NOTEQUALS:
          return ((booleanNode) left).getValue() != booleanCompareNode(right);
        default:
          throw new IllegalArgumentException("Invalid boolean comparison operation");
      }
    } else if (left instanceof booleanCompareNode && right instanceof booleanNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case OR:
          return booleanCompareNode(left) | ((booleanNode) right).getValue();
        case AND:
          return booleanCompareNode(left) & ((booleanNode) right).getValue();
        case EQUAL:
          return booleanCompareNode(left) == ((booleanNode) right).getValue();
        case NOTEQUALS:
          return booleanCompareNode(left) != ((booleanNode) right).getValue();
        default:
          throw new IllegalArgumentException("Invalid boolean comparison operation");
      }
    } else if (left instanceof booleanCompareNode && right instanceof booleanCompareNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case OR:
          return booleanCompareNode(left) | booleanCompareNode(right);
        case AND:
          return booleanCompareNode(left) & booleanCompareNode(right);
        case EQUAL:
          return booleanCompareNode(left) == booleanCompareNode(right);
        case NOTEQUALS:
          return booleanCompareNode(left) != booleanCompareNode(right);
        default:
          throw new IllegalArgumentException("Invalid boolean comparison operation");
      }
    } else if (left instanceof booleanNode && right instanceof booleanNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case OR:
          return ((booleanNode) left).getValue() | ((booleanNode) right).getValue();
        case AND:
          return ((booleanNode) left).getValue() & ((booleanNode) right).getValue();
        case EQUAL:
          return ((booleanNode) left).getValue() == ((booleanNode) right).getValue();
        case NOTEQUALS:
          return ((booleanNode) left).getValue() != ((booleanNode) right).getValue();
        default:
          throw new IllegalArgumentException("Invalid boolean comparison operation");
      }
    } else if (left instanceof integerNode && right instanceof integerNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case LESSTHAN:
          return ((integerNode) left).getValue() < ((integerNode) right).getValue();
        case LESSTHANEQUAL:
          return ((integerNode) left).getValue() <= ((integerNode) right).getValue();
        case GREATERTHAN:
          return ((integerNode) left).getValue() > ((integerNode) right).getValue();
        case GREATERTHANEQUAL:
          return ((integerNode) left).getValue() >= ((integerNode) right).getValue();
        case EQUAL:
          return ((integerNode) left).getValue() == ((integerNode) right).getValue();
        case NOTEQUALS:
          return ((integerNode) left).getValue() != ((integerNode) right).getValue();
        default:
          throw new IllegalArgumentException("Invalid integer comparison operation");
      }
    } else if (left instanceof realNode && right instanceof realNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case LESSTHAN:
          return ((realNode) left).getValue() < ((realNode) right).getValue();
        case LESSTHANEQUAL:
          return ((realNode) left).getValue() <= ((realNode) right).getValue();
        case GREATERTHAN:
          return ((realNode) left).getValue() > ((realNode) right).getValue();
        case GREATERTHANEQUAL:
          return ((realNode) left).getValue() >= ((realNode) right).getValue();
        case EQUAL:
          return ((realNode) left).getValue() == ((realNode) right).getValue();
        case NOTEQUALS:
          return ((realNode) left).getValue() != ((realNode) right).getValue();
        default:
          throw new IllegalArgumentException("Invalid real number comparison operation");
      }
    } else if (left instanceof stringNode && right instanceof stringNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case EQUAL:
          return ((stringNode) left).getValue().equals(((stringNode) right).getValue());
        case NOTEQUALS:
          return !((stringNode) left).getValue().equals(((stringNode) right).getValue());
        default:
          throw new IllegalArgumentException("Invalid string comparison operation");
      }
    } else if (left instanceof characterNode && right instanceof characterNode) {
      switch (operation) { // OR, AND, LESSTHAN, LESSTHANEQUAL, GREATERTHAN, GREATERTHANEQUAL, EQUAL,
                           // NOTEQUALS
        case EQUAL:
          return ((characterNode) left).getValue() == ((characterNode) right).getValue();
        case NOTEQUALS:
          return ((characterNode) left).getValue() != ((characterNode) right).getValue();
        default:
          throw new IllegalArgumentException("Invalid character comparison operation");
      }
    }
    throw new IllegalArgumentException("Invalid comparison, both values must be of the same type");
  }

  private void ifNode(Node node) { // interprets if nodes, interpreting the apropriate statements
    if (((ifNode) node).getCondition() == null) {
      interpretBlock(((ifNode) node).getStatements());
      return;
    }
    boolean condition = booleanCompareNode(((ifNode) node).getCondition());
    if (condition) {
      interpretBlock(((ifNode) node).getStatements());
    } else {
      if (((ifNode) node).getElseNode() != null) {
        ifNode(((ifNode) node).getElseNode());
      }
    }
  }

  private void whileNode(Node node) { // interprets while nodes, interpreting the apropriate statements
    boolean condition = booleanCompareNode(((whileNode) node).getCondition());
    while (condition) {
      interpretBlock(((whileNode) node).getStatements());
      booleanCompareNode oldCondition = (booleanCompareNode) ((whileNode) node).getCondition();
      ((whileNode) node).updateCondition(
          new booleanCompareNode(oldCondition.getNodeType(), oldCondition.getLeftNode(), oldCondition.getRightNode()));
      condition = booleanCompareNode(((whileNode) node).getCondition());
    }
  }

  private void repeatNode(Node node) { // interprets repeat nodes, interpreting the apropriate statements
    boolean condition = booleanCompareNode(((repeatNode) node).getCondition());
    while (!condition) {
      interpretBlock(((repeatNode) node).getStatements());
      condition = booleanCompareNode(((repeatNode) node).getCondition());
    }
  }

  private void forNode(Node node) { // interprets for nodes, interpreting the apropriate statements
    variableReferenceNode var = ((forNode) node).getVariableInt();
    if (!(variableReferenceNode(var) instanceof integerDataType)) {
      throw new IllegalArgumentException("For loop variable must be an integer");
    }
    int from;
    int to;

    // handle from and to
    if (expression(((forNode) node).getFrom()) instanceof variableReferenceNode) {
      if (variableReferenceNode(expression(((forNode) node).getFrom())) instanceof integerDataType) {
        from = ((integerDataType) variableReferenceNode(expression(((forNode) node).getFrom()))).getValue();
      } else {
        throw new IllegalArgumentException("For loop must be from integer to integer");
      }
    } else if (expression(((forNode) node).getFrom()) instanceof integerNode) {
      from = ((integerNode) expression(((forNode) node).getFrom())).getValue();
    } else {
      throw new IllegalArgumentException("For loop must be from integer to integer");
    }
    if (expression(((forNode) node).getTo()) instanceof variableReferenceNode) {
      if (variableReferenceNode(expression(((forNode) node).getTo())) instanceof integerDataType) {
        to = ((integerDataType) variableReferenceNode(expression(((forNode) node).getTo()))).getValue();
      } else {
        throw new IllegalArgumentException("For loop must be from integer to integer");
      }
    } else if (expression(((forNode) node).getTo()) instanceof integerNode) {
      to = ((integerNode) expression(((forNode) node).getTo())).getValue();
    } else {
      throw new IllegalArgumentException("For loop must be from integer to integer");
    }

    // counter saved as local variable
    localVariables.put(((variableReferenceNode) var).getName(), new integerDataType(from));

    while (((integerDataType) localVariables.get(((variableReferenceNode) var).getName())).getValue() >= from
        && ((integerDataType) localVariables.get(((variableReferenceNode) var).getName())).getValue() <= to) {
      interpretBlock(((forNode) node).getStatements());
      int tempIndex = ((integerDataType) localVariables.get(((variableReferenceNode) var).getName())).getValue();
      localVariables.put(((variableReferenceNode) var).getName(), new integerDataType(tempIndex + 1));
    }
    localVariables.remove(((variableReferenceNode) var).getName());
  }

  private void assignmentNode(Node node) { // interprets assighnments, storing the new values in the local variables
    // hashmap
    interpreterDataType var = variableReferenceNode(((assignmentNode) node).getVariable());
    Node value = ((assignmentNode) node).getValue();
    interpreterDataType varRefVal;
    boolean changeable = localVariables.get(((assignmentNode) node).getVariable().getName()).isChangeable();
    if (value instanceof mathOpNode) {
      value = mathOpNodeRec(value);
    }
    if (value instanceof variableReferenceNode) {
      varRefVal = variableReferenceNode((variableReferenceNode) ((assignmentNode) node).getValue());
      if (localVariables.get(((assignmentNode) node).getVariable().getName()).isChangeable()) {
        varRefVal.setChangable(true);
      } else {
        varRefVal.setChangable(false);
      }
      if (var instanceof integerDataType && varRefVal instanceof integerDataType) {
        localVariables.put(((assignmentNode) node).getVariable().getName(), varRefVal);
      } else if (var instanceof realDataType && varRefVal instanceof realDataType) {
        localVariables.put(((assignmentNode) node).getVariable().getName(), varRefVal);
      } else if (var instanceof stringDataType && varRefVal instanceof stringDataType) {
        localVariables.put(((assignmentNode) node).getVariable().getName(), varRefVal);
      } else if (var instanceof characterDataType && varRefVal instanceof characterDataType) {
        localVariables.put(((assignmentNode) node).getVariable().getName(), varRefVal);
      } else if (var instanceof booleanDataType && varRefVal instanceof booleanDataType) {
        localVariables.put(((assignmentNode) node).getVariable().getName(), varRefVal);
      } else {
        throw new IllegalArgumentException("Cannot assign value of a different type to original assignment.");
      }
    } else if (var instanceof integerDataType && value instanceof integerNode) {
      localVariables.put(((assignmentNode) node).getVariable().getName(),
          new integerDataType(((integerNode) value).getValue(), changeable));
    } else if (var instanceof realDataType && value instanceof realNode) {
      localVariables.put(((assignmentNode) node).getVariable().getName(),
          new realDataType(((realNode) value).getValue(), changeable));
    } else if (var instanceof stringDataType && value instanceof stringNode) {
      localVariables.put(((assignmentNode) node).getVariable().getName(),
          new stringDataType(((stringNode) value).getValue(), changeable));
    } else if (var instanceof characterDataType && value instanceof characterNode) {
      localVariables.put(((assignmentNode) node).getVariable().getName(),
          new characterDataType(((characterNode) value).getValue(), changeable));
    } else if (var instanceof booleanDataType && value instanceof booleanNode) {
      localVariables.put(((assignmentNode) node).getVariable().getName(),
          new booleanDataType(((booleanNode) value).getValue(), changeable));
    } else {
      throw new IllegalArgumentException("Cannot assign value of a different type to original assignment.");
    }
  }

  private void functionCallNode(Node node) {
    if (!functions.containsKey(((functionCallNode) node).getName())) {
      throw new IllegalArgumentException("Function not defined: " + (((functionCallNode) node).getName()));
    }
    functionNode function = functions.get(((functionCallNode) node).getName());
    if (!function.isVariadic()) {
      if (function.getParameters().size() != (((functionCallNode) node).getParameters()).size()) {
        throw new IllegalArgumentException(
            "Incorrect number of paramters for function" + (((functionCallNode) node).getName()));
      }
    }

    List<interpreterDataType> values = new ArrayList<>();

    for (parameterNode parameter : ((functionCallNode) node).getParameters()) {
      if (parameter.getValue() instanceof variableReferenceNode) {
        values.add(variableReferenceNode(parameter.getValue()));
      } else if (parameter.getVariableRef() == null) {
        Node value = expression(parameter.getValue());
        if (value instanceof integerNode) {
          values.add(new integerDataType(((integerNode) value).getValue()));
        } else if (value instanceof realNode) {
          values.add(new realDataType(((realNode) value).getValue()));
        } else if (value instanceof stringNode) {
          values.add(new stringDataType(((stringNode) value).getValue()));
        } else if (value instanceof booleanNode) {
          values.add(new booleanDataType(((booleanNode) value).getValue()));
        }
      } else {
        values.add(variableReferenceNode(parameter.getVariableRef()));
      }
    }

    List<interpreterDataType> updatedParams = new ArrayList<>();

    if (function.isBuiltin()) {
      function.execute(values);
    } else {
      Interpreter newInterpreter = new Interpreter(functions);
      updatedParams = newInterpreter.interpretFunction(function, values);
    }
    int i = 0;
    int j = 0;
    for (interpreterDataType value : values) {
      if (value.isChangeable()) {
        if (((functionCallNode) node).getParameters().get(i).getValue() != null) {
          if (function.isVariadic()) {
            localVariables.put(
                ((variableReferenceNode) ((functionCallNode) node).getParameters().get(i).getValue()).getName(),
                value);
          } else {
            if (function.getParameters().get(i).isChangeable()) {
              localVariables.put(
                  ((variableReferenceNode) ((functionCallNode) node).getParameters().get(i).getValue()).getName(),
                  updatedParams.get(j++));
            }
          }
        }
      }
      i++;
    }

  }

  private Node expression(Node node) { // interprets expressions of integer, real, and string, returning the resulting
                                       // value
    if (node instanceof integerNode) {
      return node;
    } else if (node instanceof booleanNode) {
      return node;
    } else if (node instanceof realNode) {
      return node;
    } else if (node instanceof stringNode) {
      return node;
    } else if (node instanceof mathOpNode) {
      return mathOpNodeRec(node);
    } else if (node instanceof variableReferenceNode) {
      interpreterDataType var = variableReferenceNode(node);
      if (var instanceof integerDataType) {
        return new integerNode(((integerDataType) var).getValue());
      } else if (var instanceof realDataType) {
        return new realNode(((realDataType) var).getValue());
      } else if (var instanceof stringDataType) {
        return new stringNode(((stringDataType) var).getValue());
      } else if (var instanceof booleanDataType) {
        return new booleanNode(((booleanDataType) var).getValue());
      }
    } else if (node instanceof booleanCompareNode) {
      return node;
    }
    throw new IllegalArgumentException("Invalid argument for math operation: " + node.toString());
  }

  private interpreterDataType variableReferenceNode(Node node) { // handles variable references, returns IDT if var's
                                                                 // reference
    if (!localVariables.containsKey(((variableReferenceNode) node).getName())) {
      throw new IllegalArgumentException("Variable not defined: " + ((variableReferenceNode) node).getName());
    }
    return localVariables.get(((variableReferenceNode) node).getName());
  }

  public interpreterDataType createIDT(variableNode variable) { // creates IDT from variable node
    switch (variable.getType()) {
      case "INTEGER":
        return new integerDataType((Integer) variable.getValue(), variable.isChangeable());
      case "STRING":
        return new stringDataType((String) variable.getValue(), variable.isChangeable());
      case "REAL":
        return new realDataType((Float) variable.getValue(), variable.isChangeable());
      case "BOOLEAN":
        return new booleanDataType((Boolean) variable.getValue(), variable.isChangeable());
      case "CHARACTER":
        return new characterDataType((Character) variable.getValue(), variable.isChangeable());
      case "ARRAY":
        return new arrayDataType((ArrayList) variable.getValue(), (Integer) variable.getFrom(),
            (Integer) variable.getTo(), variable.isChangeable());
    }
    return null;
  }

}