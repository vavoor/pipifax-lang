package ast;

public class LogicalExpr extends BinaryExpr {

  public LogicalExpr(Expr left, Expr right) {
    super(left, right);
  }

  public int calculateAndCheckTypes() {
    int errors = this.left.calculateAndCheckTypes() + this.right.calculateAndCheckTypes();
    if (this.left.type().isInt() && this.right.type().isInt()) {
      this.type = IntType.instance();
    }
    else {
      System.err.println("Incompatible types for logical operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }
}
