package Main;

import java.util.List;
import java.util.Map;

import Nodes.*;

public class semanticAnalysis {

  public semanticAnalysis() {

  }

  public void checkAssignments(programNode program) {
    for (Map.Entry<String, functionNode> entry : program.getFunctions().entrySet()) {
      functionNode function = entry.getValue();
      checkAssignmentRec(entry.getValue().getStatements(), function);
    }
  }

  public void checkAssignmentRec(List<statementNode> statements, functionNode function) {
    for (statementNode statement : statements) {// mathOpNode
      if (statement.getValue() instanceof assignmentNode) {
        actuallyCheckAssignment((assignmentNode) statement.getValue(), function);
      } else if (statement.getValue() instanceof ifNode) {
        checkAssignmentRec(((ifNode) statement.getValue()).getStatements(), function);
      } else if (statement.getValue() instanceof forNode) {
        checkAssignmentRec(((forNode) statement.getValue()).getStatements(), function);
      } else if (statement.getValue() instanceof repeatNode) {
        checkAssignmentRec(((repeatNode) statement.getValue()).getStatements(), function);
      } else if (statement.getValue() instanceof whileNode) {
        checkAssignmentRec(((whileNode) statement.getValue()).getStatements(), function);
      }
    }
  }

  public void actuallyCheckAssignment(assignmentNode node, functionNode function) {
    for (variableNode varRef : function.getConstants_variables()) {
      if (node.getVariable().getName().equals(varRef.getName())) {
        boolean done = false;
        switch (varRef.getType()) {
          case "INTEGER":
            if (!(node.getValue() instanceof integerNode)) {
              if (node.getValue() instanceof mathOpNode) {
                if (checkMathOpNodeRec(((mathOpNode) node.getValue()), function).equals("INTEGER")) {
                  break;
                }
              }
              if (node.getValue() instanceof variableReferenceNode) {
                for (variableNode assignNode : function.getConstants_variables()) {
                  if (((variableReferenceNode) node.getValue()).getName().equals(assignNode.getName())) {
                    if (assignNode.getType().equals("INTEGER")) {
                      done = true;
                      break;
                    }
                  }
                }
                for (variableNode assignNode : function.getParameters()) {
                  if (((variableReferenceNode) node.getValue()).getName().equals(assignNode.getName())) {
                    if (assignNode.getType().equals("INTEGER")) {
                      done = true;
                      break;
                    }
                  }
                }
              }
              if (done) {
                break;
              }
              throw new IllegalArgumentException("Can only assign Integer to Integer!");
            }
            break;
          case "STRING":
            if (!(node.getValue() instanceof stringNode)) {
              if (node.getValue() instanceof mathOpNode) {
                if (checkMathOpNodeRec(((mathOpNode) node.getValue()), function).equals("STRING")) {
                  break;
                }
              }
              throw new IllegalArgumentException("Can only assign STRING to STRING!");
            }
            break;
          case "REAL":
            if (!(node.getValue() instanceof realNode)) {
              if (node.getValue() instanceof mathOpNode) {
                if (checkMathOpNodeRec(((mathOpNode) node.getValue()), function).equals("REAL")) {
                  break;
                }
              }
              throw new IllegalArgumentException("Can only assign REAL to REAL!");
            }
            break;
          case "BOOLEAN":
            if (!(node.getValue() instanceof booleanNode)) {
              if (node.getValue() instanceof mathOpNode) {
                if (checkMathOpNodeRec(((mathOpNode) node.getValue()), function).equals("BOOLEAN")) {
                  break;
                }
              }
              throw new IllegalArgumentException("Can only assign BOOLEAN to BOOLEAN!");
            }
            break;
          case "CHARACTER":
            if (!(node.getValue() instanceof characterNode)) {
              if (node.getValue() instanceof mathOpNode) {
                if (checkMathOpNodeRec(((mathOpNode) node.getValue()), function).equals("CHARACTER")) {
                  break;
                }
              }
              throw new IllegalArgumentException("Can only assign Character to Character!");
            }
            break;
        }
      }

    }
  }

  private String checkMathOpNodeRec(mathOpNode node, functionNode function) {
    String leftType = null;
    if (node.getLeftNode() instanceof mathOpNode) {
      leftType = checkMathOpNodeRec(((mathOpNode) node.getLeftNode()), function);
    } else if (node.getLeftNode() instanceof stringNode) {
      leftType = "STRING";
    } else if (node.getLeftNode() instanceof characterNode) {
      leftType = "CHARACTER";
    } else if (node.getLeftNode() instanceof integerNode) {
      leftType = "INTEGER";
    } else if (node.getLeftNode() instanceof realNode) {
      leftType = "REAL";
    } else if (node.getLeftNode() instanceof stringNode) {
      leftType = "STRING";
    } else if (node.getLeftNode() instanceof variableReferenceNode) {
      for (variableNode varRef : function.getConstants_variables()) {
        if (((variableReferenceNode) node.getLeftNode()).getName().equals(varRef.getName())) {
          leftType = varRef.getType();
          break;
        }
      }
    } else {
      throw new IllegalArgumentException("could not find type of left math operation value.");
    }
    String rightType = null;
    if (node.getRightNode() instanceof mathOpNode) {
      rightType = checkMathOpNodeRec(((mathOpNode) node.getRightNode()), function);
    } else if (node.getRightNode() instanceof stringNode) {
      rightType = "STRING";
    } else if (node.getRightNode() instanceof characterNode) {
      rightType = "CHARACTER";
    } else if (node.getRightNode() instanceof integerNode) {
      rightType = "INTEGER";
    } else if (node.getRightNode() instanceof realNode) {
      rightType = "REAL";
    } else if (node.getRightNode() instanceof stringNode) {
      rightType = "STRING";
    } else if (node.getRightNode() instanceof variableReferenceNode) {
      for (variableNode varRef : function.getConstants_variables()) {
        if (((variableReferenceNode) node.getRightNode()).getName().equals(varRef.getName())) {
          rightType = varRef.getType();
          break;
        }
      }
    } else {
      throw new IllegalArgumentException("could not find type of right math operation value.");
    }

    if (leftType.equals(rightType)) {
      return leftType;
    }
    throw new IllegalArgumentException("Math operations must be of the same type.");

  }

}
