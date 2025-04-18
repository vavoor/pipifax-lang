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

    if (this.rhs.type().isInt()) {
      asm.sw(this.rhs.result(), this.lvalue.address());
    }
    else if (this.rhs.type().isArray()) {
      asm.mv(Registers.a0, this.lvalue.address());
      asm.mv(Registers.a1, this.rhs.result());
      asm.li(Registers.a2, this.rhs.type().size());
      asm.jal("__memcpy"); // TODO: provide an implementation of __memcpy
    }
    else {
      throw new RuntimeException("Must not happen");
    }
    
    this.rhs.result().release();
    this.lvalue.address().release();
  }
}
