package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.Cutscene;

import fr.darkxell.dataeditor.application.controls.CutscenesTreeCell;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;

public class CutscenesTabController implements Initializable
{

	public static CutscenesTabController instance;

	/** Currently edited Cutscene. */
	public Cutscene currentCutscene;
	@FXML
	private ListView<Cutscene> cutscenesList;
	@FXML
	private EditCutsceneController editCutsceneController;
	@FXML
	private TitledPane editCutscenePane;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		this.cutscenesList.setCellFactory(param -> {
			return new CutscenesTreeCell();
		});
		this.cutscenesList.getItems().addAll(Cutscenes.values());
		this.cutscenesList.getItems().sort(Comparator.naturalOrder());

		this.editCutscenePane.setVisible(false);
	}

	public void onCreateCutscene()
	{
		this.onCreateCutscene(null);
	}

	private void onCreateCutscene(Cutscene cutscene)
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle(cutscene == null ? "New Cutscene" : "Rename Cutscene");
		dialog.setHeaderText(null);
		dialog.setContentText("Type in " + (cutscene == null ? "the name of the new Cutscene." : "the new name for Cutscene '" + cutscene.name + "'?")
				+ "\nIt will be the same as its path in the cutscenes folder.\nUse slashes to put in subfolders, and don't include extension.");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent())
		{
			String n = name.get();
			if (Cutscenes.containsKey(n)) new Alert(AlertType.ERROR, "There is already a Cutscene named '" + n + "'.", ButtonType.OK).showAndWait();
			else
			{
				if (cutscene != null)
				{
					Cutscenes.remove(cutscene.name);
					cutscene.name = n;
				}
				Cutscene c = cutscene == null ? new Cutscene(n) : cutscene;
				Cutscenes.add(c);
				if (cutscene == null) this.cutscenesList.getItems().add(c);
				this.cutscenesList.getItems().sort(Comparator.naturalOrder());
			}
		}
	}

	public void onDelete(Cutscene item)
	{
		if (item == this.currentCutscene)
		{
			this.currentCutscene = null;
			this.editCutscenePane.setVisible(false);
		}
		this.cutscenesList.getItems().remove(item);
		Cutscenes.remove(item.name);
	}

	public void onEdit(Cutscene cutscene)
	{
		this.currentCutscene = cutscene;
		this.editCutscenePane.setVisible(true);
		this.editCutscenePane.setText(this.currentCutscene.name);
		this.editCutsceneController.setupFor(this.currentCutscene);
	}

	public void onRename(Cutscene cutscene)
	{
		this.onCreateCutscene(cutscene);
	}

}
