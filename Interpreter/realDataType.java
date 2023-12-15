package Interpreter;
public class realDataType extends interpreterDataType {

  private Float value;
  private boolean isChangeable = false;


  public realDataType(Float value) {
    this.value = value;
  }

  public realDataType(Float value, boolean isChangeable) {
    this.value = value;
    this.isChangeable = isChangeable;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public Float getValue() {
    return this.value;
  }

  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  public void setValue(Float value) {
    this.value = value;
  }
  public String toString() {
    return "" + value;
  }

  @Override
  public void fromString(String input) {
    try{
      this.value = Float.parseFloat(input);
    }
    catch(NumberFormatException e){
      throw new IllegalArgumentException("Inproper integer formatting, " + input);
    }
  }
  
}
