package ast;

public class ArrayType extends Type {
  private int dim;
  private Type baseType;
  
  public ArrayType(int dim, Type baseType) {
    this.dim = dim;
    this.baseType = baseType;
  }

  public int size() {
    return this.dim * this.baseType.size();
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
      return this.baseType.accepts(arr.baseType()) && this.dim == arr.dim;
    }
    return false;
  }
}
