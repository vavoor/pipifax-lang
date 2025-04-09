package ast;

import util.Registers;

public abstract class LValue extends Node {
  protected Registers.Register address;
  
  public abstract Type type();

  public Registers.Register address() {
    return this.address;
  }
}
