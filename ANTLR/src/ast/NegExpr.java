package ast;

import util.AsmWriter;

public class NegExpr extends UnaryExpr {

  public NegExpr(Expr expr) {
    super(expr);
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.expr.calculateAndCheckTypes();
    if (this.expr.type().isNumeric()) {
      this.type = this.expr.type();
    }
    else {
      System.err.println("Incompatible types for neg operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    // TODO
    this.expr.generateCode(asm);
    this.expr.type().call(new Type.Operation() {
      public void forInt() {
        asm.neg(expr.result(),expr.result());
      }

      public void forDouble() {
        asm.fneg(expr.result(),expr.result());
      }
    });
    this.register = this.expr.result();
  }
}
