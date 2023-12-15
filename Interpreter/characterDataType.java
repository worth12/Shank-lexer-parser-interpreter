package Interpreter;
public class characterDataType extends interpreterDataType {

  private char value;
  private boolean isChangeable = false;

  public characterDataType(char value) {
    this.value = value;
  }

  public characterDataType(char value, boolean isChangeable) {
    this.value = value;
    this.isChangeable = isChangeable;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public char getValue() {
    return this.value;
  }

  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  public void setValue(char value) {
    this.value = value;
  }

  public String toString() {
    return "" + value;
  }
  @Override
  public void fromString(String input) {
    if (input.length() == 1) {
      this.value = input.charAt(0);
    }
    else{
      throw new IllegalArgumentException("expected one character, got:" + input.length());
    }
  }
  
}
