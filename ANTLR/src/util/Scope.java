package util;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.LinkedHashMap;

public class Scope<T> {

  private Deque<Map<String, T>> scopes = new ArrayDeque<>();

  public void enter() {
    Map<String, T> scope = new LinkedHashMap<>();
    this.scopes.addFirst(scope);
  }

  public void leave() {
    this.scopes.removeFirst();
  }

  /**
   * @return true on error
   */
  public boolean insert(String name, T value) {
    Map<String, T> scope = this.scopes.getFirst();
    return scope.put(name, value) != null;
  }

  public T lookup(String name) {
    for (Map<String, T> scope : this.scopes) {
      T t = scope.get(name);
      if (t != null) {
        return t;
      }
    }
    return null;
  }
}
