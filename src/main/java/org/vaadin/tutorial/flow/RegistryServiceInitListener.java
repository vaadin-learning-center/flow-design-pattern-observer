package org.vaadin.tutorial.flow;

import com.vaadin.flow.server.*;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.vaadin.tutorial.flow.designpattern.observer.components.democomponent.DemoComponentRegistry;

public class RegistryServiceInitListener
    implements VaadinServiceInitListener, UIInitListener, HasLogger {
  @Override
  public void serviceInit(ServiceInitEvent serviceInitEvent) {
serviceInitEvent.getSource().addUIInitListener(this);
  }


  @Override
  public void uiInit(UIInitEvent uiInitEvent) {
    final VaadinSession session = uiInitEvent.getUI()
                                             .getSession();
    logger().info("uiInit - session is " + session);
    session.setAttribute(DemoComponentRegistry.class, null);
    session.setAttribute(DemoComponentRegistry.class, new DemoComponentRegistry());
  }
}
