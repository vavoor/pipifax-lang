import ast.Node;
import ast.Program;
import ast.GlobalVariable;
import ast.Statement;
import ast.Assignment;

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
  public Node visitStatement(PfxParser.StatementContext ctx) {
    String name = ctx.Name().getText();
    int value = Integer.parseInt(ctx.IntNumber().getText());
    return new Assignment(name, value);
  }

  public int errors() {
    return this.errors;
  }
}
