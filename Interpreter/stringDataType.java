package Interpreter;
public class stringDataType extends interpreterDataType {

  private String value;
  private boolean isChangeable = false;

  public stringDataType(String value) {
    this.value = value;
  }

  public stringDataType(String value, boolean isChangeable) {
    this.value = value;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  public String toString() {
    return value;
  }

  @Override
  public void fromString(String input) {
    this.value = input;
  }
  
}
