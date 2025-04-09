package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;

public class LValueExpr extends Expr {
  private LValue lvalue;
  
  public LValueExpr(LValue lvalue) {
    this.lvalue = lvalue;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.lvalue.resolveFunctionNames(functions);
    this.type = this.lvalue.type();
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.lvalue.calculateAndCheckTypes();
    this.type = this.lvalue.type();
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    if (this.type.isInt()) {
      this.lvalue.generateCode(asm);
      this.register = this.lvalue.address();
      asm.println("\tlw " + this.register + ",0(" + this.register +")");
    }
    else if (this.type.isArray()) {
      throw new RuntimeException("Not yet implemented");
    }
    else {
      throw new RuntimeException("Must not happend");
    }
  }
}
