package ast;

import util.AsmWriter;
import util.Registers;

public class LessOrEqualExpr extends ComparativeExpr {

  public LessOrEqualExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);

    this.register = Registers.acquireGP();

    this.left.type().call(new Type.Operation() {
      public void forInt() {
        asm.slt(register, right.result(), left.result());
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
