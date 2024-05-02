import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import java.io.IOException;

class Pfxc {

    public static void main(String[] args) {
        Pfxc pfxc = new Pfxc(args[0]);
    }

    private ParserRuleContext parseTree;

    private Pfxc(String input) {
        try {
            CharStream inputStream = CharStreams.fromFileName(input);
            PfxLexer lexer = new PfxLexer(inputStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            PfxParser parser = new PfxParser(tokenStream);
            parseTree = parser.program();
        }
        catch (IOException e) {
            System.err.println("Cannot open file " + input);
        }
    }
}
