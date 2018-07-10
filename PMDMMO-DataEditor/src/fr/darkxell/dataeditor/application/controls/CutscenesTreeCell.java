package fr.darkxell.dataeditor.application.controls;

import java.util.Optional;

import com.darkxell.client.mechanics.cutscene.Cutscene;

import fr.darkxell.dataeditor.application.controller.cutscene.CutscenesTabController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

public class CutscenesTreeCell extends ListCell<Cutscene>
{

	private ContextMenu menu(Cutscene item, boolean empty)
	{
		ContextMenu menu = new ContextMenu();

		MenuItem add = new MenuItem("New Cutscene...");
		add.setOnAction(e -> {
			CutscenesTabController.instance.onCreateCutscene();
		});
		menu.getItems().add(add);

		if (!empty)
		{
			MenuItem edit = new MenuItem("Edit");
			edit.setOnAction(e -> {
				CutscenesTabController.instance.onEdit(item);
			});

			MenuItem remove = new MenuItem("Delete");
			remove.setOnAction(e -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Delete Cutscene");
				alert.setContentText("Are you sure you want to delete Cutscene '" + item.name + "'?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) CutscenesTabController.instance.onDelete(item);
			});

			menu.getItems().add(edit);
			menu.getItems().add(remove);
		}

		return menu;
	}

	@Override
	protected void updateItem(Cutscene item, boolean empty)
	{
		super.updateItem(item, empty);
		this.setText(empty ? "" : item.name);

		this.setContextMenu(this.menu(item, empty));
	}

}
