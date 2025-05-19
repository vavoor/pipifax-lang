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
    this.type.call(new Type.Operation() {
      public void forInt() {
        register = lvalue.address();
        asm.lw(register, register);
      }

      public void forDouble() {
        register = Registers.acquireFP();
        asm.flw(register, lvalue.address());
        lvalue.address().release();
      }

      public void forString() {
        register = lvalue.address();
      }

      public void forArray() {
        register = lvalue.address();
      }
    });
  }
}
