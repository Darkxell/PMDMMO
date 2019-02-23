package fr.darkxell.dataeditor.application.data.floortable;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import fr.darkxell.dataeditor.application.util.FXUtils;

public class FloorTableBoolean extends FloorTableItem<Boolean, String> {

    public FloorTableBoolean(int floor, Boolean value) {
        super(floor, value);
    }

    @Override
    public Callback<TableColumn<FloorTableItem<Boolean, String>, String>, TableCell<FloorTableItem<Boolean, String>, String>> cellFactory() {
        return TextFieldTableCell.forTableColumn();
    }

    @Override
    public FloorTableItem<Boolean, String> copy() {
        return new FloorTableBoolean(this.floor, this.value);
    }

    @Override
    public String getValue() {
        return String.valueOf(this.value);
    }

    @Override
    public void onValueEdited(String newValue) {
        boolean isTrue = false, isFalse = false;

        if (newValue.equals("0"))
            isFalse = true;
        if (newValue.toLowerCase().startsWith("f"))
            isFalse = true;
        if (newValue.equals("1"))
            isTrue = true;
        if (newValue.toLowerCase().startsWith("t"))
            isTrue = true;

        if (isTrue)
            this.value = true;
        else if (isFalse)
            this.value = false;
        else
            FXUtils.showAlert("Invalid boolean: " + value);
    }

}
