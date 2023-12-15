package Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interpreter.*;
import Nodes.*;

public class Shank {

  private int indentLevel = 0;
  private boolean isCommentState = false;
  private List<Token> tokens;
  private int lineNumber = 0;

  public static HashMap<String, functionNode> functions = new HashMap<String, functionNode>();

  public Shank(boolean isCommentState, Integer indentLevel, List<Token> tokens, Integer lineNumber) {// object to store
                                                                                                     // each line's
                                                                                                     // values, used to
                                                                                                     // keep track of
                                                                                                     // line numbers,
                                                                                                     // indent level,
                                                                                                     // and comment
                                                                                                     // status. Will be
                                                                                                     // used to store
                                                                                                     // tokens
    this.indentLevel = indentLevel;
    this.isCommentState = isCommentState;
    this.tokens = tokens;
    this.lineNumber = lineNumber;
  }

  public static void main(String[] args) throws IOException, SyntaxErrorException {
    if (args.length != 0) { // ensure that there is only one argument for the file name
      throw new IllegalArgumentException("Wrong number of arguments");
    }

    List<Token> tokens = new ArrayList<Token>();

    // Open file with given path and create list with each line
    // Path myPath = Paths.get(args[0]);
    Path myPath = Paths.get("test.shank");

    List<String> lines = Files.readAllLines(myPath, StandardCharsets.UTF_8);

    Shank prevLine = new Shank(false, 0, null, 0);

    Parser parse;
    // lex each line from the file
    for (String line : lines) {
      prevLine = Lexer.lex(line, prevLine);
      tokens.addAll(prevLine.tokens);
    }
    tokens.addAll(Lexer.lex("", prevLine).tokens); // to account for the remaining dedents
    parse = new Parser(tokens);
    programNode program = parse.parse();
    semanticAnalysis sem = new semanticAnalysis();
    sem.checkAssignments(program);
    addBuiltinFunctions();
    Interpreter interpreter = new Interpreter(program, functions);
    List<interpreterDataType> parameters = new ArrayList();
    interpreter.interpretFunction(program.getFunction("start"), parameters);
  }

  private static void addBuiltinFunctions() {
    functions.put("Read", new builtInRead("Read", null, null, null));
    functions.put("Write", new builtInWrite("Write", null, null, null));
    functions.put("Left", new builtInLeft("Left", null, null, null));
    functions.put("Right", new builtInRight("Right", null, null, null));
    functions.put("Substring", new builtInSubstring("Substring", null, null, null));
    functions.put("SquareRoot", new builtInSquareRoot("SquareRoot", null, null, null));
    functions.put("GetRandom", new builtInGetRandom("GetRandom", null, null, null));
    functions.put("IntegerToReal", new builtInIntegerToReal("IntegerToReal", null, null, null));
    functions.put("Start", new builtInStart("Start", null, null, null));
    functions.put("End", new builtInEnd("End", null, null, null));
  }

  // accesors / modifiers for the Shank object

  public int getIndentLevel() {
    return this.indentLevel;
  }

  public boolean getCommentState() {
    return this.isCommentState;
  }

  public List<Token> getTokens() {
    return this.tokens;
  }

  public Integer getLineNumber() {
    return this.lineNumber;
  }

  public void setIndentLevel(int indentLevel) {
    this.indentLevel = indentLevel;
  }

  public void setCommentState(boolean isCommentState) {
    this.isCommentState = isCommentState;
  }

  public void setTokens(List<Token> tokens) {
    this.tokens = tokens;
  }

  public void setLineNumber(Integer lineNumber) {
    this.lineNumber = lineNumber;
  }

  public HashMap<String, functionNode> getFunctions() {
    return functions;
  }

}
