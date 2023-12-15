package Nodes;
import java.util.List;

public class ifNode extends Node {

  private Node condition = null;
  private List<statementNode> statements = null;
  private ifNode elseNode = null;

  public ifNode(Node condition, List<statementNode> statements, ifNode elseNode) {
    this.condition = condition;
    this.statements = statements;
    this.elseNode = elseNode;
  }

  public Node getCondition() {
    return this.condition;
  }

  public List<statementNode> getStatements() {
    return this.statements;
  }

  public ifNode getElseNode() {
    return this.elseNode;
  }


  public String toString() {
    if (condition == null & elseNode == null & statements == null){
      return null;
    }
    else if (condition == null & elseNode == null){
      return "ifNode(statements(" + statements.toString() + "))";
    }
    else if (elseNode == null){
      return "ifNode(condition(" + condition.toString() + "),statements(" + statements.toString() + "))";
    }
    return "ifNode(condition(" + condition.toString() + "),statements(" + statements.toString() + "),else(" + elseNode.toString() + "))";
  }
}
