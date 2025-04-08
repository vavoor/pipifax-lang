package ast;

import util.AsmWriter;

public class GlobalVariable extends Node {
  private String name;
  private Type type;

  public GlobalVariable(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  public String name() {
    return this.name;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.println(this.name + ":\t.word 0");
  }
}
