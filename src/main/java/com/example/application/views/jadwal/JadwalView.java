package com.example.application.views.jadwal;

import com.example.application.data.entity.Jadwal;
import com.example.application.data.service.JadwalService;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Jadwal")
@Route(value = "jadwal/:jadwalID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class JadwalView extends Div implements BeforeEnterObserver {

    private final String JADWAL_ID = "jadwalID";
    private final String JADWAL_EDIT_ROUTE_TEMPLATE = "jadwal/%s/edit";

    private final Grid<Jadwal> grid = new Grid<>(Jadwal.class, false);

    private TextField namapembooking;
    private TextField nohp;
    private DatePicker bookinguntuktanggal;
    private DateTimePicker jam;
    private TextField jumlahdp;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Jadwal> binder;

    private Jadwal jadwal;

    private final JadwalService jadwalService;

    public JadwalView(JadwalService jadwalService) {
        this.jadwalService = jadwalService;
        addClassNames("jadwal-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("namapembooking").setAutoWidth(true);
        grid.addColumn("nohp").setAutoWidth(true);
        grid.addColumn("bookinguntuktanggal").setAutoWidth(true);
        grid.addColumn("jam").setAutoWidth(true);
        grid.addColumn("jumlahdp").setAutoWidth(true);
        grid.setItems(query -> jadwalService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(JADWAL_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(JadwalView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Jadwal.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.jadwal == null) {
                    this.jadwal = new Jadwal();
                }
                binder.writeBean(this.jadwal);
                jadwalService.update(this.jadwal);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(JadwalView.class);
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
        Optional<Long> jadwalId = event.getRouteParameters().get(JADWAL_ID).map(Long::parseLong);
        if (jadwalId.isPresent()) {
            Optional<Jadwal> jadwalFromBackend = jadwalService.get(jadwalId.get());
            if (jadwalFromBackend.isPresent()) {
                populateForm(jadwalFromBackend.get());
            } else {
                Notification.show(String.format("The requested jadwal was not found, ID = %s", jadwalId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(JadwalView.class);
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
        namapembooking = new TextField("Namapembooking");
        nohp = new TextField("Nohp");
        bookinguntuktanggal = new DatePicker("Bookinguntuktanggal");
        jam = new DateTimePicker("Jam");
        jam.setStep(Duration.ofSeconds(1));
        jumlahdp = new TextField("Jumlahdp");
        formLayout.add(namapembooking, nohp, bookinguntuktanggal, jam, jumlahdp);

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

    private void populateForm(Jadwal value) {
        this.jadwal = value;
        binder.readBean(this.jadwal);

    }
}
