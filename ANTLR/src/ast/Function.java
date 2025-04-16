package ast;

import java.util.Map;
import java.util.Collection;
import util.AsmWriter;
import util.Registers;

public class Function extends Node {
  private String name;
  private Collection<LocalVariable> locals;
  private Collection<Parameter> parameters;
  private Block block;

  private int localsSize;

  public Function(String name, Collection<Parameter> parameters, Collection<LocalVariable> locals, Block block) {
    this.name = name;
    this.parameters = parameters;
    this.locals = locals;
    this.block = block;
  }

  public String name() {
    return this.name;
  }

  public String mangledName() {
    return "f_" + this.name;
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
  public void allocateMemory() {
    int offset = 8; // for ra and fp
    for (LocalVariable l : this.locals) {
      offset += l.type().size();
      l.setOffset(offset);
    }
    this.localsSize = offset;
  }

  @Override
  public  void generateCode(AsmWriter asm) {
    asm.comment("Function " + this.name);
    for (Variable l : locals) {
      asm.comment("var " + l.name() + "@" + l.offset());
    }
    asm.label(this.name);
    asm.addi(Registers.sp, Registers.sp, -frameSize());
    asm.sw(Registers.ra, frameSize() - 4, Registers.sp);
    asm.sw(Registers.fp, frameSize() - 8, Registers.sp);
    asm.addi(Registers.fp, Registers.sp, frameSize());
    asm.nl();
    block.generateCode(asm);
    asm.nl();
    asm.lw(Registers.fp, frameSize() - 8, Registers.sp);
    asm.lw(Registers.ra, frameSize() - 4, Registers.sp);
    asm.addi(Registers.sp, Registers.sp, frameSize());
    asm.ret();
  }

  private int frameSize() {
    return this.localsSize;
  }
}
