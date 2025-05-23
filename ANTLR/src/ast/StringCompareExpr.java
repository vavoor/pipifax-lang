package ast;

import util.AsmWriter;
import util.Registers;

public class StringCompareExpr extends BinaryExpr {

  public StringCompareExpr(Expr left, Expr right) {
    super(left, right);
  }

  public int calculateAndCheckTypes() {
    int errors = this.left.calculateAndCheckTypes() + this.right.calculateAndCheckTypes();
    if (this.left.type().isString() && this.right.type().isString()) {
      this.type = IntType.instance();
    }
    else {
      System.err.println("Incompatible types for comparative operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    // TODO
    this.left.generateCode(asm);
    this.right.generateCode(asm);
    asm.mv(Registers.a0, this.left.result());
    asm.mv(Registers.a1, this.right.result());
    asm.jal("__strcmp");
    asm.mv(this.left.result(), Registers.a0);
    this.register = this.left.result();
    this.right.result().release();
  }
}
