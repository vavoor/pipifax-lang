import ast.Node;
import ast.Program;
import ast.GlobalVariable;

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
    return program;
  }

  @Override
  public Node visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    GlobalVariable v = new GlobalVariable(ctx.Name().getText());
    return v;
  }

  public int errors() {
    return this.errors;
  }
}
