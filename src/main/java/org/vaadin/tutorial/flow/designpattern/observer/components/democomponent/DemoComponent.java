package org.vaadin.tutorial.flow.designpattern.observer.components.democomponent;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.model.Result;
import org.vaadin.tutorial.flow.Registration;

import static java.util.Objects.nonNull;
import static org.vaadin.tutorial.flow.designpattern.observer.components.democomponent.DemoComponentRegistry.ValueEvent;

public class DemoComponent
    extends Composite<FormLayout>
    implements HasLogger {

  private final Checkbox  active       = new Checkbox(false);
  private final TextField input        = new TextField();
  private final Button    sendBtn      = new Button();
  private final TextField eventID      = new TextField("ID:");
  private final TextField eventMessage = new TextField("MSG:");


  private Result<Registration> registrationResult = Result.failure("not registered");

  public DemoComponent(String componentID) {
    setId(componentID);

    eventID.setReadOnly(true);
    eventMessage.setReadOnly(true);

    input.setLabel("msg to send");
    sendBtn.setText("send event");
    sendBtn.addClickListener(e -> {
      final String value = input.getValue();
      final String id = DemoComponent.this.getId()
                                          .orElse("");
      final ValueEvent valueEvent = new ValueEvent(id, value);

      fireCustomEvent(valueEvent);
    });

    active.setLabel("receiving events");
    active.addValueChangeListener(e -> {
      final Boolean isActive = e.getValue();
      if (isActive) registrationResult = Result.ofNullable(registerForEvents());
      else {
        registrationResult.ifPresent(Registration::remove);
        eventID.setValue("");
        eventMessage.setValue("");
      }
    });

    getContent().add(new Span("" + getId().get()), active, input, sendBtn, eventID, eventMessage);
  }

  private Registration registerForEvents() {
    return UI.getCurrent()
             .getSession()
             .getAttribute(DemoComponentRegistry.class)
             .register(valueEvent -> {
               if (nonNull(valueEvent.id()) && !valueEvent.id()
                                                          .equals(getId().orElse(""))) {
                 eventID.setValue(valueEvent.id());
                 eventMessage.setValue(valueEvent.value());
               }
             });
  }

  private void fireCustomEvent(ValueEvent valueEvent) {
    UI.getCurrent()
      .getSession()
      .getAttribute(DemoComponentRegistry.class)
      .sentEvent(valueEvent);
  }


}
