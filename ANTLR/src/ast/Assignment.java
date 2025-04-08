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
    //~ int errors = this.rhs.resolveNames(globals, functions);
    
    //~ this.variable = globals.get(this.name);
    //~ if (this.variable == null) {
      //~ System.err.println("Undeclared variable \'" + name + "\'");
      //~ errors++;
    //~ }
    //~ return errors;
    return 0;
  }

  public void generateCode(AsmWriter asm) {
    // We assume that values are handled in t1 and addresses are handled in t2
    //~ this.rhs.generateCode(asm);
    //~ asm.println("\tla t2," + this.name);
    //~ asm.println("\tsw t1,0(t2)");
  }
}
