package ast;

public abstract class LValue extends Node {
  // The code generator leaves the address in t2
  public abstract Type type();
}
