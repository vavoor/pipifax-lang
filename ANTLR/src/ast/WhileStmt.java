package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;
import util.Labels;

public class WhileStmt extends Statement {
  private Expr cond;
  private Block block;
  
  public WhileStmt(Expr cond, Block block) {
    this.cond = cond;
    this.block = block;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.cond.resolveFunctionNames(functions);
    errors += this.block.resolveFunctionNames(functions);
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    errors += this.cond.calculateAndCheckTypes();
    errors += this.block.calculateAndCheckTypes();
    if (errors == 0 && !this.cond.type().isInt()) {
      System.err.println("Condition in while statement must be integer");
      errors++;
    }
    return errors;
  }

  public void generateCode(AsmWriter asm) {
    String lbl1 = Labels.label();
    String lbl2 = Labels.label();

    asm.label(lbl1);
    this.cond.generateCode(asm);
    asm.beqz(this.cond.result(), lbl2);
    this.cond.result().release();
    this.block.generateCode(asm);
    asm.j(lbl1);
    asm.label(lbl2);
  }
}
