package ast;

import java.util.Map;
import util.AsmWriter;

public class VariableExpr extends Expr {
  private String name;
  
  private GlobalVariable variable;

  public VariableExpr(String name) {
    this.name = name;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    this.variable = globals.get(this.name);
    if (this.variable == null) {
      System.err.println("Undeclared variable \'" + this.name + "\'");
      return 1;
    }
    return 0;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.println("\tla t1," + this.name);
    asm.println("\tlw t1,0(t1)");
  }
}
