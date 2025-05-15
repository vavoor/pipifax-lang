package ast;

public class ReferenceType extends Type {

  private Type base;

  public ReferenceType(Type base) {
    this.base = base;
  }

  public Type base() {
    return this.base;
  }

  public boolean isReference() {
    return true;
  }

  public void call(Operation op) {
    op.forReference();
  }
  
  public int size() {
    return 4;
  }

  public boolean accepts(Type type) {
    return this.base.accepts(type);
  }
}
