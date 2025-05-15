package ast;

import java.util.Map;

public class UnaryExpr extends Expr {
  protected Expr expr;

  public UnaryExpr(Expr expr) {
    this.expr = expr;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = this.expr.resolveFunctionNames(functions);
    return errors;
  }
}
