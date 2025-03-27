import java.util.Map;
import java.util.HashMap;

public class NameChecker extends PfxBaseVisitor<Void> {

  private Map<String, PfxParser.GlobalVariableContext> globalVariables = new HashMap<>();
  private int errorCount = 0;

  @Override
  public Void visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    String variableName = ctx.Name().getText();
    PfxParser.GlobalVariableContext previous = this.globalVariables.get(variableName);
    if (previous == null) {
      this.globalVariables.put(variableName, ctx);
    }
    else {
      errorCount++;
      System.err.println("Error: Global variable \'" + variableName + "\' is declared more than once.");
    }
    return null;
  }

  public int errors() {
    return errorCount;
  }
}
