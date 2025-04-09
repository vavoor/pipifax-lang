package ast;

import util.Registers;

public abstract class Expr extends Node {
  protected Type type;
  protected Registers.Register register;
  
  public Type type() {
    return this.type;
  }

  public Registers.Register result() {
    return this.register;
  }
}
