package ast;

import util.AsmWriter;
import util.Registers;

public class GlobalVariable extends Variable {

  public GlobalVariable(String name, Type type) {
    super(name, type);
  }

  @Override
  public Registers.Register la(AsmWriter asm) {
    Registers.Register address = Registers.acquire();
    asm.la(address, this.name);
    return address;
  }
  
  @Override
  public void generateCode(AsmWriter asm) {
    asm.println(this.name() + ":\t.word 0");
  }
}
