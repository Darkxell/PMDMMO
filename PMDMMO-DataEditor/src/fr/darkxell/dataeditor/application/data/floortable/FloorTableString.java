package fr.darkxell.dataeditor.application.data.floortable;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class FloorTableString extends FloorTableItem<String, String>
{

	public FloorTableString(int floor, String value)
	{
		super(floor, value);
	}

	@Override
	public Callback<TableColumn<FloorTableItem<String, String>, String>, TableCell<FloorTableItem<String, String>, String>> cellFactory()
	{
		return TextFieldTableCell.forTableColumn();
	}

	@Override
	public FloorTableItem<String, String> copy()
	{
		return new FloorTableString(this.floor, this.value);
	}

	@Override
	public String getValue()
	{
		return this.value;
	}

	@Override
	public void onValueEdited(String newValue)
	{
		this.value = newValue;
	}

}
