package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;
import util.Labels;

public class IfStmt extends Statement {
  private Expr cond;
  private Block ifTrue;
  private Block ifFalse;
  
  public IfStmt(Expr cond, Block ifTrue, Block ifFalse) {
    this.cond = cond;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.cond.resolveFunctionNames(functions);
    errors += this.ifTrue.resolveFunctionNames(functions);
    errors += this.ifFalse.resolveFunctionNames(functions);
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    errors += this.cond.calculateAndCheckTypes();
    errors += this.ifTrue.calculateAndCheckTypes();
    errors += this.ifFalse.calculateAndCheckTypes();
    if (errors == 0 && !this.cond.type().isInt()) {
      System.err.println("Condition in if statement must be integer");
      errors++;
    }
    return errors;
  }

  public void generateCode(AsmWriter asm) {
    this.cond.generateCode(asm);
    if (this.ifFalse.isEmpty()) {
      String lbl = Labels.label();
      asm.beqz(this.cond.result(), lbl);
      this.cond.result().release();
      this.ifTrue.generateCode(asm);
      asm.label(lbl);
    }
    else {
      String lbl1 = Labels.label();
      String lbl2 = Labels.label();
      asm.beqz(this.cond.reesult(), lbl1);
      this.cond.result().release();
      this.ifTrue.generateCode(asm);
      asm.j(lbl2);
      asm.label(lbl1);
      this.ifFalse.generateCode(asm);
      asm.label(lbl2);
    }
  }
}
