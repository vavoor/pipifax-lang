package ast;

import java.util.Map;
import util.AsmWriter;

public class Assignment extends Statement {
  private LValue lvalue;
  private Expr rhs;
  
  public Assignment(LValue lvalue, Expr rhs) {
    this.lvalue = lvalue;
    this.rhs = rhs;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.lvalue.resolveFunctionNames(functions) + this.rhs.resolveFunctionNames(functions);
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    errors += this.rhs.calculateAndCheckTypes();
    errors += this.lvalue.calculateAndCheckTypes();
    if (!this.lvalue.type().accepts(this.rhs.type())) {
      System.err.println("Incompatible types in assignment");
      errors++;
    }
    return errors;
  }

  public void generateCode(AsmWriter asm) {
    this.rhs.generateCode(asm);
    this.lvalue.generateCode(asm);
    asm.println("\tsw " + this.rhs.result() + ",0(" + this.lvalue.address() + ")");
    this.rhs.result().release();
    this.lvalue.address().release();
  }
}
