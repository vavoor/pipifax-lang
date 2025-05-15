package ast;

public class BaseArrayType extends Type {
  protected Type baseType;
  
  public BaseArrayType(Type baseType) {
    this.baseType = baseType;
  }

  public void call(Operation op) {
    op.forArray();
  }

  public int size() {
    throw new RuntimeException("baseArray has no size");
  }

  public boolean isArray() {
    return true;
  }

  public Type baseType() {
    return this.baseType;
  }

  public boolean accepts(Type type) {
    if (type.isArray()) {
      ArrayType arr = (ArrayType) type;
      return this.baseType.accepts(arr.baseType());
    }
    return false;
  }
}
