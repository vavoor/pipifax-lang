package ast;

import util.AsmWriter;

public class OrExpr extends LogicalExpr {

  public OrExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    // TODO
    this.left.generateCode(asm);
    this.right.generateCode(asm);
    asm.add(this.left.result(), this.left.result(), this.right.result());
    this.register = this.left.result();
    this.right.result().release();
  }
}
