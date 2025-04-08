package ast;

public class NamedLValue extends LValue {
  private String name;

  public NamedLValue(String name) {
    this.name = name;
  }
}
