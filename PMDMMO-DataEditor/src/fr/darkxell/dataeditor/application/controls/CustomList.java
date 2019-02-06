package fr.darkxell.dataeditor.application.controls;

import java.util.Optional;

import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class CustomList {

    private static class CustomListMenuHandler<T> {
        private MenuItem edit, rename, delete, create, top, bottom;
        private ListView<T> list;

        public CustomListMenuHandler(ListView<T> list) {
            this.list = list;
            this.list.getSelectionModel().selectedIndexProperty()
                    .addListener((observer, oldValue, newValue) -> this.onItemChanged(newValue));
        }

        private ContextMenu createMenu(ListCellParent<T> parent, String typeName, boolean edit, boolean rename,
                boolean delete, boolean create, boolean order) {
            ContextMenu menu = new ContextMenu();
            this.create = new MenuItem("New " + typeName + "...");
            this.edit = new MenuItem("Edit");
            this.rename = new MenuItem("Rename");
            this.delete = new MenuItem("Delete");
            this.top = new MenuItem("Move to top");
            this.bottom = new MenuItem("Move to bottom");

            if (create) {
                this.create.setOnAction(e -> {
                    parent.onCreate(null);
                });
                menu.getItems().add(this.create);
            }

            if (edit) {
                this.edit.setOnAction(e -> {
                    parent.onEdit(list.getSelectionModel().getSelectedItem());
                });
                this.edit.setDisable(true);
                menu.getItems().add(this.edit);
            }

            if (rename) {
                this.rename.setOnAction(e -> {
                    TextInputDialog dialog = new TextInputDialog("");
                    dialog.setTitle("Rename " + typeName);
                    dialog.setHeaderText(null);
                    dialog.setContentText("Type in the new name for " + typeName + " '"
                            + list.getSelectionModel().getSelectedItem() + "'.");
                    Optional<String> name = dialog.showAndWait();
                    if (name.isPresent())
                        parent.onRename(list.getSelectionModel().getSelectedItem(), name.get());
                });

                this.rename.setDisable(true);
                menu.getItems().add(this.rename);
            }

            if (order) {
                this.top.setOnAction(e -> {
                    this.move(parent, list, list.getSelectionModel().getSelectedItem(),
                            -list.getSelectionModel().getSelectedIndex());
                });
                this.bottom.setOnAction(e -> {
                    this.move(parent, list, list.getSelectionModel().getSelectedItem(),
                            list.getItems().size() - list.getSelectionModel().getSelectedIndex());
                });
                this.top.setDisable(true);
                this.bottom.setDisable(true);

                menu.getItems().add(top);
                menu.getItems().add(bottom);
            }

            if (delete) {
                this.delete.setOnAction(e -> {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("Delete " + typeName);
                    alert.setContentText("Are you sure you want to delete " + typeName + " '"
                            + list.getSelectionModel().getSelectedItem() + "'?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK)
                        parent.onDelete(list.getSelectionModel().getSelectedItem());
                });
                this.delete.setDisable(true);
                menu.getItems().add(this.delete);
            }

            return menu;
        }

        private void move(ListCellParent<T> parent, ListView<T> list, T item, int indexMove) {
            if (indexMove == 0)
                return;
            ObservableList<T> items = list.getItems();
            int oldIndex = items.indexOf(item);
            int newIndex = oldIndex + indexMove;
            items.add(newIndex, item);
            items.remove(oldIndex + (indexMove < 0 ? 1 : 0));
            list.getSelectionModel().select(item);

            parent.onMove(item, items.indexOf(item));
        }

        private void onItemChanged(Number index) {
            boolean isNull = index.intValue() == -1;
            this.edit.setDisable(isNull);
            this.rename.setDisable(isNull);
            this.delete.setDisable(isNull);
            this.create.setDisable(isNull);
            this.top.setDisable(isNull || index.intValue() == 0);
            this.bottom.setDisable(isNull || index.intValue() == this.list.getItems().size() - 1);
        }
    }

    public static <T> void setup(ListCellParent<T> parent, ListView<T> list, String typeName, boolean edit,
            boolean rename, boolean delete, boolean create, boolean order) {
        list.setCellFactory(param -> new CustomListCell<>(parent, order));
        list.setContextMenu(
                new CustomListMenuHandler<>(list).createMenu(parent, typeName, edit, rename, delete, create, order));
    }

}
