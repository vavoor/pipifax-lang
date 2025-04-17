package ast;

public class IntType extends Type {
  private static Type instance = new IntType();

  private IntType() {}

  public static Type instance() {
    return instance;
  }

  public int size() {
    return 4;
  }

  public boolean isInt() {
    return true;
  }

  public boolean accepts(Type type) {
    return type.isInt();
  }
}
