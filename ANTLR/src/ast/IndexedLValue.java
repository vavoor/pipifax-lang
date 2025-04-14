package ast;

import java.util.Map;
import util.AsmWriter;
import util.Registers;

public class IndexedLValue extends LValue {
  private LValue base;
  private Expr index;

  public IndexedLValue(LValue base, Expr index) {
    this.base = base;
    this.index = index;
  }

  public Type type() {
    ArrayType a = (ArrayType) this.base.type();
    return a.baseType();
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    return this.base.resolveFunctionNames(functions) + this.index.resolveFunctionNames(functions);
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = this.index.calculateAndCheckTypes() + this.base.calculateAndCheckTypes();
    if (!this.index.type().isInt()) {
      System.err.println("Index must be integer type");
      errors++;
    }
    if (!this.base.type().isArray()) {
      System.err.println("Must be array type");
      errors++;
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    this.index.generateCode(asm);
    
    Registers.Register incr = Registers.acquire();
    ArrayType a = (ArrayType) this.base.type();
    asm.li(incr, a.baseType().size());
    asm.mul(this.index.result(), this.index.result(), incr);
    incr.release();

    this.base.generateCode(asm);
    asm.add(this.index.result(), this.base.address(), this.index.result());
    this.address = this.index.result();
    this.base.address().release();
  }
}
