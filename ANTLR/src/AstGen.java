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
  private List<DoubleLiteralExpr> doubles = new ArrayList<>();
  private List<StringLiteralExpr> strings = new ArrayList<>();

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
    
    return new Program(globals, functions, doubles, strings);
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

    scopes.enter();
    List<Parameter> parameters = new ArrayList<>();
    for (PfxParser.ParamContext p : ctx.param()) {
      Parameter param = (Parameter) p.accept(this);
      parameters.add(param);
      if (scopes.insert(param.name(), param)) {
        System.err.println("Parameter \'" + param.name() + "\' is declared more than once.");
        this.errors++;
      }
    }

    Type type = (ctx.type() != null) ? (Type) ctx.type().accept(this) : VoidType.instance();
    Parameter ret = new Parameter(name, type);
    parameters.add(ret);
    if (scopes.insert(ret.name(), ret)) {
      System.err.println("Function name \'" + ret.name() + "\' mujst not be used as parameter name");
      this.errors++;
    }

    this.locals = new ArrayList<LocalVariable>();
    Block block = (Block) ctx.block().accept(this);
    
    scopes.leave();
    return new Function(name, type, parameters, this.locals, block);
  }

  @Override
  public Node visitBlock(PfxParser.BlockContext ctx) {
    scopes.enter();
    Block block = new Block();
    for (PfxParser.StatementOrDeclarationContext s : ctx.statementOrDeclaration()) {
      Node n = s.accept(this);
      if (n != null) {
        Statement stmt = (Statement) n;
        block.addStmt(stmt);
      }
    }
    scopes.leave();
    return block;
  }

  @Override
  public Node visitParam(PfxParser.ParamContext ctx) {
    String name = ctx.Name().getText();
    Type type = (Type) ctx.ptype().accept(this);
    return new Parameter(name, type);
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
    CallExpr call = (CallExpr) ctx.call().accept(this);
    return new CallStmt(call);
  }

  @Override
  public Node visitIfStmt(PfxParser.IfStmtContext ctx) {
    Expr cond = (Expr) ctx.expr().accept(this);
    Block ifTrue = (Block) ctx.block(0).accept(this);
    Block ifFalse;
    if (ctx.block(1) != null) {
      ifFalse = (Block) ctx.block(1).accept(this);
    }
    else {
      ifFalse = new Block();
    }
    return new IfStmt(cond, ifTrue, ifFalse);
  }

  @Override
  public Node visitWhileStmt(PfxParser.WhileStmtContext ctx) {
    Expr cond = (Expr) ctx.expr().accept(this);
    Block block = (Block) ctx.block().accept(this);
    return new WhileStmt(cond, block);
  }

  @Override
  public Node visitCall(PfxParser.CallContext ctx) {
    String name = ctx.Name().getText();
    List<Expr> args = new ArrayList<>();
    for (PfxParser.ExprContext e : ctx.expr()) {
      Expr expr = (Expr) e.accept(this);
      args.add(expr);
    }
    return new CallExpr(name, args);
  }

  @Override
  public Node visitAdditiveExpr(PfxParser.AdditiveExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    if (ctx.getChild(1).getText().equals("+")) {
      return new AddExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("-")) {
      return new SubExpr(left, right);
    }
    else {
      throw new RuntimeException("Unexpected operator");
    }
  }

  @Override
  public Node visitMultiplicativeExpr(PfxParser.MultiplicativeExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    if (ctx.getChild(1).getText().equals("*")) {
      return new MultExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("/")) {
      return new DivExpr(left, right);
    }
    else {
      throw new RuntimeException("Unexpected operator");
    }
  }

  @Override
  public Node visitComparativeExpr(PfxParser.ComparativeExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    if (ctx.getChild(1).getText().equals("<")) {
      return new LessThanExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("<=")) {
      return new LessOrEqualExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals(">")) {
      return new GreaterThanExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals(">=")) {
      return new GreaterOrEqualExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("==")) {
      return new EqualExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("!=")) {
      return new NotEqualExpr(left, right);
    }
    else if (ctx.getChild(1).getText().equals("<=>")) {
      return new StringCompareExpr(left, right);
    }
    else {
      throw new RuntimeException("Unexpected operator");
    }
  }

  @Override
  public Node visitAndExpr(PfxParser.AndExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    return new AndExpr(left, right);
  }
  
  @Override
  public Node visitOrExpr(PfxParser.OrExprContext ctx) {
    Expr left = (Expr) ctx.expr(0).accept(this);
    Expr right = (Expr) ctx.expr(1).accept(this);
    return new OrExpr(left, right);
  }

  @Override
  public Node visitNotExpr(PfxParser.NotExprContext ctx) {
    Expr expr = (Expr) ctx.expr().accept(this);
    return new NotExpr(expr);
  }

  @Override
  public Node visitNegExpr(PfxParser.NegExprContext ctx) {
    Expr expr = (Expr) ctx.expr().accept(this);
    return new NegExpr(expr);
  }

  @Override
  public Node visitIntCastExpr(PfxParser.IntCastExprContext ctx) {
    Expr expr = (Expr) ctx.expr().accept(this);
    return new IntCastExpr(expr);
  }

  @Override
  public Node visitDoubleCastExpr(PfxParser.DoubleCastExprContext ctx) {
    Expr expr = (Expr) ctx.expr().accept(this);
    return new DoubleCastExpr(expr);
  }

  @Override
  public Node visitBracketExpr(PfxParser.BracketExprContext ctx) {
    Expr expr = (Expr) ctx.expr().accept(this);
    return expr;
  }

  @Override
  public Node visitIntLiteralExpr(PfxParser.IntLiteralExprContext ctx) {
    int value = Integer.parseInt(ctx.IntNumber().getText());
    return new IntLiteralExpr(value);
  }

  @Override
  public Node visitDoubleLiteralExpr(PfxParser.DoubleLiteralExprContext ctx) {
    double value = Double.parseDouble(ctx.DoubleNumber().getText());
    DoubleLiteralExpr d = new DoubleLiteralExpr(value);
    doubles.add(d);
    return d;
  }

  @Override
  public Node visitStringLiteralExpr(PfxParser.StringLiteralExprContext ctx) {
    String value = ctx.StringLiteral().getText();
    StringLiteralExpr s = new StringLiteralExpr(value.substring(1, value.length() - 1));
    strings.add(s);
    return s;
  }

  @Override
  public Node visitLValueExpr(PfxParser.LValueExprContext ctx) {
    LValue lvalue = (LValue) ctx.lvalue().accept(this);
    return new LValueExpr(lvalue);
  }

  @Override
  public Node visitCallExpr(PfxParser.CallExprContext ctx) {
    CallExpr call = (CallExpr) ctx.call().accept(this);
    return call;
  }

  @Override
  public Node visitIntType(PfxParser.IntTypeContext ctx) {
    return IntType.instance();
  }

  @Override
  public Node visitDoubleType(PfxParser.DoubleTypeContext ctx) {
    return DoubleType.instance();
  }

  @Override
  public Node visitStringType(PfxParser.StringTypeContext ctx) {
    return StringType.instance();
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
