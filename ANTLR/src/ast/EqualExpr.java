package ast;

import util.AsmWriter;
import util.Registers;

public class EqualExpr extends ComparativeExpr {

  public EqualExpr(Expr left, Expr right) {
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
        asm.sltiu(register, register, 1); // == seqz
      }

      public void forDouble() {
        asm.feq(register, left.result(), right.result());
      }
    });
    
    this.left.result().release();
    this.right.result().release();
  }
}
