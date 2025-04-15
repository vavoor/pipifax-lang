package ast;

import java.util.Map;
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

  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    return this.block.resolveNames(globals, functions);
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
