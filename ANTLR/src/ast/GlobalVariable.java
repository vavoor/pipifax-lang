package ast;

import util.AsmWriter;

public class GlobalVariable extends Node {
  private String name;

  public GlobalVariable(String name) {
    this.name = name;
  }

  public String name() {
    return this.name;
  }

  public void generateCode(AsmWriter asm) {
    asm.println(this.name + ":\t.word 0");
  }
}
