package fr.darkxell.dataeditor.application.controls;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class CustomListCell<T> extends ListCell<T>
{

	public static interface ListCellParent<T>
	{

		Node graphicFor(T item);

		void onCreate(T nullItem);

		void onDelete(T item);

		void onEdit(T item);

		void onRename(T item, String name);

	}

	public boolean canEdit = true, canRename = true, canDelete = true, canCreate = true, canOrder = true;
	public final ListCellParent<T> parent;
	public final String typeName;

	public CustomListCell(ListCellParent<T> parent, String typeName)
	{
		this.parent = parent;
		this.typeName = typeName;
	}

	private ContextMenu menu(T item, boolean empty)
	{
		ContextMenu menu = new ContextMenu();

		if (this.canCreate)
		{
			MenuItem add = new MenuItem("New " + this.typeName + "...");
			add.setOnAction(e -> {
				parent.onCreate(null);
			});
			menu.getItems().add(add);
		}

		if (!empty)
		{
			if (this.canEdit)
			{
				MenuItem edit = new MenuItem("Edit");
				edit.setOnAction(e -> {
					parent.onEdit(item);
				});
				menu.getItems().add(edit);
			}

			if (this.canRename)
			{
				MenuItem rename = new MenuItem("Rename");
				rename.setOnAction(e -> {
					TextInputDialog dialog = new TextInputDialog("");
					dialog.setTitle("Rename " + typeName);
					dialog.setHeaderText(null);
					dialog.setContentText("Type in the new name for " + typeName + " '" + item + "'.");
					Optional<String> name = dialog.showAndWait();
					if (name.isPresent()) parent.onRename(item, name.get());
				});

				menu.getItems().add(rename);
			}

			if (this.canOrder)
			{
				Menu order = new Menu("Order");
				MenuItem top = new MenuItem("Move to top");
				top.setOnAction(e -> {
					this.move(item, -this.getListView().getItems().indexOf(item));
				});
				MenuItem up = new MenuItem("Move up");
				up.setOnAction(e -> {
					this.move(item, -1);
				});
				MenuItem down = new MenuItem("Move down");
				down.setOnAction(e -> {
					this.move(item, 2);
				});
				MenuItem bottom = new MenuItem("Move to bottom");
				bottom.setOnAction(e -> {
					this.move(item, this.getListView().getItems().size() - this.getListView().getItems().indexOf(item));
				});

				top.setDisable(this.getListView().getItems().indexOf(item) == 0);
				up.setDisable(top.isDisable());
				bottom.setDisable(this.getListView().getItems().indexOf(item) == this.getListView().getItems().size() - 1);
				down.setDisable(bottom.isDisable());

				order.getItems().add(top);
				order.getItems().add(up);
				order.getItems().add(down);
				order.getItems().add(bottom);
				menu.getItems().add(order);
			}

			if (this.canDelete)
			{
				MenuItem remove = new MenuItem("Delete");
				remove.setOnAction(e -> {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Delete Cutscene");
					alert.setContentText("Are you sure you want to delete " + typeName + " '" + item.toString() + "'?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) parent.onDelete(item);
				});
				menu.getItems().add(remove);
			}
		}

		return menu;
	}

	private void move(T item, int indexMove)
	{
		if (indexMove == 0) return;
		ObservableList<T> items = this.getListView().getItems();
		int oldIndex = items.indexOf(item);
		int newIndex = oldIndex + indexMove;
		items.add(newIndex, item);
		items.remove(oldIndex + (indexMove < 0 ? 1 : 0));
		this.getListView().getSelectionModel().select(item);
	}

	public CustomListCell<T> setCanCreate(boolean canCreate)
	{
		this.canCreate = canCreate;
		return this;
	}

	public CustomListCell<T> setCanDelete(boolean canDelete)
	{
		this.canDelete = canDelete;
		return this;
	}

	public CustomListCell<T> setCanEdit(boolean canEdit)
	{
		this.canEdit = canEdit;
		return this;
	}

	public CustomListCell<T> setCanOrder(boolean canOrder)
	{
		this.canOrder = canOrder;
		return this;
	}

	public CustomListCell<T> setCanRename(boolean canRename)
	{
		this.canRename = canRename;
		return this;
	}

	@Override
	protected void updateItem(T item, boolean empty)
	{
		super.updateItem(item, empty);
		this.setText(empty ? "" : item.toString());
		this.setGraphic(this.parent.graphicFor(item));
		this.setContextMenu(this.menu(item, empty));
	}

}
