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
  public void generateCode(AsmWriter asm) {
    this.index.generateCode(asm);
    
    Registers.Register incr = Registers.acquire();
    ArrayType a = (ArrayType) this.base.type();
    asm.println("\tli " + incr + "," + a.baseType().size());
    asm.println("\tmul " + this.index.result() + "," + this.index.result() + "," + incr);
    incr.release();

    this.base.generateCode(asm);
    asm.println("\tadd " + this.index.result() + "," + this.base.address() + "," + this.index.result());
    this.address = this.index.result();
    this.base.address().release();
  }
}
