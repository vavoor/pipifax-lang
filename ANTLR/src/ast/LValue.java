package ast;

import util.Registers;

public abstract class LValue extends Node {
  protected Registers.GPRegister address;
  
  public abstract Type type();

  public Registers.GPRegister address() {
    return this.address;
  }
}
