package ast;

public class DoubleType extends Type {
  private static Type instance = new DoubleType();

  private DoubleType() {}

  public static Type instance() {
    return instance;
  }

  public void call(Operation op) {
    op.forDouble();
  }

  public int size() {
    return 8;
  }

  public boolean isDouble() {
    return true;
  }

  public boolean accepts(Type type) {
    return type.isDouble();
  }
}
