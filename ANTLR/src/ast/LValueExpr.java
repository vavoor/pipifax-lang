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
    this.lvalue.generateCode(asm);
    if (this.type.isInt()) {
      this.register = this.lvalue.address();
      asm.lw(this.register, this.register);
    }
    else if (this.type.isArray()) {
      this.register = this.lvalue.address();
    }
    else {
      throw new RuntimeException("Must not happend");
    }
  }
}
