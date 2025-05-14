package ast;

public class VoidType extends Type {
  private static Type instance = new VoidType();

  private VoidType() {}

  public void call(Operation op) {
    op.forVoid();
  }

  public static Type instance() {
    return instance;
  }

  public int size() {
    return 0;
  }

  public boolean accepts(Type type) {
    return false;
  }
}
