package ast;

import java.util.Map;
import util.AsmWriter;

public class Assignment extends Statement {
  private String name;
  private Expr rhs;
  
  private GlobalVariable variable;

  public Assignment(String name, Expr rhs) {
    this.name = name;
    this.rhs = rhs;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals) {
    int errors = this.rhs.resolveNames(globals);
    
    this.variable = globals.get(this.name);
    if (this.variable == null) {
      System.err.println("Undeclared variable \'" + name + "\'");
      errors++;
    }
    return errors;
  }

  public void generateCode(AsmWriter asm) {
    // We assume that values are handled in t2 and addresses are handled in t1
    this.rhs.generateCode(asm);
    asm.println("\tla t1," + this.name);
    asm.println("\tsw t2,0(t1)");
  }
}
