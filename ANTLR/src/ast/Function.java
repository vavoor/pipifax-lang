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

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    return 0; // TODO
  }
  
  @Override
  public int calculateAndCheckTypes() {
    return this.block.calculateAndCheckTypes();
  }

  @Override
  public  void generateCode(AsmWriter asm) {
    asm.comment("Function " + this.name);
    asm.label(this.name);
    asm.instr("addi sp,sp,-4");
    asm.instr("sw ra,0(sp)");
    block.generateCode(asm);
    asm.instr("lw ra,0(sp)");
    asm.instr("addi sp,sp,4");
    asm.ret();
  }
}
