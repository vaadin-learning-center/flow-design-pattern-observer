package org.vaadin.tutorial.flow.designpattern.observer.components.democomponent;

import org.rapidpm.frp.model.serial.Pair;
import org.vaadin.tutorial.flow.Registry;

public class DemoComponentRegistry
    extends Registry<DemoComponentRegistry.ValueEvent> {

  public static class ValueEvent
      extends Pair<String, String> {

    public ValueEvent(String id, String value) {
      super(id, value);
    }

    public String id() {
      return getT1();
    }

    public String value() {
      return getT2();
    }
  }
}
