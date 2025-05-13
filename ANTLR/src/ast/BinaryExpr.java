package ast;

import java.util.Map;

public abstract class BinaryExpr extends Expr {
  protected Expr left;
  protected Expr right;

  public BinaryExpr(Expr left, Expr right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.left.resolveFunctionNames(functions) + this.right.resolveFunctionNames(functions);
    return errors;
  }
}
