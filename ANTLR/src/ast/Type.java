package ast;

public abstract class Type extends Node {

  public static class Operation {
    
    public void forInt() {
      throwNotImplemented();
    }

    public void forDouble() {
      throwNotImplemented();
    }

    public void forString() {
      throwNotImplemented();
    }

    public void forArray() {
      throwNotImplemented();
    }

    public void forVoid() {
      throwNotImplemented();
    }

    public void forError() {
      throwNotImplemented();
    }

    private void throwNotImplemented() {
      throw new RuntimeException("Operation not implemented");
    }
  }

  public abstract void call(Operation op);
  
  public abstract int size();

  public boolean isNumeric() {
    return isInt() || isDouble();
  }

  public boolean isInt() {
    return false;
  }

  public boolean isDouble() {
    return false;
  }

  public boolean isString() {
    return false;
  }
  
  public boolean isArray() {
    return false;
  }

  /**
   * @return if this type does accept another type fo assignment
   */
  public abstract boolean accepts(Type type);
}
