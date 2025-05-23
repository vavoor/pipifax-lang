package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;

public class Assignment extends Statement {
  private LValue lvalue;
  private Expr rhs;
  
  public Assignment(LValue lvalue, Expr rhs) {
    this.lvalue = lvalue;
    this.rhs = rhs;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.lvalue.resolveFunctionNames(functions) + this.rhs.resolveFunctionNames(functions);
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    errors += this.rhs.calculateAndCheckTypes();
    errors += this.lvalue.calculateAndCheckTypes();
    if (errors == 0 && !this.lvalue.type().accepts(this.rhs.type())) {
      System.err.println("Incompatible types in assignment");
      errors++;
    }
    return errors;
  }

  public void generateCode(AsmWriter asm) {
    this.rhs.generateCode(asm);
    this.lvalue.generateCode(asm);

    this.rhs.type().call(new Type.Operation() {
      public void forInt() {
        asm.sw(rhs.result(), lvalue.address());
      }

      public void forDouble() {
        asm.fsw(rhs.result(), lvalue.address());
      }

      public void forString() {
        asm.sw(rhs.result(), lvalue.address());
      }

      public void forArray() {
        asm.memcpy(lvalue.address(), rhs.result(), rhs.type().size());
      }
    });

    this.rhs.result().release();
    this.lvalue.address().release();
  }
}
