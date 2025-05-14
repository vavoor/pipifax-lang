package ast;

public class StringType extends Type {
  private static Type instance = new StringType();

  private StringType() {}

  public static Type instance() {
    return instance;
  }

  public void call(Operation op) {
    op.forString();
  }

  public int size() {
    return 4;
  }

  public boolean isString() {
    return true;
  }

  public boolean accepts(Type type) {
    return type.isString();
  }
}
