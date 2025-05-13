package util;

public class Labels {
  private static int id = 0;

  private Labels() {
  }

  public static String label() {
    id++;
    return "_L" + id;
  }
}
