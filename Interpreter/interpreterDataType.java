package Interpreter;
public abstract class interpreterDataType<T> { // abstract class for interpreter
  public abstract String toString();
  public abstract void fromString(String input);
  public abstract boolean isChangeable();
  public abstract void setChangable(boolean isChangeable);

}