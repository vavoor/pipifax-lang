package ast;

import java.util.Map;
import util.AsmWriter;

public class CallStmt extends Statement {
  private CallExpr call;
  
  public CallStmt(CallExpr call) {
    this.call = call;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    return call.resolveFunctionNames(functions);
  }

  @Override
  public int calculateAndCheckTypes() {
    return call.calculateAndCheckTypes();
  }

  @Override
  public void generateCode(AsmWriter asm) {
    call.generateCode(asm);
    call.result().release();
  }
}
