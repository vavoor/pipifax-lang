package ast;

import util.AsmWriter;

public class SubExpr extends ArithmeticExpr {
  
  public SubExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);
    this.left.type().call(new Type.Operation() {
      public void forInt() {
        asm.sub(left.result(), left.result(), right.result());
      }

      public void forDouble() {
        asm.fsub(left.result(), left.result(), right.result());
      }
    });
    this.register = this.left.result();
    this.right.result().release();
  }
}
