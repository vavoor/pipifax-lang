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
    return this.variable.type();
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.address = this.variable.la(asm);
  }

  public Variable variable() {
    return this.variable;
  }
}
