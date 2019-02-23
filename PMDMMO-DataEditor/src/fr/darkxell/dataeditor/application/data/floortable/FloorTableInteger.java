package fr.darkxell.dataeditor.application.data.floortable;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import fr.darkxell.dataeditor.application.util.FXUtils;

public class FloorTableInteger extends FloorTableItem<Integer, String> {

    public FloorTableInteger(int floor, Integer value) {
        super(floor, value);
    }

    @Override
    public Callback<TableColumn<FloorTableItem<Integer, String>, String>, TableCell<FloorTableItem<Integer, String>, String>> cellFactory() {
        return TextFieldTableCell.forTableColumn();
    }

    @Override
    public FloorTableInteger copy() {
        return new FloorTableInteger(this.floor, this.value);
    }

    @Override
    public String getValue() {
        return String.valueOf(this.value);
    }

    @Override
    public void onValueEdited(String value) {
        if (!value.matches("\\d+"))
            FXUtils.showAlert("Invalid integer: " + value);
        else
            this.value = Integer.parseInt(value);
    }

}
