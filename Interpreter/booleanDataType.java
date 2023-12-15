package Interpreter;
public class booleanDataType extends interpreterDataType {


  private Boolean value;
  private boolean isChangeable = false;


  public booleanDataType(Boolean Boolean) {
    this.value = value;
  }

  public booleanDataType(Boolean value, boolean isChangeable) {
    this.value = value;
    this.isChangeable = isChangeable;
  }

  public booleanDataType() {
      this.value = (Boolean)null;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  public Boolean getValue() {
    return this.value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  public String toString() {
    return "" + value;
  }

  public void fromString(String input) {
    if (input.equals("true")){
      this.value = true;
    }
    else if(input.equals("false")){
      this.value = false;
    }
    else{
      throw new IllegalArgumentException("expected either true or false, got:" + input);
    }
  }
  
  
}
