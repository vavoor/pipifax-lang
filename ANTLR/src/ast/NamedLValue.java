package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;

public class NamedLValue extends LValue {
  private String name;
  private Variable variable;

  public NamedLValue(String name, Variable v) {
    this.name = name;
    this.variable = v;
  }

  public Type type() {
    Type t = this.variable.type();
    if (t.isReference()) {
      ReferenceType r = (ReferenceType) t;
      return r.base();
    }
    else {
      return t;
    }
  }

  @Override
  public void generateCode(AsmWriter asm) {
    Type t = this.variable.type();
    if (t.isReference()) {
      this.address = this.variable.la(asm);
      asm.lw(this.address, this.address);
    }
    else {
      this.address = this.variable.la(asm);
    }
  }

  public Variable variable() {
    return this.variable;
  }
}
