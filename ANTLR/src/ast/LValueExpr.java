package ast;

import java.util.Map;
import util.AsmWriter;

public class LValueExpr extends Expr {
  private LValue lvalue;
  
  private GlobalVariable variable;

  public LValueExpr(LValue lvalue) {
    this.lvalue = lvalue;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    return this.lvalue.resolveNames(globals, functions);
  }

  @Override
  public void generateCode(AsmWriter asm) {
    //~ asm.println("\tla t1," + this.name);
    //~ asm.println("\tlw t1,0(t1)");
  }
}
