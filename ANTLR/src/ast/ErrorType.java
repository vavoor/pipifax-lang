package ast;

public class ErrorType extends Type {
  public int size() {
    return 0;
  }

  public void call(Operation op) {
    op.forError();
  }

  public boolean accepts(Type type) {
    return false;
  }
  
}
