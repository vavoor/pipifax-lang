package ast;

import java.util.Map;
import util.AsmWriter;

public class MultExpr extends ArithmeticExpr {
  public MultExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);
    this.left.type().call(new Type.Operation() {
      public void forInt() {
        asm.mul(left.result(), left.result(), right.result());
      }

      public void forDouble() {
        asm.fmul(left.result(), left.result(), right.result());
      }
    });
    this.register = this.left.result();
    this.right.result().release();
  }
}
