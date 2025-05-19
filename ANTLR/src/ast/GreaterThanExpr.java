package ast;

import util.AsmWriter;
import util.Registers;

public class GreaterThanExpr extends ComparativeExpr {

  public GreaterThanExpr(Expr left, Expr right) {
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
      }

      public void forDouble() {
        asm.flt(register, right.result(), left.result());
      }
    });
    
    this.left.result().release();
    this.right.result().release();
  }
}
