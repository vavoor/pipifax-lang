package ast;

public class ArrayType extends BaseArrayType {
  private int dim;
  
  public ArrayType(int dim, Type baseType) {
    super(baseType);
    this.dim = dim;
  }

  public int size() {
    return this.dim * this.baseType.size();
  }

  public boolean accepts(Type type) {
    if (type.isArray()) {
      ArrayType arr = (ArrayType) type;
      return this.baseType.accepts(arr.baseType()) && this.dim == arr.dim;
    }
    return false;
  }
}
