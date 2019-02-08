package fr.darkxell.dataeditor.application.controls;

import java.util.Comparator;

import fr.darkxell.dataeditor.application.data.floortable.FloorTableItem;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class FloorTable {

    public static class FloorTableManager<T, D> {
        public MenuItem add, remove;
        public TableColumn<FloorTableItem<T, D>, String> floorColumn;
        public final TableView<FloorTableItem<T, D>> table;
        public TableColumn<FloorTableItem<T, D>, D> valueColumn;

        public FloorTableManager(TableView<FloorTableItem<T, D>> table) {
            this.table = table;
        }

        public void onFloorEdited(CellEditEvent<FloorTableItem<T, D>, String> event) {
            FloorTableItem<T, D> selected = this.table.getSelectionModel().getSelectedItem();
            String value = event.getNewValue();
            if (!value.matches("\\d+"))
                FXUtils.showAlert("Invalid floor: " + value);
            else if (value.equals("0"))
                FXUtils.showAlert("Bruh, above 0 please. What are you doing");
            else
                selected.floor = Integer.parseInt(value);
            this.sort();
        }

        public void onMenuShowing() {
            this.remove.setDisable(this.table.getSelectionModel().isEmpty());
        }

        public void onValueEdited(CellEditEvent<FloorTableItem<T, D>, D> event) {
            FloorTableItem<T, D> selected = this.table.getSelectionModel().getSelectedItem();
            selected.onValueEdited(event.getNewValue());
            this.table.refresh();
        }

        public void sort() {
            this.table.getItems().sort(Comparator.naturalOrder());
            this.table.refresh();
        }
    }

    private static <T, D> ContextMenu createContextMenu(TableView<FloorTableItem<T, D>> table, String objectName,
            FloorTableItem<T, D> defaultItem, FloorTableManager<T, D> manager) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(manager.add = new MenuItem("Add"));
        menu.getItems().add(manager.remove = new MenuItem("Delete"));

        manager.add.setOnAction(event -> {
            table.getItems().add(defaultItem.copy());
            manager.sort();
        });

        manager.remove.setOnAction(event -> {
            table.getItems().remove(table.getSelectionModel().getSelectedIndex());
        });

        menu.setOnShowing(event -> manager.onMenuShowing());

        return menu;
    }

    public static <T, D> FloorTableManager<T, D> setup(TableView<FloorTableItem<T, D>> table, String objectName,
            FloorTableItem<T, D> defaultItem) {
        table.setEditable(true);

        FloorTableManager<T, D> manager = new FloorTableManager<>(table);
        table.getColumns().add(manager.floorColumn = new TableColumn<>("Floor"));
        table.getColumns().add(manager.valueColumn = new TableColumn<>(objectName));

        manager.floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        manager.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        manager.floorColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        manager.valueColumn.setCellFactory(defaultItem.cellFactory());

        table.setPrefWidth(150);
        manager.floorColumn.setMinWidth(50);
        manager.floorColumn.setPrefWidth(50);
        manager.floorColumn.setMaxWidth(50);
        manager.valueColumn.setPrefWidth(-1);

        manager.floorColumn.setOnEditCommit(manager::onFloorEdited);
        manager.valueColumn.setOnEditCommit(manager::onValueEdited);

        table.setContextMenu(createContextMenu(table, objectName, defaultItem, manager));

        return manager;
    }

}
