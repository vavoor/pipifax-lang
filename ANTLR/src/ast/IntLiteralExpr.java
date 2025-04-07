package ast;

import util.AsmWriter;

public class IntLiteralExpr extends Expr {
  private int value;

  public IntLiteralExpr(int value) {
    this.value = value;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.println("\tli t1," + this.value);
  }
}
