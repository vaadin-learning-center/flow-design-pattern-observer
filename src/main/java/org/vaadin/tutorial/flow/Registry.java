package org.vaadin.tutorial.flow;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Registry<VALUE> {

  private final Set<Consumer<VALUE>> listeners = ConcurrentHashMap.newKeySet();

  public Registration register(Consumer<VALUE> listener) {
    listeners.add(listener);
    return () -> listeners.remove(listener);
  }

  public void sentEvent(VALUE event) {
    listeners.forEach(listener -> listener.accept(event));
  }

}
