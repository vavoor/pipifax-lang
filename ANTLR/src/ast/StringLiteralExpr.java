package ast;

import util.AsmWriter;
import util.Registers;

public class StringLiteralExpr extends Expr {
  private static int counter = 0;
  
  private String value;
  private int id;

  public StringLiteralExpr(String value) {
    this.value = value;
    this.type = StringType.instance();
    this.id = counter++;
  }

  public String value() {
    return this.value;
  }

  public String id() {
    return "_SC" + this.id;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.register = Registers.acquireGP();
  }
}
