package ast;

import java.util.Map;

public class NamedLValue extends LValue {
  private String name;
  private GlobalVariable variable;

  public NamedLValue(String name) {
    this.name = name;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    this.variable = globals.get(this.name);
    if (this.variable == null) {
      System.err.println("Undeclared variable \'" + name + "\'");
     return 1;
    }
    return 0;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.println("\tla t2," + this.name);
  }
}
