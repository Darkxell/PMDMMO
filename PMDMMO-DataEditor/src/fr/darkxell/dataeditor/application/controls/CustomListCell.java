package fr.darkxell.dataeditor.application.controls;

import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class CustomListCell<T> extends ListCell<T>
{

	public static interface ListCellParent<T>
	{

		Node graphicFor(T item);

		void onCreate(T nullItem);

		void onDelete(T item);

		void onEdit(T item);

		void onMove(T item, int newIndex);

		void onRename(T item, String name);

	}

	public final ListCellParent<T> parent;

	public CustomListCell(ListCellParent<T> parent)
	{
		this.parent = parent;
	}

	@Override
	protected void updateItem(T item, boolean empty)
	{
		super.updateItem(item, empty);
		this.setText(empty ? "" : item.toString());
		this.setGraphic(this.parent.graphicFor(item));
	}

}
