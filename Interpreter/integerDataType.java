package Interpreter;
import java.util.IllformedLocaleException;

public class integerDataType extends interpreterDataType {

  private Integer value;
  private boolean isChangeable = false;


  
  public integerDataType(Integer value) {
    this.value = value;
  }
  
  public integerDataType(Integer value, boolean isChangeable) {
    this.value = value;
    this.isChangeable = isChangeable;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public integerDataType() {
    this.value = (Integer)null;
  }

  public Integer getValue() {
    return this.value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public String toString() {
    return "" + value;
  }

  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  @Override
  public void fromString(String input) {
    try{
      this.value = Integer.parseInt(input);
    }
    catch(NumberFormatException e){
      throw new IllegalArgumentException("Inproper integer formatting, " + input);
    }
  }
  
}
