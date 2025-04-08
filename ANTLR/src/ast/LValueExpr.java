package ast;

import java.util.Map;
import util.AsmWriter;

public class LValueExpr extends Expr {
  private LValue lvalue;
  
  public LValueExpr(LValue lvalue) {
    this.lvalue = lvalue;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.lvalue.resolveFunctionNames(functions);
    this.type = this.lvalue.type();
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    this.type = this.lvalue.type();
    return 0;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.lvalue.generateCode(asm);
    asm.println("\tlw t1,0(t2)");
  }
}
