package ast;

import util.AsmWriter;

public class NotExpr extends UnaryExpr {

  public NotExpr(Expr expr) {
    super(expr);
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.expr.calculateAndCheckTypes();
    if (this.expr.type().isInt()) {
      this.type = IntType.instance();
    }
    else {
      System.err.println("Incompatible types for not operation");
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
