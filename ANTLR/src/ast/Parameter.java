package ast;

import util.AsmWriter;
import util.Registers;

public class Parameter extends Variable {
  public Parameter(String name, Type type) {
    super(name, type);
  }

  @Override
  public Registers.GPRegister la(AsmWriter asm) {
    Registers.GPRegister address = Registers.acquireGP();
    asm.addi(address, Registers.fp, this.offset);
    return address;
  }
}
