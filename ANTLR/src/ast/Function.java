package ast;

import java.util.Map;
import java.util.List;
import util.AsmWriter;
import util.Registers;

public class Function extends Node {
  private String name;
  private Type type;
  private List<LocalVariable> locals;
  private List<Parameter> parameters;
  private Block block;

  private int paramsSize;
  private int localsSize;

  public Function(String name, Type type, List<Parameter> parameters, List<LocalVariable> locals, Block block) {
    this.name = name;
    this.type = type;
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
  
  public Type type() {
    return this.type;
  }

  public List<Parameter> parameters() {
    return this.parameters;
  }

  public int parametersSize() {
    return this.paramsSize;
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
    asm.nl();
    asm.comment("Function " + this.name);
    for (Parameter p : this.parameters) {
      asm.comment("param " + p.name() + " @ fp+" + p.offset());
    }
    for (Variable l : this.locals) {
      asm.comment("var " + l.name() + " @ fp-" + l.offset());
    }
    asm.label(this.mangledName());
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

  public void allocateMemory() {
    int offset = 0;
    for (Parameter p : this.parameters) {
      p.setOffset(offset);
      offset += p.type().size();
    }
    this.paramsSize = offset;
    
    offset = 8; // for ra and fp
    for (LocalVariable l : this.locals) {
      offset += l.type().size();
      l.setOffset(offset);
    }
    this.localsSize = offset;
  }

  private int frameSize() {
    return this.localsSize;
  }
}
