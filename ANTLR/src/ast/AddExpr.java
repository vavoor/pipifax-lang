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
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.left.resolveFunctionNames(functions) + this.right.resolveFunctionNames(functions);
    return errors;
  }

  public int calculateAndCheckTypes() {
    int errors = this.left.calculateAndCheckTypes() + this.right.calculateAndCheckTypes();
    if (this.left.type().isInt() && this.right.type().isInt()) {
      this.type = IntType.instance();
    }
    else {
      System.err.println("Incompatible types for arithmetic operation");
      errors++;
      this.type = new ErrorType();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.left.generateCode(asm);
    asm.println("\tmv t2,t1");
    this.right.generateCode(asm);
    asm.println("\tadd t1,t2,t1");
  }
}
