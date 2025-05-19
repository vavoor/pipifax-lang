package ast;

import util.AsmWriter;
import util.Registers;

public class DoubleLiteralExpr extends Expr {
  private static int counter = 0;
  
  private double value;
  private int id;

  public DoubleLiteralExpr(double value) {
    this.value = value;
    this.type = DoubleType.instance();
    this.id = counter++;
  }

  public double value() {
    return this.value;
  }

  public String id() {
    return "_DC" + this.id;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.register = Registers.acquireFP();
    Registers.Register addr = Registers.acquireGP();
    asm.la(addr, this.id());
    asm.flw(this.register, addr);
    addr.release();
  }
}
