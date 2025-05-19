package ast;

import util.AsmWriter;
import util.Registers;

public class GreaterOrEqualExpr extends ComparativeExpr {

  public GreaterOrEqualExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);

    this.register = Registers.acquireGP();

    this.left.type().call(new Type.Operation() {
      public void forInt() {
        asm.slt(register, left.result(), right.result());
        asm.not(register, register);
      }

      public void forDouble() {
        asm.fle(register, right.result(), left.result());
      }
    });
    
    this.left.result().release();
    this.right.result().release();
  }
}
