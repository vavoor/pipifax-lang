package util;

import java.io.PrintStream;
import java.io.FileNotFoundException;

public class AsmWriter {
  private PrintStream output;

  public AsmWriter(String fname) throws FileNotFoundException {
    this.output = new PrintStream(fname);
    this.output.println("# Pipifax compiler V1.0");
  }

  public void dataSection() {
    this.output.println("\n.data");
  }

  public void textSection() {
    this.output.println("\n.text");
  }

  public void println(String s) {
    this.output.println(s);
  }
}
