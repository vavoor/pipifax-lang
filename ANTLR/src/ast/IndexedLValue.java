package ast;

import java.util.Map;

public class IndexedLValue extends LValue {
  private LValue base;
  private Expr index;

  public IndexedLValue(LValue base, Expr index) {
    this.base = base;
    this.index = index;
  }

  @Override
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    return this.base.resolveNames(globals, functions) + this.index.resolveNames(globals, functions);
  }
}
