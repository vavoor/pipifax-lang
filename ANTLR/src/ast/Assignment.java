package ast;

import java.util.Map;
import util.AsmWriter;

public class Assignment extends Statement {
  private LValue lvalue;
  private Expr rhs;
  
  private GlobalVariable variable;

  public Assignment(LValue lvalue, Expr rhs) {
    this.lvalue = lvalue;
    this.rhs = rhs;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    int errors = this.lvalue.resolveNames(globals, functions) + this.rhs.resolveNames(globals, functions);
    return errors;
  }

  @Override
  public int checkTypes() {
    return 0;
  }

  public void generateCode(AsmWriter asm) {
    // We assume that values are handled in t1 and addresses are handled in t2
    //~ this.rhs.generateCode(asm);
    //~ asm.println("\tla t2," + this.name);
    //~ asm.println("\tsw t1,0(t2)");
  }
}
