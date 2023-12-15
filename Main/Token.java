package Main;
import java.util.List;

public class Token {

  public enum type { // types of tokens
    ENDOFLINE, IDENTIFIER, NUMBER, WHILE, IF, ELSE, ELSIF, FOR, FROM, REPEAT, UNTIL, TO, DEFINE, THEN, WRITE, VARIABLES,
    CONSTANTS, INTEGER, CHARACTERLITERAL, STRINGLITERAL, REAL, VAR, INDENT, DEDENT, OPERATOR, BOOLEAN, CHARACTER,
    STRING, ARRAY, MOD, ASSIGN, EQUALS, MINUS, TIMES, DIVIDE, NOT, AND, OR, PLUS, LEFTPARENTHESIS, RIGHTPARENTHESIS,
    LEFTSQUARE, RIGHTSQUARE, NOTEQUALS, GREATERTHAN, LESSTHAN, GREATERTHANEQUAL, LESSTHANEQUAL, SEMICOLON, COMMA, COLON, TRUE, FALSE
  };

  // private attributes for Token object
  private type tokenType;
  private String value;
  private int lineNumber;

  public Token(List<Character> value, type tokenType, int lineNumber) {
    // takes the token type as string and assigns type accordingly
    // takes the list of characters and creates a string for the token value using
    // string methods to clean the formatting
    this.tokenType = tokenType;
    this.value = value.toString().replaceAll(", ", "").replace("[", "").replace("]", "");
    this.lineNumber = lineNumber; // line number to keep track of token position
  }

  public Token(type tokenType, int lineNumber) {
    this.tokenType = tokenType;
    this.lineNumber = lineNumber;
  }

  public Token() {
    this.tokenType = null;
  }


  public String getValue() { // returns the token value
    return this.value;
  }

  public type getType() {// returns the token type
    return this.tokenType;
  }

  public int getLineNumber() {// returns the line number
    return this.lineNumber;
  }

  // notably I did not add modifier accessors because the tokens should never be
  // altered, as far as I know.

}
