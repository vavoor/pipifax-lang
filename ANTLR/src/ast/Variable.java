package ast;

public abstract class Variable extends Node {
  private String name;
  private Type type;

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
}
