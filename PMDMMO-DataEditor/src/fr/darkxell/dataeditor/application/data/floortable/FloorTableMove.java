package fr.darkxell.dataeditor.application.data.floortable;

import java.util.ArrayList;

import com.darkxell.common.Registries;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;

import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

public class FloorTableMove extends FloorTableItem<Move, Move>
{

	public FloorTableMove(int floor, Move value)
	{
		super(floor, value);
	}

	@Override
	public Callback<TableColumn<FloorTableItem<Move, Move>, Move>, TableCell<FloorTableItem<Move, Move>, Move>> cellFactory()
	{
		ArrayList<Move> moves = Registries.moves().toList();
		moves.removeIf(move -> move.id < 0);
		return ComboBoxTableCell.forTableColumn(FXCollections.observableList(moves));
	}

	@Override
	public FloorTableItem<Move, Move> copy()
	{
		return new FloorTableMove(this.floor, this.value);
	}

	@Override
	public Move getValue()
	{
		return this.value;
	}

	@Override
	public void onValueEdited(Move newValue)
	{
		this.value = newValue;
	}

}
