package Main;
public class SyntaxErrorException extends Exception {
  public SyntaxErrorException(String errorMessage) {
    super(errorMessage);
  }

  public SyntaxErrorException() {

  }
}
