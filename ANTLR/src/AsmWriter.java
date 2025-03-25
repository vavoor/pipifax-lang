import java.io.FileWriter;
import java.io.IOException;

class AsmWriter {
  private FileWriter output;

  public AsmWriter(String fname) throws IOException {
    this.output = new FileWriter(fname + ".s");
  }
}
