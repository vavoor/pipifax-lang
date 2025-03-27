import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import java.io.IOException;

/**
 * The Pipifax compiler
 */
class Pfxc {

  public static void main(String[] args) {
    Pfxc pfxc = new Pfxc(args[0]);
  }

  private String inputFileName;

  private Pfxc(String fname) {
    this.inputFileName = fname;
  }

  private void compile() {
    try {
      PfxLexer lexer = new PfxLexer(CharStreams.fromFileName(this.inputFileName));
      PfxParser parser = new PfxParser(new CommonTokenStream(lexer));
      ParserRuleContext parseTree = parser.program();
      AsmWriter asm = new AsmWriter(baseName() + ".s");
    }
    catch (IOException e) {
      System.err.println("Cannot open file");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String baseName() {
    String baseName;
    int dot = this.inputFileName.lastIndexOf('.');
    if (dot > 0) {
      baseName = this.inputFileName.substring(0, dot);
    }
    else {
      baseName = this.inputFileName;
    }
    return baseName;
  }
}
