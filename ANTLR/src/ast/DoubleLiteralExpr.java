package ast;

import util.AsmWriter;
import util.Registers;

public class DoubleLiteralExpr extends Expr {
  private double value;

  public DoubleLiteralExpr(double value) {
    this.value = value;
    this.type = DoubleType.instance();
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.register = Registers.acquireGP();
  }
}
