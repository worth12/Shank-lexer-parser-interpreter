package Main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Lexer {

  private enum State {
    NONE, IDENTIFIER, NUMBER, DECIMALNUMBER, COMMENT, CHARACTERLITERAL, STRINGLITERAL
  };

  public static Shank lex(String line, Shank prevLine) throws SyntaxErrorException {
    State state;
    state = State.NONE; // start the state as none, as there are no inputs yet
    List<Character> charAccumulator = new ArrayList<Character>(); // list to collect characters to be madde into tokens
    List<Token> tokenAccumulator = new ArrayList<Token>(); // list of tokens made
    int currentLineNumber = prevLine.getLineNumber() + 1;
    int indentLevel = 0;

    

    if (prevLine.getCommentState()) { // if the previous line ended in the comment state, the state transitions to comment again.
      state = State.COMMENT;
      indentLevel = prevLine.getIndentLevel();
    } else {
      while (indentLevel < line.length()) { // indent level is only reevaluated if the line does not start with a comment
        if (line.charAt(indentLevel) == ' '){
          indentLevel++;
        }else if (line.charAt(indentLevel) == '\t'){  
          indentLevel = indentLevel + 4;// tabs are 4 spaces
        }else{
          break;
        }
      }
      indentLevel = indentLevel / 4; // indent level is rounded to the lowwer division of 4
      tokenAccumulator.addAll(createIndentTokens(indentLevel, prevLine.getIndentLevel(), currentLineNumber)); // indents / dedents are determined and added to the token accumulator
    }


    HashMap<String, Token> knownWords = new HashMap<String, Token>(); // hashmap of all key words
    knownWords.put("while", new Token(Token.type.WHILE, currentLineNumber));
    knownWords.put("if", new Token(Token.type.IF, currentLineNumber));
    knownWords.put("else", new Token(Token.type.ELSE, currentLineNumber));
    knownWords.put("elsif", new Token(Token.type.ELSIF, currentLineNumber));
    knownWords.put("for", new Token(Token.type.FOR, currentLineNumber));
    knownWords.put("from", new Token(Token.type.FROM, currentLineNumber));
    knownWords.put("repeat", new Token(Token.type.REPEAT, currentLineNumber));
    knownWords.put("until", new Token(Token.type.UNTIL, currentLineNumber));
    knownWords.put("to", new Token(Token.type.TO, currentLineNumber));
    knownWords.put("define", new Token(Token.type.DEFINE, currentLineNumber));
    knownWords.put("then", new Token(Token.type.THEN, currentLineNumber));
    knownWords.put("write", new Token(Token.type.WRITE, currentLineNumber));
    knownWords.put("variables", new Token(Token.type.VARIABLES, currentLineNumber));
    knownWords.put("constants", new Token(Token.type.CONSTANTS, currentLineNumber));
    knownWords.put("integer", new Token(Token.type.INTEGER, currentLineNumber));
    knownWords.put("characterliteral", new Token(Token.type.CHARACTERLITERAL, currentLineNumber)); 
    knownWords.put("stringliteral", new Token(Token.type.STRINGLITERAL, currentLineNumber));       
    knownWords.put("real", new Token(Token.type.REAL, currentLineNumber));
    knownWords.put("var", new Token(Token.type.VAR, currentLineNumber));
    knownWords.put("indent", new Token(Token.type.INDENT, currentLineNumber));
    knownWords.put("dedent", new Token(Token.type.DEDENT, currentLineNumber));
    knownWords.put("operator", new Token(Token.type.OPERATOR, currentLineNumber));
    knownWords.put("boolean", new Token(Token.type.BOOLEAN, currentLineNumber));
    knownWords.put("character", new Token(Token.type.CHARACTER, currentLineNumber));
    knownWords.put("string", new Token(Token.type.STRING, currentLineNumber));
    knownWords.put("array", new Token(Token.type.ARRAY, currentLineNumber));
    knownWords.put("not", new Token(Token.type.NOT, currentLineNumber));
    knownWords.put("and", new Token(Token.type.AND, currentLineNumber));
    knownWords.put("or", new Token(Token.type.OR, currentLineNumber));
    knownWords.put("true", new Token(Token.type.TRUE, currentLineNumber));
    knownWords.put("false", new Token(Token.type.FALSE, currentLineNumber));


    for (int i = 0; i < line.length(); i++) { // loop through each character in the line
      char character = line.charAt(i);
      char nextChar = ' ';
      if (i + 1 < line.length()) { // if there is another character left in the line, the next char is stored for operators
        nextChar = line.charAt(i+1);
      }
      // every valid character is added to charAccumulator
      // every token made is added to tokenAccumulator
      switch (state) {
        case NONE:
          if (character == ' ' | character == '\t') { // if the character is a space, there is nothing to be done, so breaks
            break;
          } else if (Character.isDigit(character)) { // if character is a digit, transition to number state
            state = State.NUMBER;
            charAccumulator.add(character);
            break;
          } else if (Character.isLetter(character)) { // if character is a letter, transition to word state
            state = State.IDENTIFIER;
            charAccumulator.add(character);
            break;
          } else if (character == '.') { // if character is a period, transition to dicimal number state
            state = State.DECIMALNUMBER;
            charAccumulator.add(character);
            break;
          } else if (character == '{' | character == '\'' | character == '\"'){
            state = pickState(character); // if the character is a {, ', or "  the appropriate state is selected
            break;
          } else {
            Token.type tempType = pickOperatorToken(character, nextChar);
            if (tempType != null){ // creates token with the given token type if it is a supported operator
              tokenAccumulator.add(new Token(tempType, currentLineNumber));
              if (tempType == Token.type.GREATERTHANEQUAL | tempType == Token.type.LESSTHANEQUAL | tempType == Token.type.NOTEQUALS | tempType == Token.type.ASSIGN ){
                i++; // if the operator token created was two characters long, the current position is incremented to account for the extra character
              }
              state = State.NONE;
              break;
            }
          }
          throw new SyntaxErrorException("Line " + currentLineNumber +": Invalid character: " + character); // if the input has any other characters, throws exception for invalid input
        case IDENTIFIER:
          if (character == ' ' | character == '{' | character == '\'' | character == '\"') { // if there is a space, creates token with current accumulator and clears the accumulator for the next token
            if (knownWords.containsKey(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", ""))){ // if the word is in the hashmap, the corresponding token is created and added to the accumulator
              tokenAccumulator.add(knownWords.get(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", "")));
            }
            else{
              tokenAccumulator.add(new Token(charAccumulator, Token.type.IDENTIFIER, currentLineNumber));
            }
            state = pickState(character); // determines state to transition to
            charAccumulator.clear();
            break;
          } else if (Character.isDigit(character)) { // allows numbers to words, but the word has to start with a letter
            charAccumulator.add(character);
            break;
          } else if (Character.isLetter(character)) {
            charAccumulator.add(character);
            break;
          }else {
            Token.type tempType = pickOperatorToken(character, nextChar); // if there is an operator, the appropriate operator token is created
            if (tempType != null){
              if (knownWords.containsKey(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", ""))){ // creates token wiith charAccumulator as done above
                tokenAccumulator.add(knownWords.get(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", "")));
              }
              else{
                tokenAccumulator.add(new Token(charAccumulator, Token.type.IDENTIFIER, currentLineNumber));
              }
              charAccumulator.clear();
              tokenAccumulator.add(new Token(tempType, currentLineNumber));
              if (tempType == Token.type.GREATERTHANEQUAL | tempType == Token.type.LESSTHANEQUAL | tempType == Token.type.NOTEQUALS | tempType == Token.type.ASSIGN ){ // creates operator token the same as above
                i++;
              }
              state = State.NONE; // reset state to NONE
              break;
            }
          }
            throw new SyntaxErrorException("Line " + currentLineNumber +": Invalid character: " + character); // if the input has any other characters, throws exception for invalid input

        case NUMBER:
        if (character == ' ' | character == '{' | character == '\'' | character == '\"'){ // if there is a space, creates token with current accumulator and clears the accumulator for the next token
            state = pickState(character); // transitions state to none
            tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber));
            charAccumulator.clear();
            break;
          } else if (Character.isDigit(character)) {
            charAccumulator.add(character);
            break;
          } else if (character == '.') { // given a period, transitions to dicimal number state. This is for numbers such as 123.456
            state = State.DECIMALNUMBER;
            charAccumulator.add(character);
            break;
          }else {
            Token.type tempType = pickOperatorToken(character, nextChar);
            if (tempType != null){
              tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber)); // creates number token from the accumulator before the operrator is created
              charAccumulator.clear();
              tokenAccumulator.add(new Token(tempType, currentLineNumber));
              if (tempType == Token.type.GREATERTHANEQUAL | tempType == Token.type.LESSTHANEQUAL | tempType == Token.type.NOTEQUALS | tempType == Token.type.ASSIGN ){
                i++;
              }
              state = State.NONE;
              break;
            }
          }
          throw new SyntaxErrorException("Line " + currentLineNumber +": Invalid character: " + character); // if the input has any other characters, throws exception for invalid input
        case DECIMALNUMBER: // Decimal number state exists to ensure there is only one period allowed per number. This avoids invalid numbers such as 123.456.789
          if (Character.isDigit(character)) {
            charAccumulator.add(character);
            break;
          }
          else if (character == ' ' | character == '{' | character == '\'' | character == '\"') { // if there is a space, creates token with current accumulator and clears the accumulator for the next token
            state = pickState(character); // transitions the state to none
            tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber));
            charAccumulator.clear();
            break;
          }else {
            Token.type tempType = pickOperatorToken(character, nextChar);
            if (tempType != null){
              tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber)); // creates number token from the accumulator before the operrator is created
              charAccumulator.clear();
              tokenAccumulator.add(new Token(tempType, currentLineNumber));
              if (tempType == Token.type.GREATERTHANEQUAL | tempType == Token.type.LESSTHANEQUAL | tempType == Token.type.NOTEQUALS | tempType == Token.type.ASSIGN ){
                i++;
              }
              state = State.NONE;
              break;
            }
          }
          throw new SyntaxErrorException("Line " + currentLineNumber +": Invalid character: " + character); // given a period, transitions to dicimal number state. This is for numbers such as 123.456
        case CHARACTERLITERAL:
          if (charAccumulator.size() == 0 & character !=  '\'') { // ensures that the character literal has only one character 
            charAccumulator.add(character);
            break;
          }
          else if (charAccumulator.size() == 1 & character == '\''){ // only creates character literal token if there is only one character and ends in  a '
            tokenAccumulator.add(new Token(charAccumulator, Token.type.CHARACTERLITERAL, currentLineNumber));
            charAccumulator.clear();
            state = State.NONE;
            break;
          }
          throw new SyntaxErrorException("Line " + currentLineNumber +": Invalid character: " + character + ", character literal must contain one character.");
        case STRINGLITERAL:
          if (character !=  '\"') { // adds any characters to string literal except "
            charAccumulator.add(character);
            break;
          }
          else {
            tokenAccumulator.add(new Token(charAccumulator, Token.type.STRINGLITERAL, currentLineNumber)); // creates string literal token if closing " is encountered
            charAccumulator.clear();
            state = State.NONE;
            break;
          }
        case COMMENT:
          if (character == '}') { // does nothing until comment is closed
            state = State.NONE;
          }
          break;
      }
    }
    switch (state) { // allows for last token to be created with the accumulator, based on the current state, as there is not necisarily a space after the last token of a line.
      case NONE: // if the state is none, there is nothing to create
        break;
      case COMMENT: // if the state is none, there is nothing to create
        break;
      case IDENTIFIER:
        if (knownWords.containsKey(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", ""))){
          tokenAccumulator.add(knownWords.get(charAccumulator.toString().replaceAll(", ", "").replace("[", "").replace("]", "")));
        }
        else{
          tokenAccumulator.add(new Token(charAccumulator, Token.type.IDENTIFIER, currentLineNumber));
        }
        break;
      case NUMBER:
        tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber));
        break;
      case DECIMALNUMBER:
        tokenAccumulator.add(new Token(charAccumulator, Token.type.NUMBER, currentLineNumber));
        break;
      case CHARACTERLITERAL:
        throw new SyntaxErrorException("Character Literal not closed"); // if in character literal state when line ends, this is invalid
      case STRINGLITERAL:
        throw new SyntaxErrorException("String Literal not closed"); // if in string literal state when line ends, this is invalid
    }
    if (state != State.COMMENT){
      tokenAccumulator.add(new Token(Token.type.ENDOFLINE, currentLineNumber)); // creates an end-of-line token as the end of the loop is the end of the line.
    }
    // tokenPrinter(line, tokenAccumulator); // prints the tokens with the inputs
    prevLine.setCommentState(state == State.COMMENT); // sets the previous line's values to the current line's values, creating new object is not necesary at this point
    prevLine.setIndentLevel(indentLevel);
    prevLine.setTokens(tokenAccumulator);
    prevLine.setLineNumber(currentLineNumber);
    return prevLine;
  }

  public static void tokenPrinter(String line, List<Token> tokens) {
    System.out.print("Original: " + line + "\nTokens: "); // prints the original line
    for (Token token : tokens) {
      if (token.getType() == Token.type.IDENTIFIER || token.getType() == Token.type.NUMBER || token.getType() == Token.type.CHARACTERLITERAL ||  token.getType() == Token.type.STRINGLITERAL) { // only prints the value of tokens that have a value attribute
        System.out.print(token.getType() + "(" + token.getValue() + ") "); // prints token type with the value
      } else {
        System.out.print(token.getType() + " ");
      }
    }
    System.out.println("");
  }

  public static List<Token> createIndentTokens(int indentLevel, int prevIndentLevel, int currentLineNumber) { // creates indent / dedent tokens to be added to the token accumulator
    List<Token> newIndentTokens = new ArrayList<Token>();
    if (indentLevel > prevIndentLevel){ // if there is a greater indent value than before, indent tokens are created
      for (int i = 0; i < indentLevel - prevIndentLevel; i++) {
        newIndentTokens.add(new Token(Token.type.INDENT, currentLineNumber));
      }
    }
    else if (indentLevel < prevIndentLevel){// if there is a lesser indent value than before, dedent tokens are created
      for (int i = 0; i < prevIndentLevel - indentLevel; i++) {
        newIndentTokens.add(new Token(Token.type.DEDENT, currentLineNumber));
      }
    }
    return newIndentTokens;
  }

  public static State pickState(char character) { // determines state to transition to for states that do not require storing the current character
    switch (character) {
      case '\'':
        return State.CHARACTERLITERAL;
      case '\"':
        return State.STRINGLITERAL;
      case '{':
        return  State.COMMENT;
      default:
        return State.NONE;
    }
  }

  public static Token.type pickOperatorToken(char character, char nextCharacter) { // choses the appropriate type of operator token to create based on the current and next chacter
    switch (character){
      case '<':
        if (nextCharacter == '>') {
          return Token.type.NOTEQUALS;
        } else if (nextCharacter == '='){
          return Token.type.LESSTHANEQUAL;
        } else {
          return Token.type.LESSTHAN;
        }
      
        case '>':
          if (nextCharacter == '='){
            return Token.type.GREATERTHANEQUAL;
          }
          else {
            return Token.type.GREATERTHAN;
          }
        
        case ':':
          if (nextCharacter == '='){
            return Token.type.ASSIGN;
          }
          else{
            return Token.type.COLON;
          }
        
        case '-':
          return Token.type.MINUS;
        
        case '+':
          return Token.type.PLUS;
        
        case '%':
          return Token.type.MOD;
        
        case '*':
          return Token.type.TIMES;

        case '(':
          return Token.type.LEFTPARENTHESIS;
        
        case ')':
          return Token.type.RIGHTPARENTHESIS;

        case '[':
          return Token.type.LEFTSQUARE;
        
        case ']':
          return Token.type.RIGHTSQUARE;
        
        case '=':
          return Token.type.EQUALS;

        case ',':
          return Token.type.COMMA;
          
        case ';':
          return Token.type.SEMICOLON;
        
        default:
          return null; // if it is none of the operators, null is returned
    }
  }

}