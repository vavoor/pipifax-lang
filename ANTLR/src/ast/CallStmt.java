package ast;

import java.util.Map;
import util.AsmWriter;

public class CallStmt extends Statement {
  private String name;
  private Function function;
  
  public CallStmt(String function) {
    this.name = function;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    this.function = functions.get(this.name);
    if (this.function == null) {
      System.err.println("Function \'" + this.name + "\' is called but not defined.");
      return 1;
    }
    return 0;
  }

  @Override
  public int checkTypes() {
    return 0;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.println("\tjal " + this.name);
  }
}
