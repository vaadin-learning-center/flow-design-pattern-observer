package org.vaadin.tutorial;

import org.apache.commons.cli.ParseException;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.nano.CoreUIServiceJava;

import static org.rapidpm.vaadin.nano.CoreUIServiceJava.CORE_UI_BASE_PKG;

public class WebRunner
    implements HasLogger {

  private WebRunner() {
  }

  public static void main(String[] args) throws ParseException {
    //Persistence

    System.setProperty(CORE_UI_BASE_PKG, "org.vaadin");
    new CoreUIServiceJava().executeCLI(args)
                           .startup();
  }

}
