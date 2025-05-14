package ast;

import java.util.Map;
import util.AsmWriter;

public class DivExpr extends ArithmeticExpr {
  public DivExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    this.right.generateCode(asm);
    asm.div(this.left.result(), this.left.result(), this.right.result());
    this.register = this.left.result();
    this.right.result().release();
  }
}
