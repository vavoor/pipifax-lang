package ast;

import java.util.Map;
import util.AsmWriter;

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
    // TODO
    this.index.generateCode(asm);
    asm.println("\tli t2," + this.base.type().size());
    asm.println("\tmul t1,t1,t2");
    this.base.generateCode(asm);
  }
}
