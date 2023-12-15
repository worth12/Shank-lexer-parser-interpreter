package Interpreter;
import java.util.ArrayList;

public class arrayDataType<T> extends interpreterDataType {

  private ArrayList<T> value;
  private int from;
  private int to;
  private boolean isChangeable = false;

  public arrayDataType(ArrayList<T> value, int from, int to) {
    this.value = value;
    this.from = from;
    this.to = to;
  }
  public arrayDataType(ArrayList<T> value, int from, int to, boolean isChangeable) {
    this.value = value;
    this.from = from;
    this.to = to;
    this.isChangeable = isChangeable;
  }

  public boolean isChangeable() {
    return isChangeable;
  }

  public ArrayList<T> getValue() {
    return this.value;
  }

  private int getFrom() {
    return this.from;
  }
  
  public void setChangable(boolean isChangeable){
    this.isChangeable = isChangeable;
  }

  private int getTo() {
    return this.to;
  }

  private void setFrom(int from) {
    this.from = from;
  }

  private void setTo(int to) {
    this.to = to;
  }

  public void setValue(ArrayList<T> value) {
    this.value = value;
  }

  public String toString() {
    return value.toString();
  }

  @Override
  public void fromString(String input) {
    String[] items = input.split(",");
    ArrayList<String> values = new ArrayList<String>();
    for (int i = 0; i < items.length; i++) {
      values.set(i,items[i]);
    }
    this.value = (ArrayList<T>) values;
  }
  
}
