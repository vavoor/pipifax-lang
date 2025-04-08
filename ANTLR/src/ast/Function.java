package ast;

import util.AsmWriter;

public class Function extends Node {
  private String name;
  private Block block;

  public Function(String name, Block block) {
    this.name = name;
    this.block = block;
  }

  public String name() {
    return this.name;
  }

  @Override
  public int checkTypes() {
    return this.block.checkTypes();
  }

  @Override
  public  void generateCode(AsmWriter asm) {
    asm.println("\n# Function " + this.name);
    asm.println(this.name + ":");
    asm.println("\taddi sp,sp,-4");
    asm.println("\tsw ra,0(sp)");
    block.generateCode(asm);
    asm.println("\tlw ra,0(sp)");
    asm.println("\taddi sp,sp,4");
    asm.println("\tret");
  }
}
