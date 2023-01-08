package com.example.application.views.bookingonline;

import com.example.application.data.entity.Bookingonline;
import com.example.application.data.service.BookingonlineService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Booking Online")
@Route(value = "booking-online/:bookingonlineID?/:action?(edit)", layout = MainLayout.class)
public class BookingOnlineView extends Div implements BeforeEnterObserver {

    private final String BOOKINGONLINE_ID = "bookingonlineID";
    private final String BOOKINGONLINE_EDIT_ROUTE_TEMPLATE = "booking-online/%s/edit";

    private final Grid<Bookingonline> grid = new Grid<>(Bookingonline.class, false);

    private TextField namapenyewa;
    private TextField nohp;
    private TextField email;
    private DatePicker bookinguntuktanggal;
    private DateTimePicker jam;
    private TextField paket;
    private TextField buktidp;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Bookingonline> binder;

    private Bookingonline bookingonline;

    private final BookingonlineService bookingonlineService;

    public BookingOnlineView(BookingonlineService bookingonlineService) {
        this.bookingonlineService = bookingonlineService;
        addClassNames("booking-online-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("namapenyewa").setAutoWidth(true);
        grid.addColumn("nohp").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("bookinguntuktanggal").setAutoWidth(true);
        grid.addColumn("jam").setAutoWidth(true);
        grid.addColumn("paket").setAutoWidth(true);
        grid.addColumn("buktidp").setAutoWidth(true);
        grid.setItems(query -> bookingonlineService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(BOOKINGONLINE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(BookingOnlineView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Bookingonline.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.bookingonline == null) {
                    this.bookingonline = new Bookingonline();
                }
                binder.writeBean(this.bookingonline);
                bookingonlineService.update(this.bookingonline);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(BookingOnlineView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> bookingonlineId = event.getRouteParameters().get(BOOKINGONLINE_ID).map(Long::parseLong);
        if (bookingonlineId.isPresent()) {
            Optional<Bookingonline> bookingonlineFromBackend = bookingonlineService.get(bookingonlineId.get());
            if (bookingonlineFromBackend.isPresent()) {
                populateForm(bookingonlineFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested bookingonline was not found, ID = %s", bookingonlineId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(BookingOnlineView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        namapenyewa = new TextField("Namapenyewa");
        nohp = new TextField("Nohp");
        email = new TextField("Email");
        bookinguntuktanggal = new DatePicker("Bookinguntuktanggal");
        jam = new DateTimePicker("Jam");
        jam.setStep(Duration.ofSeconds(1));
        paket = new TextField("Paket");
        buktidp = new TextField("Buktidp");
        formLayout.add(namapenyewa, nohp, email, bookinguntuktanggal, jam, paket, buktidp);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Bookingonline value) {
        this.bookingonline = value;
        binder.readBean(this.bookingonline);

    }
}
