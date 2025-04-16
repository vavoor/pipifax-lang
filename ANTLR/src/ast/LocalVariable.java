package ast;

import util.AsmWriter;
import util.Registers;

public class LocalVariable extends Variable {

  public LocalVariable(String name, Type type) {
    super(name, type);
  }

  @Override
  public Registers.Register la(AsmWriter asm) {
    Registers.Register address = Registers.acquire();
    asm.addi(address, Registers.fp, offset() - this.offset);
    return address;
  }
}
