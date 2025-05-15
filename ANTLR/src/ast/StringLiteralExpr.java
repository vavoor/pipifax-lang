package ast;

import util.AsmWriter;
import util.Registers;

public class StringLiteralExpr extends Expr {
  private String value;

  public StringLiteralExpr(String value) {
    this.value = value;
    this.type = IntType.instance();
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.register = Registers.acquireGP();
  }
}
