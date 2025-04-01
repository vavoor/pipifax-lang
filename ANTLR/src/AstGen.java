import ast.*;

public class AstGen extends PfxBaseVisitor<Node> {

  private int errors = 0;

  @Override
  public Node visitProgram(PfxParser.ProgramContext ctx) {
    Program program = new Program();

    for (PfxParser.GlobalVariableContext gv : ctx.globalVariable()) {
      GlobalVariable v = (GlobalVariable) gv.accept(this);
      if (!program.addGlobalVariable(v)) {
        this.errors++;
      }
    }

    for (PfxParser.StatementContext s : ctx.statement()) {
      Statement stmt = (Statement) s.accept(this);
      program.addStatement(stmt);
    }
    return program;
  }

  @Override
  public Node visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    GlobalVariable v = new GlobalVariable(ctx.Name().getText());
    return v;
  }

  @Override
  public Node visitAssignmentStmt(PfxParser.AssignmentStmtContext ctx) {
    String name = ctx.Name().getText();
    Expr rhs = (Expr) ctx.expr().accept(this);
    return new Assignment(name, rhs);
  }

  @Override
  public Node visitIntLiteralExpr(PfxParser.IntLiteralExprContext ctx) {
    int value = Integer.parseInt(ctx.IntNumber().getText());
    return new IntLiteralExpr(value);
  }

  @Override
  public Node visitVariableExpr(PfxParser.VariableExprContext ctx) {
    String name = ctx.Name().getText();
    return new VariableExpr(name);
  }

  public int errors() {
    return this.errors;
  }
}
