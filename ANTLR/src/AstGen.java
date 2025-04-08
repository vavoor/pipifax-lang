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

    for (PfxParser.FunctionDefinitionContext fc : ctx.functionDefinition()) {
      Function f = (Function) fc.accept(this);
      if (!program.addFunction(f)) {
        this.errors++;
      }
    }
    return program;
  }

  @Override
  public Node visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    String name = ctx.Name().getText();
    Type type = (Type) ctx.type().accept(this);
    GlobalVariable v = new GlobalVariable(name, type);
    return v;
  }

  @Override
  public Node visitFunctionDefinition(PfxParser.FunctionDefinitionContext ctx) {
    String name = ctx.Name().getText();
    Block block = (Block) ctx.block().accept(this);
    return new Function(name, block);
  }

  @Override
  public Node visitBlock(PfxParser.BlockContext ctx) {
    Block block = new Block();
    for (PfxParser.StatementContext s : ctx.statement()) {
      Statement stmt = (Statement) s.accept(this);
      block.addStmt(stmt);
    }
    return block;
  }

  @Override
  public Node visitAssignmentStmt(PfxParser.AssignmentStmtContext ctx) {
    LValue lvalue = (LValue) ctx.lvalue().accept(this);
    Expr rhs = (Expr) ctx.expr().accept(this);
    return new Assignment(lvalue, rhs);
  }

  @Override
  public Node visitCallStmt(PfxParser.CallStmtContext ctx) {
    String name = ctx.Name().getText();
    return new CallStmt(name);
  }

  @Override
  public Node visitAddExpr(PfxParser.AddExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    return new AddExpr(left, right);
  }

  @Override
  public Node visitIntLiteralExpr(PfxParser.IntLiteralExprContext ctx) {
    int value = Integer.parseInt(ctx.IntNumber().getText());
    return new IntLiteralExpr(value);
  }

  @Override
  public Node visitLValueExpr(PfxParser.LValueExprContext ctx) {
    LValue lvalue = (LValue) ctx.lvalue().accept(this);
    return new LValueExpr(lvalue);
  }

  @Override
  public Node visitIntType(PfxParser.IntTypeContext ctx) {
    return new IntType();
  }

  @Override
  public Node visitArrayType(PfxParser.ArrayTypeContext ctx) {
    int dim = Integer.parseInt(ctx.IntNumber().getText());
    Type type = (Type) ctx.type().accept(this);
    return new ArrayType(dim, type);
  }

  @Override
  public Node visitNamedLValue(PfxParser.NamedLValueContext ctx) {
    String name = ctx.Name().getText();
    return new NamedLValue(name);
  }

  @Override
  public Node visitIndexedLValue(PfxParser.IndexedLValueContext ctx) {
    LValue lvalue = (LValue) ctx.lvalue().accept(this);
    Expr index = (Expr) ctx.expr().accept(this);
    return new IndexedLValue(lvalue, index);
  }
  

  public int errors() {
    return this.errors;
  }
}
