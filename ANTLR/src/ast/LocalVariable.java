package ast;

import util.AsmWriter;

public class LocalVariable extends Variable {

  public LocalVariable(String name, Type type) {
    super(name, type);
  }
  
  @Override
  public void generateCode(AsmWriter asm) {
    // TODO
  }
}
