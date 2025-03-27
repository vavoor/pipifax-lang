import java.io.PrintStream;
import java.io.FileNotFoundException;

class AsmWriter {
  private PrintStream output;

  public AsmWriter(String fname) throws FileNotFoundException {
    this.output = new PrintStream(fname);
    this.output.println("# Pipifax compiler V1.0");
    this.output.println(".data");
    this.output.println(".text");
  }
}
