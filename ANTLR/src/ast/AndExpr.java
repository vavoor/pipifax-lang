package ast;

import util.AsmWriter;
import util.Registers;
import util.Labels;

public class AndExpr extends LogicalExpr {

  public AndExpr(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    String lbl = Labels.label();
    
    this.left.generateCode(asm);
    asm.sltu(this.left.result(), Registers.zero, this.left.result());
    asm.beqz(this.left.result(), lbl);
    this.right.generateCode(asm);
    asm.sltu(this.left.result(), Registers.zero, this.right.result());
    asm.label(lbl);
    this.register = this.left.result();
    this.right.result().release();
  }
}
