package ast;

public class IndexedLValue extends LValue {
  private LValue base;
  private Expr index;

  public IndexedLValue(LValue base, Expr index) {
    this.base = base;
    this.index = index;
  }
}
