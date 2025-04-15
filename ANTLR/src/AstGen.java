import java.util.List;
import java.util.ArrayList;

import ast.*;
import util.Scope;


public class AstGen extends PfxBaseVisitor<Node> {

  private int errors = 0;

  private Scope<Variable> scopes = new Scope<>();
  private List<GlobalVariable> globals = new ArrayList<>();
  private List<LocalVariable> locals;
  private List<Function> functions = new ArrayList<>();

  @Override
  public Node visitProgram(PfxParser.ProgramContext ctx) {
    scopes.enter(); // create the global scope
    
    for (PfxParser.GlobalVariableContext gv : ctx.globalVariable()) {
      GlobalVariable v = (GlobalVariable) gv.accept(this);
      this.globals.add(v);
    }

    for (PfxParser.FunctionDefinitionContext fc : ctx.functionDefinition()) {
      Function f = (Function) fc.accept(this);
      this.functions.add(f);
    }

    scopes.leave();
    
    return new Program(globals, functions);
  }

  @Override
  public Node visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    String name = ctx.Name().getText();
    Type type = (Type) ctx.type().accept(this);
    GlobalVariable v = new GlobalVariable(name, type);
    
    if (this.scopes.insert(name, v)) {
      System.err.println("Global variable \'" + name + "\' is declared more than once.");
      this.errors++;
    }
    
    return v;
  }

  @Override
  public Node visitFunctionDefinition(PfxParser.FunctionDefinitionContext ctx) {
    String name = ctx.Name().getText();
    this.locals = new ArrayList<LocalVariable>();
    Block block = (Block) ctx.block().accept(this);
    return new Function(name, locals, block);
  }

  @Override
  public Node visitBlock(PfxParser.BlockContext ctx) {
    Block block = new Block();
    for (PfxParser.StatementOrDeclarationContext s : ctx.statementOrDeclaration()) {
      Node n = s.accept(this);
      if (n != null) {
        Statement stmt = (Statement) n;
        block.addStmt(stmt);
      }
    }
    return block;
  }

  @Override
  public Node visitLocalVariable(PfxParser.LocalVariableContext ctx) {
    String name = ctx.Name().getText();
    Type type = (Type) ctx.type().accept(this);
    LocalVariable v = new LocalVariable(name, type);
    
    if (this.scopes.insert(v.name(), v)) {
      System.err.println("Local variable \'" + name + "\' is declared more than once.");
      this.errors++;
    }
    this.locals.add(v);
    
    return null;
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
    return IntType.instance();
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
    Variable v = this.scopes.lookup(name);
    if (v == null) {
      System.err.println("Variable \'" + name + "\' is not declared.");
      errors++;
    }
    return new NamedLValue(name, v);
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
