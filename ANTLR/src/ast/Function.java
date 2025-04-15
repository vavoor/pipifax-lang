package ast;

import java.util.Map;
import java.util.Collection;
import util.AsmWriter;

public class Function extends Node {
  private String name;
  Collection<LocalVariable> locals;
  private Block block;

  public Function(String name, Collection<LocalVariable> locals, Block block) {
    this.name = name;
    this.locals = locals;
    this.block = block;
  }

  public String name() {
    return this.name;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    return this.block.resolveFunctionNames(functions);
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
