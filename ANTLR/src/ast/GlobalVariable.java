package ast;

import util.AsmWriter;
import util.Registers;

public class GlobalVariable extends Variable {

  public GlobalVariable(String name, Type type) {
    super(name, type);
  }

  @Override
  public Registers.Register la(AsmWriter asm) {
    Registers.GPRegister address = Registers.acquireGP();
    asm.la(address, this.mangledName());
    return address;
  }
  
  @Override
  public void generateCode(AsmWriter asm) {
    asm.println(this.mangledName() + ":\t.word 0");
  }
}
