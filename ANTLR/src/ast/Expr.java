package ast;

public abstract class Expr extends Node {
  protected Type type;
  
  public Type type() {
    return this.type;
  }
}
