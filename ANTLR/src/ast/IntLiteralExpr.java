package ast;

import util.AsmWriter;
import util.Registers;

public class IntLiteralExpr extends Expr {
  private int value;

  public IntLiteralExpr(int value) {
    this.value = value;
    this.type = IntType.instance();
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.register = Registers.acquire();
    asm.println("\tli " + this.register + "," + this.value);
  }
}
