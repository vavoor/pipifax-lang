package ast;

public abstract class ComparativeExpr extends BinaryExpr {

  public ComparativeExpr(Expr left, Expr right) {
    super(left, right);
  }

  public int calculateAndCheckTypes() {
    int errors = this.left.calculateAndCheckTypes() + this.right.calculateAndCheckTypes();
    if (this.left.type().isNumeric() && this.right.type().isNumeric()) {
      this.type = this.left.type();
    }
    else {
      System.err.println("Incompatible types for comparative operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }
}
