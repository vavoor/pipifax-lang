package ast;

import util.AsmWriter;

public class GlobalVariable extends Variable {

  public GlobalVariable(String name, Type type) {
    super(name, type);
  }
  
  @Override
  public void generateCode(AsmWriter asm) {
    asm.println(this.name() + ":\t.word 0");
  }
}
