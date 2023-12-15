package Main;

import java.util.ArrayList;
import java.util.List;

import Nodes.*;
import Nodes.booleanCompareNode.NodeType;

public class Parser {

  private List<Token> tokens; // Token list for the parser object

  private int parens = 0; // Tracker for the number of parentheses

  public Parser(List<Token> tokens) { // constructor for parser objects
    this.tokens = tokens;
  }

  public programNode parse() throws SyntaxErrorException { // parses a list of tokens using the object's token list
    functionNode function;
    programNode program = new programNode();
    while (true) {
      function = function(); // stores the node created from the line
      if (function == null) {
        break;
      }
      // System.out.println(function.toString()); // prints node
      program.addFunction(function);
    }
    return program;
  }

  public functionNode function() throws SyntaxErrorException {
    // define string tokens
    if (tokens.size() == 0) {
      return null;
    }
    if (matchAndRemove(Token.type.DEFINE).getType() != Token.type.DEFINE) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,  expected \"define\"");
    }

    // name of the function
    if (peek(0).getType() != Token.type.IDENTIFIER) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,  expected function name");
    }
    String name = matchAndRemove(Token.type.IDENTIFIER).getValue();

    // left paren
    if (matchAndRemove(Token.type.LEFTPARENTHESIS).getType() != Token.type.LEFTPARENTHESIS) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,  expected left parenthasis");
    }

    // list of 0 or more variable declarations
    List<variableNode> parameters = parameterDeclarations();

    // right paren
    if (matchAndRemove(Token.type.RIGHTPARENTHESIS).getType() != Token.type.RIGHTPARENTHESIS) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,  expected right parenthasis");
    }

    // endOfLine
    expectEndOfLine();

    // const / vars
    List<variableNode> const_vars = constant_varDeclarations();

    // // indent
    // if (matchAndRemove(Token.type.INDENT).getType() != Token.type.INDENT) {
    // throw new SyntaxErrorException(
    // "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,
    // expected indent");
    // }

    // statements (currently just expressions)
    List<statementNode> statements = statements();

    // Node expression;
    // while (true) {
    // expression = expression();
    // if (expression == null) {
    // break;
    // }
    // System.out.println(expression.toString());
    // expectEndOfLine();
    // }

    // // dedent
    // if (matchAndRemove(Token.type.DEDENT).getType() != Token.type.DEDENT) {
    // throw new SyntaxErrorException(
    // "Line " + tokens.get(0).getLineNumber() + ": Incorrect function declaration,
    // expected dedent");
    // }
    expectEndOfLine();
    // return funciton or null

    return new functionNode(name, parameters, const_vars, statements);
  }

  public List<variableNode> parameterDeclarations() throws SyntaxErrorException {// process any parameters (may be vars
                                                                                 // or not) (may be 0 to indefinite)
    List<variableNode> variables = new ArrayList<variableNode>();
    boolean changeable = false;
    List<String> names = new ArrayList<String>();
    String type;

    if (peek(0).getType() == Token.type.RIGHTPARENTHESIS) {
      return variables;
    }

    while (true) {
      if (matchAndRemove(Token.type.VAR).getType() == Token.type.VAR) {
        changeable = true;
      }
      while (true) {
        if (peek(0).getType() != Token.type.IDENTIFIER) {
          throw new SyntaxErrorException("Line " + tokens.get(0).getLineNumber() + ": Incorrect parameter declaration");
        }
        names.add(matchAndRemove(Token.type.IDENTIFIER).getValue());
        if (matchAndRemove(Token.type.COMMA).getType() != Token.type.COMMA) {
          break;
        }
      }
      if (matchAndRemove(Token.type.COLON).getType() != Token.type.COLON) {
        throw new SyntaxErrorException(
            "Line " + tokens.get(0).getLineNumber() + ": Incorrect parameter declaration, expected colon");
      }

      type = pickType();
      for (String name : names) {
        variables.add(new variableNode(name, type, changeable, null));
      }
      names.clear();
      if (matchAndRemove(Token.type.SEMICOLON).getType() != Token.type.SEMICOLON) {
        break;
      }
    }
    if (variables.size() == 0) {
      return null;
    }
    return variables;
  }

  private String pickType() { // returns the type of the variable
    if (matchAndRemove(Token.type.INTEGER).getType() == Token.type.INTEGER) {
      return "INTEGER";
    } else if (matchAndRemove(Token.type.CHARACTER).getType() == Token.type.CHARACTER) {
      return "CHARACTER";
    } else if (matchAndRemove(Token.type.STRING).getType() == Token.type.STRING) {
      return "STRING";
    } else if (matchAndRemove(Token.type.BOOLEAN).getType() == Token.type.BOOLEAN) {
      return "BOOLEAN";
    } else if (matchAndRemove(Token.type.REAL).getType() == Token.type.REAL) {
      return "REAL";
    } else if (matchAndRemove(Token.type.INTEGER).getType() == Token.type.INTEGER) {
      return "INTEGER";
    }
    return null;
  }

  public <T> List<variableNode> constant_varDeclarations() throws SyntaxErrorException { // handles constant and
                                                                                         // variable
    // declarations from 0 to
    // indefinite
    List<variableNode> variables = new ArrayList<variableNode>();

    if (peek(0).getType() != Token.type.CONSTANTS & peek(0).getType() != Token.type.VARIABLES) {
      if (peek(0).getType() == Token.type.INDENT) {
        return variables;
      }
      throw new SyntaxErrorException("Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
    }
    while (true) {
      if (matchAndRemove(Token.type.CONSTANTS).getType() == Token.type.CONSTANTS) {
        while (true) {
          if (peek(0).getType() != Token.type.IDENTIFIER) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect constant declaration");
          }

          String name = matchAndRemove(Token.type.IDENTIFIER).getValue();

          if (matchAndRemove(Token.type.EQUALS).getType() != Token.type.EQUALS) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect constant declaration");
          }
          Node consty = pickConstant();
          T constant;
          if (consty instanceof integerNode) {
            variables.add(new variableNode(name, null, false, ((integerNode) consty).getValue()));
          } else if (consty instanceof realNode) {
            variables.add(new variableNode(name, null, false, ((realNode) consty).getValue()));
          } else if (consty instanceof stringNode) {
            variables.add(new variableNode(name, null, false, ((stringNode) consty).getValue()));
          } else if (consty instanceof characterNode) {
            variables.add(new variableNode(name, null, false, ((characterNode) consty).getValue()));
          } else if (consty instanceof booleanNode) {
            variables.add(new variableNode(name, null, false, ((booleanNode) consty).getValue()));
          } else {
            throw new IllegalArgumentException("Unknown constant value type");
          }

          if (matchAndRemove(Token.type.COMMA).getType() != Token.type.COMMA) {
            break;
          }
        }
      } else if (matchAndRemove(Token.type.VARIABLES).getType() == Token.type.VARIABLES) {
        List<String> names = new ArrayList<String>();
        String type;
        while (true) {
          if (peek(0).getType() != Token.type.IDENTIFIER) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }
          names.add(matchAndRemove(Token.type.IDENTIFIER).getValue());
          if (matchAndRemove(Token.type.COMMA).getType() != Token.type.COMMA) {
            break;
          }
        }
        if (matchAndRemove(Token.type.COLON).getType() != Token.type.COLON) {
          throw new SyntaxErrorException(
              "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration, expected colon");
        }
        type = pickType();
        if (peek(0).getType() == Token.type.FROM & (type.equals("INTEGER") | type.equals("STRING"))) {
          matchAndRemove(Token.type.FROM);
          if (peek(0).getType() != Token.type.NUMBER) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }
          integerNode from = new integerNode(Integer.valueOf(matchAndRemove(Token.type.NUMBER).getValue()));

          if (matchAndRemove(Token.type.TO).getType() != Token.type.TO) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }

          if (peek(0).getType() != Token.type.NUMBER) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }
          integerNode to = new integerNode(Integer.valueOf(matchAndRemove(Token.type.NUMBER).getValue()));
          for (String eachName : names) {
            variables.add(new variableNode(eachName, type, true, null, from, to));
          }
        } else if (peek(0).getType() == Token.type.FROM & type.equals("REAL")) {
          matchAndRemove(Token.type.FROM);
          if (peek(0).getType() != Token.type.NUMBER) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }
          if (peek(0).getValue().contains(".") != true) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber()
                    + ": Incorrect variable declaration, expected real number, not int");
          }

          realNode from = new realNode(Float.valueOf(matchAndRemove(Token.type.NUMBER).getValue()));

          if (matchAndRemove(Token.type.TO).getType() != Token.type.TO) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber() + ": Incorrect variable declaration");
          }
          if (peek(0).getValue().contains(".") != true) {
            throw new SyntaxErrorException(
                "Line " + tokens.get(0).getLineNumber()
                    + ": Incorrect variable declaration, expected real number, not int");
          }

          realNode to = new realNode(Float.valueOf(matchAndRemove(Token.type.NUMBER).getValue()));
          for (String eachName : names) {
            variables.add(new variableNode(eachName, type, true, null, from, to));
          }
        } else {
          for (String eachName : names) {
            variables.add(new variableNode(eachName, type, true, null));
          }
        }
      } else if (peek(0).getType() == Token.type.INDENT) {
        break;
      } else {
        throw new SyntaxErrorException("Line " + tokens.get(0).getLineNumber() + ": Incorrect parameter declaration");
      }
      expectEndOfLine();
    }

    if (variables.size() == 0) {
      return null;
    }
    return variables;
  }

  private Node pickConstant() { // helper function to determine constant value, returns node for the constant
                                // value
    int sign = 1;

    if (matchAndRemove(Token.type.MINUS).getType() == Token.type.MINUS) { // checks if number is negative
      sign = -1;
    }
    if (peek(0).getType() == Token.type.NUMBER) {
      Token numberToken = matchAndRemove(Token.type.NUMBER); // takes current token as a number
      // checks if the number is a float or
      // integer and returns it
      if (numberToken.getValue().contains(".") == true) {
        realNode num = new realNode(Float.valueOf(numberToken.getValue()) * sign);
        return num;
      } else {
        integerNode num = new integerNode(Integer.valueOf(numberToken.getValue()) * sign);
        return num;
      }
    }
    if (peek(0).getType() == Token.type.CHARACTERLITERAL) {
      return new characterNode(matchAndRemove(Token.type.CHARACTERLITERAL).getValue().charAt(0));
    }
    if (peek(0).getType() == Token.type.STRINGLITERAL) {
      return new stringNode(matchAndRemove(Token.type.STRINGLITERAL).getValue());
    }
    if (matchAndRemove(Token.type.TRUE).getType() == Token.type.TRUE) {
      return new booleanNode(true);
    }
    if (matchAndRemove(Token.type.FALSE).getType() == Token.type.FALSE) {
      return new booleanNode(false);
    }
    return null;
  }

  private ifNode findIfNode() throws SyntaxErrorException { // checks for if node, returns null if not found

    if (matchAndRemove(Token.type.IF).getType() != Token.type.IF) { // checks for IF token
      return null;
    }

    Node condition = boolCompare(); // gets boolean compare expression

    if (condition instanceof booleanNode) {
      condition = new booleanCompareNode(NodeType.EQUAL, condition, new booleanNode(true));
    } else if ((condition instanceof booleanCompareNode) != true) { // checks if condition was a boolean compare node
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected boolean expression after if.");
    }

    if (matchAndRemove(Token.type.THEN).getType() != Token.type.THEN) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected then after boolean expresion.");
    }

    expectEndOfLine();

    List<statementNode> statements = statements();

    return new ifNode(condition, statements, findElseNode());
  }

  private ifNode findElseNode() throws SyntaxErrorException { // finds any trailing ifnodes in an if / elsif block

    if (matchAndRemove(Token.type.ELSE).getType() == Token.type.ELSE) {
      expectEndOfLine();

      List<statementNode> statements = statements();

      return new ifNode(null, statements, null);
    }
    if (matchAndRemove(Token.type.ELSIF).getType() != Token.type.ELSIF) { // checks for ELSIF token
      return null;
    }

    Node condition = boolCompare(); // gets boolean compare expression

    if ((condition instanceof booleanCompareNode) != true) { // checks if condition was a boolean compare node
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected boolean expression after elsif.");
    }

    if (matchAndRemove(Token.type.THEN).getType() != Token.type.THEN) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected then after boolean expresion.");
    }

    expectEndOfLine();

    List<statementNode> statements = statements();

    return new ifNode(condition, statements, findElseNode());
  }

  public whileNode findWhileNode() throws SyntaxErrorException { // finds any while loops, returns null if none found
    if (matchAndRemove(Token.type.WHILE).getType() != Token.type.WHILE) {
      return null;
    }

    Node condition = boolCompare(); // gets boolean compare expression

    if ((condition instanceof booleanCompareNode) != true) { // checks if condition was a boolean compare node
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected boolean expression after elsif.");
    }


    expectEndOfLine();

    List<statementNode> statements = statements();

    return new whileNode(condition, statements);

  }

  public repeatNode findRepeatNode() throws SyntaxErrorException { // finds any repeat loop, returns null if not
    if (matchAndRemove(Token.type.REPEAT).getType() != Token.type.REPEAT) {
      return null;
    }

    if (matchAndRemove(Token.type.UNTIL).getType() != Token.type.UNTIL) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected until after repeat.");
    }

    Node condition = boolCompare();

    if ((condition instanceof booleanCompareNode) != true) { // checks if condition was a boolean compare node
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected boolean expression after until.");
    }

    expectEndOfLine();

    List<statementNode> statements = statements();

    return new repeatNode(condition, statements);
  }

  private forNode findForNode() throws SyntaxErrorException { // finds any for loop, returns null if not found

    if (matchAndRemove(Token.type.FOR).getType() != Token.type.FOR) {
      return null;
    }

    variableReferenceNode varNode = null;

    if (peek(0).getType() == Token.type.IDENTIFIER) {
      String name = matchAndRemove(Token.type.IDENTIFIER).getValue();
      varNode = new variableReferenceNode(name, null);
    }

    if (varNode == null) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected variable reference after for.");
    }

    if (matchAndRemove(Token.type.FROM).getType() != Token.type.FROM) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected from after variable reference.");
    }

    Node from = expression();

    if (from == null) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected expression after from.");
    }

    if (matchAndRemove(Token.type.TO).getType() != Token.type.TO) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected to after expression.");
    }

    Node to = expression();

    if (to == null) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected expression after to.");
    }

    expectEndOfLine();

    List<statementNode> statements = statements();

    return new forNode(varNode, from, to, statements);

  }

  public functionCallNode findFunctionCall() throws SyntaxErrorException { // finds any function calls, returns null if
                                                                           // none found
    if (peek(0).getType() != Token.type.IDENTIFIER) {
      return null;
    }
    if (peek(1).getType() == Token.type.ASSIGN) {
      return null;
    }
    String name = matchAndRemove(Token.type.IDENTIFIER).getValue();
    List<parameterNode> parameters = new ArrayList<parameterNode>();

    while (true) {
      if (peek(0).getType() == Token.type.IDENTIFIER) {
        parameters.add(
            new parameterNode(null, new variableReferenceNode(matchAndRemove(Token.type.IDENTIFIER).getValue(), null)));
      } else if (peek(0).getType() == Token.type.LEFTPARENTHESIS) {
        Node exp = expression();
        parameters.add(new parameterNode(null, exp));
      } else if (peek(0).getType() == Token.type.NUMBER) {
        int sign = 1;
        if (matchAndRemove(Token.type.MINUS).getType() == Token.type.MINUS) { // checks if number is negative
          sign = -1;
        }
        Token numberToken = tokens.get(0); // takes current token as a number
        if (matchAndRemove(Token.type.NUMBER).getType() == Token.type.NUMBER) { // checks if the number is a float or
                                                                                // integer and adds it
          if (numberToken.getValue().contains(".") == true) {
            realNode num = new realNode(Float.valueOf(numberToken.getValue()) * sign);
            parameters.add(new parameterNode(null, num));
          } else {
            integerNode num = new integerNode(Integer.valueOf(numberToken.getValue()) * sign);
            parameters.add(new parameterNode(null, num));
          }
        }
      } else if (peek(0).getType() == Token.type.STRINGLITERAL) {
        stringNode str = new stringNode(matchAndRemove(Token.type.STRINGLITERAL).getValue());
        parameters.add(new parameterNode(null, str));
      } else if (matchAndRemove(Token.type.VAR).getType() == Token.type.VAR) {
        String varName = matchAndRemove(Token.type.IDENTIFIER).getValue();
        if (varName == null) {
          throw new SyntaxErrorException(
              "Line " + tokens.get(0).getLineNumber() + ": Expected variable reference after var.");
        }
        parameters.add(new parameterNode(new variableReferenceNode(varName, null), null));
      } else {
        break;
      }
      if (peek(0).getType() == Token.type.ENDOFLINE) {
        break;
      }
      if (matchAndRemove(Token.type.COMMA).getType() != Token.type.COMMA) {
        throw new SyntaxErrorException(
            "Line " + tokens.get(0).getLineNumber() + ": Expected comma after parameter.");
      }
      
    }
    expectEndOfLine();
    return new functionCallNode(name, parameters);
  }

  private Token matchAndRemove(Token.type type) { // seeks a certain type of token from list and if found deletes and
                                                  // returns that token
    if (tokens.size() == 0 | type != tokens.get(0).getType()) { // returns a null token if match not found
      return new Token();
    }
    Token temp = tokens.get(0); // stores temporary token
    tokens.remove(0); // removes then returns that token
    return temp;
  }

  private void expectEndOfLine() throws SyntaxErrorException { // called when end of line token is expected, throws
                                                               // error if the current token is not.
    if (parens != 0) { // ensures correct number of parenthasis
      throw new SyntaxErrorException("Line " + tokens.get(0).getLineNumber() + ": Incorrect number of parenthasis.");
    }
    if (matchAndRemove(Token.type.ENDOFLINE).getType() != Token.type.ENDOFLINE) { // ensures there is at least one end
                                                                                  // of line token
      throw new SyntaxErrorException("No end of line token found");
    }
    while (tokens.size() != 0) { // consumes all end of line tokens until none is found
      if (matchAndRemove(Token.type.ENDOFLINE).getType() != Token.type.ENDOFLINE) {
        break;
      }
    }
  }

  private Token peek(int num) { // looks ahead in the token list a passed in number of spaces and returns that
                                // token
    if (this.tokens.size() <= num) { // makes sure there are enough tokens
      return new Token();
    }
    return this.tokens.get(num);
  }

  public Node boolCompare() throws SyntaxErrorException { // handles boolean comparisons, lowwest priority in the
                                                          // recursive descent

    Node expression1 = expression(); // expects expression

    if (matchAndRemove(Token.type.TRUE).getType() == Token.type.TRUE) {
      expression1 = new booleanNode("true");
    } else if (matchAndRemove(Token.type.FALSE).getType() == Token.type.FALSE) {
      expression1 = new booleanNode("false");
    }

    booleanCompareNode.NodeType compareType = pickComparison(); // finds if there is boolean operator

    if (compareType == null) {
      return expression1;
    }

    Node expression2 = expression(); // expects another expression

    if (matchAndRemove(Token.type.TRUE).getType() == Token.type.TRUE) {
      expression2 = new booleanNode("true");
    } else if (matchAndRemove(Token.type.FALSE).getType() == Token.type.FALSE) {
      expression2 = new booleanNode("false");
    }

    if (expression2 == null) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": Expected expression after boolean operator");
    }

    return new booleanCompareNode(compareType, expression1, expression2); // returns new boolean compare node
  }

  public Node expression() throws SyntaxErrorException { // start of recursive descent to create mathoptnode tree
    Node leftTerm = term();
    mathOpNode.operationType operator = null;

    if (matchAndRemove(Token.type.PLUS).getType() == Token.type.PLUS) { // checks if plus or minus is found
      operator = mathOpNode.operationType.PLUS;
    } else if (matchAndRemove(Token.type.MINUS).getType() == Token.type.MINUS) {
      operator = mathOpNode.operationType.MINUS;
    } else if (tokens.size() == 1) {
      return leftTerm;
    } else {

    }

    NodeType comp = pickComparison();

    // if there is another iteration of plus / minus, creates mathoptnode with
    // another expression call
    if (comp == null & (peek(1).getType() == Token.type.PLUS | peek(1).getType() == Token.type.MINUS)) {
      return new mathOpNode(operator, leftTerm, expression());
    }
    if (comp != null & (peek(1).getType() == Token.type.PLUS | peek(1).getType() == Token.type.MINUS)) {
      return new booleanCompareNode(comp, leftTerm, expression());
    }
    Node rightTerm = term(); // takes right term and returns mathopt or just term if null / not
    if (rightTerm == null) {
      return leftTerm;
    } else if (comp != null) {
      return new booleanCompareNode(comp, leftTerm, rightTerm);
    }
    return new mathOpNode(operator, leftTerm, rightTerm);
  }

  public Node term() throws SyntaxErrorException { // step 2 in recursive descent,
    Node leftTerm = factor(); // takes factor for the left term

    mathOpNode.operationType operator = null;
    if (matchAndRemove(Token.type.TIMES).getType() == Token.type.TIMES) { // checks if operation is times, divide or
                                                                          // mod, if not returns the left term
      operator = mathOpNode.operationType.TIMES;
    } else if (matchAndRemove(Token.type.DIVIDE).getType() == Token.type.DIVIDE) {
      operator = mathOpNode.operationType.DIVIDE;
    } else if (matchAndRemove(Token.type.MOD).getType() == Token.type.MOD) {
      operator = mathOpNode.operationType.MOD;
    } else {
      return leftTerm;
    }
    // if the operation repeats, recursively calls itself for right term
    if (peek(1).getType() == Token.type.TIMES | peek(1).getType() == Token.type.DIVIDE
        | peek(2).getType() == Token.type.MOD) {
      return new mathOpNode(operator, leftTerm, term());
    }

    Node rightTerm = factor(); // if not a factor is taken for the right term and mathopt node is returned
    return new mathOpNode(operator, leftTerm, rightTerm);
  }

  public Node factor() throws SyntaxErrorException { // final step of the recursive descent
    int sign = 1;
    // checks for opening parenthasis, if so calls expression for the internal
    // expression
    if (matchAndRemove(Token.type.LEFTPARENTHESIS).getType() == Token.type.LEFTPARENTHESIS) {
      parens++;
      Node expression = expression();
      if (matchAndRemove(Token.type.RIGHTPARENTHESIS).getType() == Token.type.RIGHTPARENTHESIS) { // checks for closing
                                                                                                  // parenthasis
        if (parens == 0) {
          throw new SyntaxErrorException(
              "Line " + tokens.get(0).getLineNumber() + ": Incorrect number of parenthasis.");
        }
        parens--;
        return expression;
      }

      // if not, throws error
      throw new SyntaxErrorException("Line " + tokens.get(0).getLineNumber() + ": Incorrect number of parenthasis.");
    }

    if (matchAndRemove(Token.type.TRUE).getType() == Token.type.TRUE){
      return new booleanNode(true);
    } else if (matchAndRemove(Token.type.FALSE).getType() == Token.type.FALSE){
      return new booleanNode(false);
    }

    if (peek(0).getType() == Token.type.IDENTIFIER) {
      String name = matchAndRemove(Token.type.IDENTIFIER).getValue();
      Node index = null;
      if (matchAndRemove(Token.type.LEFTSQUARE).getType() == Token.type.LEFTSQUARE) {
        index = expression();
        if (matchAndRemove(Token.type.RIGHTSQUARE).getType() != Token.type.RIGHTSQUARE) {
          throw new SyntaxErrorException(
              "Line " + tokens.get(0).getLineNumber() + ": Incorrect number of brackets.");
        }
      }
      return new variableReferenceNode(name, index);
    }
    if (peek(0).getType() == Token.type.STRINGLITERAL) {
      return new stringNode(matchAndRemove(Token.type.STRINGLITERAL).getValue());
    }

    if (matchAndRemove(Token.type.MINUS).getType() == Token.type.MINUS) { // checks if number is negative
      sign = -1;
    }
    Token numberToken = tokens.get(0); // takes current token as a number
    if (matchAndRemove(Token.type.NUMBER).getType() == Token.type.NUMBER) { // checks if the number is a float or
                                                                            // integer and returns it
      if (numberToken.getValue().contains(".") == true) {
        realNode num = new realNode(Float.valueOf(numberToken.getValue()) * sign);
        return num;
      } else {
        integerNode num = new integerNode(Integer.valueOf(numberToken.getValue()) * sign);
        return num;
      }
    }
    return null;
  }

  public booleanCompareNode.NodeType pickComparison() { // deteremines if there is a boolean operator, and if so return
                                                        // the type, if not, null
    if (matchAndRemove(Token.type.EQUALS).getType() == Token.type.EQUALS) {
      return booleanCompareNode.NodeType.EQUAL;
    } else if (matchAndRemove(Token.type.LESSTHAN).getType() == Token.type.LESSTHAN) {
      return booleanCompareNode.NodeType.LESSTHAN;
    } else if (matchAndRemove(Token.type.LESSTHANEQUAL).getType() == Token.type.LESSTHANEQUAL) {
      return booleanCompareNode.NodeType.LESSTHANEQUAL;
    } else if (matchAndRemove(Token.type.GREATERTHAN).getType() == Token.type.GREATERTHAN) {
      return booleanCompareNode.NodeType.GREATERTHAN;
    } else if (matchAndRemove(Token.type.GREATERTHANEQUAL).getType() == Token.type.GREATERTHANEQUAL) {
      return booleanCompareNode.NodeType.GREATERTHANEQUAL;
    } else if (matchAndRemove(Token.type.NOTEQUALS).getType() == Token.type.NOTEQUALS) {
      return booleanCompareNode.NodeType.NOTEQUALS;
    } else if (matchAndRemove(Token.type.NOT).getType() == Token.type.NOT) {
      return booleanCompareNode.NodeType.NOT;
    } else if (matchAndRemove(Token.type.AND).getType() == Token.type.AND) {
      return booleanCompareNode.NodeType.AND;
    } else if (matchAndRemove(Token.type.OR).getType() == Token.type.OR) {
      return booleanCompareNode.NodeType.OR;
    }
    return null;
  }

  public Node assignment() throws SyntaxErrorException { // handles assignment statements

    String name = matchAndRemove(Token.type.IDENTIFIER).getValue();
    Node index = null;

    if (name == null) {
      return null;
    }

    if (matchAndRemove(Token.type.LEFTSQUARE).getType() == Token.type.LEFTSQUARE) {
      index = assignmentRec();
      if (matchAndRemove(Token.type.RIGHTSQUARE).getType() != Token.type.RIGHTSQUARE) {
        throw new SyntaxErrorException(
            "Line " + tokens.get(0).getLineNumber() + ": expected right bracket.");
      }
    }

    variableReferenceNode var = new variableReferenceNode(name, index);

    if (matchAndRemove(Token.type.ASSIGN).getType() != Token.type.ASSIGN) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected assignment.");
    }

    Node value = boolCompare();

    expectEndOfLine();

    return new assignmentNode(value, var);
  }

  public Node assignmentRec() throws SyntaxErrorException { // recursively handles assignment indexing, allowing for
                                                            // indefinite nested arrays
    Node index = null;

    if (peek(1).getType() != Token.type.LEFTSQUARE) {
      return expression();
    }

    String name = matchAndRemove(Token.type.IDENTIFIER).getValue();

    if (name == null) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected name identifier.");
    }

    if (matchAndRemove(Token.type.LEFTSQUARE).getType() != Token.type.LEFTSQUARE) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected name identifier.");
    }

    index = assignmentRec();

    if (matchAndRemove(Token.type.RIGHTSQUARE).getType() != Token.type.RIGHTSQUARE) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected right bracket.");
    }

    return new variableReferenceNode(name, index);

  }

  public statementNode statement() throws SyntaxErrorException { // temporarily only calling assignment

    Node statement = findIfNode();
    if (statement != null) {
      return new statementNode(statement);
    }

    statement = findFunctionCall();
    if (statement != null) {
      return new statementNode(statement);
    }

    statement = assignment();
    if (statement != null) {
      return new statementNode(statement);
    }

    statement = findWhileNode();
    if (statement != null) {
      return new statementNode(statement);
    }

    statement = findRepeatNode();
    if (statement != null) {
      return new statementNode(statement);
    }

    statement = findForNode();
    if (statement != null) {
      return new statementNode(statement);
    }

    return null;
  }

  public List<statementNode> statements() throws SyntaxErrorException { // temporarily only calling assignment until
                                                                        // there are none, then returning the results in
                                                                        // a list

    List<statementNode> statements = new ArrayList<statementNode>();

    if (matchAndRemove(Token.type.INDENT).getType() != Token.type.INDENT) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected indent.");
    }

    while (true) {
      statementNode statement = statement();
      if (statement == null) {
        break;
      }
      statements.add(statement);
    }

    if (matchAndRemove(Token.type.DEDENT).getType() != Token.type.DEDENT) {
      throw new SyntaxErrorException(
          "Line " + tokens.get(0).getLineNumber() + ": expected dedent.");
    }

    return statements;
  }
}