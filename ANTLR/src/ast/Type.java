package ast;

public abstract class Type extends Node {
  public abstract int size();

  public boolean isInt() {
    return false;
  }
  
  public boolean isArray() {
    return false;
  }

  public abstract boolean accepts(Type type);
}
