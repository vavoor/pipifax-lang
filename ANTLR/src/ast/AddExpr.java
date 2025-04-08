package ast;

import java.util.Map;
import util.AsmWriter;

public class AddExpr extends Expr {
  private Expr left;
  private Expr right;

  public AddExpr(Expr left, Expr right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    return left.resolveNames(globals, functions) + right.resolveNames(globals, functions);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    // We use t3 to store the value of the left expression, as t2 is used for addresses
    this.left.generateCode(asm);
    asm.println("\tmv t2,t1");
    this.right.generateCode(asm);
    asm.println("\tadd t1,t2,t1");
  }
}
