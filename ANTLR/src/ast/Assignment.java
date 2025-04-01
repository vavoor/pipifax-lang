package ast;

import java.util.Map;
import util.AsmWriter;

public class Assignment extends Statement {
  private String name;
  private int value;
  
  private GlobalVariable variable;

  public Assignment(String name, int value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals) {
    this.variable = globals.get(this.name);
    if (this.variable == null) {
      System.err.println("Undeclared variable \'" + name + "\'");
      return 1;
    }
    return 0;
  }

  public void generateCode(AsmWriter asm) {
    asm.println("\tla t1," + this.name);
    asm.println("\tli t2," + this.value);
    asm.println("\tsw t2,0(t1)");
  }
}
