package ast;

import util.AsmWriter;

public class DoubleCastExpr extends UnaryExpr {

  public DoubleCastExpr(Expr expr) {
    super(expr);
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.expr.calculateAndCheckTypes();
    if (this.expr.type().isInt()) {
      this.type = DoubleType.instance();
    }
    else {
      System.err.println("Incompatible types for int cast operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    // TODO
    this.expr.generateCode(asm);
    this.register = this.expr.result();
  }
}
