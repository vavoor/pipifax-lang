package ast;

public class IntType extends Type {
  private static IntType instance = new IntType();

  private IntType() {}

  public static Type instance() {
    return instance;
  }
}
