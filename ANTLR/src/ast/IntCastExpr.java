package ast;

import util.AsmWriter;
import util.Registers;

public class IntCastExpr extends UnaryExpr {

  public IntCastExpr(Expr expr) {
    super(expr);
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.expr.calculateAndCheckTypes();
    if (this.expr.type().isDouble()) {
      this.type = IntType.instance();
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
    this.expr.generateCode(asm);
    this.register = Registers.acquireGP();
    asm.dtoi(this.register, this.expr.result());
    this.expr.result().release();
  }
}
