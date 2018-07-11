package fr.darkxell.dataeditor.application.controls;

import java.util.Optional;

import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;

import fr.darkxell.dataeditor.application.controller.cutscene.CutsceneCreationController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

public class CutsceneEntityTreeCell extends ListCell<CutsceneEntity>
{

	private ContextMenu menu(CutsceneEntity item, boolean empty)
	{
		ContextMenu menu = new ContextMenu();

		MenuItem add = new MenuItem("New Cutscene Entity...");
		add.setOnAction(e -> {
			CutsceneCreationController.instance.onCreateEntity();
		});
		menu.getItems().add(add);

		if (!empty)
		{
			MenuItem edit = new MenuItem("Edit");
			edit.setOnAction(e -> {
				CutsceneCreationController.instance.onEdit(item);
			});

			MenuItem remove = new MenuItem("Delete");
			remove.setOnAction(e -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Delete Entity");
				alert.setContentText("Are you sure you want to delete Entity '" + item + "'?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) CutsceneCreationController.instance.onDelete(item);
			});

			menu.getItems().add(edit);
			menu.getItems().add(remove);
		}

		return menu;
	}

	@Override
	protected void updateItem(CutsceneEntity item, boolean empty)
	{
		super.updateItem(item, empty);
		this.setText(empty ? "" : item.toString());

		this.setContextMenu(this.menu(item, empty));
	}

}
