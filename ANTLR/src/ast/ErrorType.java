package ast;

public class ErrorType extends Type {
  public int size() {
    return 0;
  }

  public boolean accepts(Type type) {
    return false;
  }
  
}
