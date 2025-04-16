package ast;

import util.Registers;
import util.AsmWriter;

public abstract class Variable extends Node {
  protected String name;
  protected Type type;

  protected int offset;

  public Variable(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  public String name() {
    return this.name;
  }

  public Type type() {
    return this.type;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int offset() {
    return this.offset;
  }

  public abstract Registers.Register la(AsmWriter asm);
}
