package org.vaadin.tutorial.flow.designpattern.observer;


import com.github.appreciated.css.grid.sizes.Flex;
import com.github.appreciated.css.grid.sizes.Length;
import com.github.appreciated.css.grid.sizes.MinMax;
import com.github.appreciated.css.grid.sizes.Repeat.RepeatMode;
import com.github.appreciated.layout.FlexibleGridLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.rapidpm.frp.model.Result;
import org.vaadin.tutorial.flow.Registration;
import org.vaadin.tutorial.flow.designpattern.observer.components.democomponent.DemoComponent;
import org.vaadin.tutorial.flow.designpattern.observer.components.democomponent.DemoComponentRegistry;

import static com.github.appreciated.css.grid.GridLayoutComponent.AutoFlow;
import static com.github.appreciated.css.grid.GridLayoutComponent.Overflow;
import static java.util.Objects.nonNull;
import static org.vaadin.tutorial.flow.designpattern.observer.components.democomponent.DemoComponentRegistry.ValueEvent;

@Route("")
@Push
public class MainView
    extends Composite<Div> {

  public static final String ID_FORM_LAYOUT = "form-layout";
  public static final String ID_COMP_A      = "comp-a";
  public static final String ID_COMP_B      = "comp-b";
  public static final String ID_COMP_C      = "comp-c";
  public static final String ID_COMP_D      = "comp-d";
  public static final String ID_MAIN_VIEW   = "main view";

  private final DemoComponent compA = new DemoComponent(ID_COMP_A);
  private final DemoComponent compB = new DemoComponent(ID_COMP_B);
  private final DemoComponent compC = new DemoComponent(ID_COMP_C);
  private final DemoComponent compD = new DemoComponent(ID_COMP_D);


  private final Checkbox   active       = new Checkbox(false);
  private final TextField  input        = new TextField();
  private final Button     sendBtn      = new Button();
  private final TextField  eventID      = new TextField("ID:");
  private final TextField  eventMessage = new TextField("MSG:");
  private final FormLayout inputForm    = new FormLayout(new Span("Main View - Input"), active, input, sendBtn, eventID,
                                                         eventMessage) {{
    eventID.setReadOnly(true);
    eventMessage.setReadOnly(true);
  }};

  private final FlexibleGridLayout gridLayout = new FlexibleGridLayout().withColumns(RepeatMode.AUTO_FILL,
                                                                                     new MinMax(new Length("200px"),
                                                                                                new Flex(1)))
                                                                        .withPadding(true)
                                                                        .withSpacing(true)
                                                                        .withAutoFlow(AutoFlow.ROW_DENSE)
                                                                        .withOverflow(Overflow.AUTO)
                                                                        .withItems(inputForm)
                                                                        .withItems(compA)
                                                                        .withItems(compB)
                                                                        .withItems(compC)
                                                                        .withItems(compD);

  private Result<Registration> registrationResult = Result.failure("not registered");

  public MainView() {
    //connect components
    setId(ID_MAIN_VIEW);
    inputForm.setId(ID_FORM_LAYOUT);

    input.setLabel("msg to send");

    sendBtn.setText("send event");
    sendBtn.addClickListener(e -> {
      final String value = input.getValue();
      final String id = MainView.this.getId()
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
        registrationResult = Result.failure("not registered");
        eventID.setValue("");
        eventMessage.setValue("");
      }
    });


    gridLayout.setSizeFull();
    getContent().add(gridLayout);
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
