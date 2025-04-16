package ast;

import util.AsmWriter;
import util.Registers;

public class Parameter extends Variable {
  public Parameter(String name, Type type) {
    super(name, type);
  }

  @Override
  public Registers.Register la(AsmWriter asm) {
    Registers.Register address = Registers.acquire();
    asm.addi(address, Registers.fp, this.offset);
    return address;
  }
}
