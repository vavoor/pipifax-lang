package ast;

import util.AsmWriter;
import util.Registers;

public class NotEqualExpr extends ComparativeExpr {

  public NotEqualExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);

    this.register = Registers.acquireGP();

    this.left.type().call(new Type.Operation() {
      public void forInt() {
        asm.sub(register, left.result(), right.result());
        asm.sltu(register, Registers.zero, register);
      }

      public void forDouble() {
        asm.feq(register, left.result(), right.result());
        asm.not(register, register);
      }
    });
    
    this.left.result().release();
    this.right.result().release();
  }
}
