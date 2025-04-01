package ast;

import util.AsmWriter;

public class Assignment extends Statement {
  private String variable;
  private int value;

  public Assignment(String name, int value) {
    this.variable = name;
    this.value = value;
  }

  public void generateCode(AsmWriter asm) {
    asm.println("\tla t1," + this.variable);
    asm.println("\tli t2," + this.value);
    asm.println("\tsw t2,0(t1)");
  }
}
