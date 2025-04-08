package ast;

public class ArrayType extends Type {
  private int dim;
  private Type baseType;
  
  public ArrayType(int dim, Type baseType) {
    this.dim = dim;
    this.baseType = baseType;
  }
}
