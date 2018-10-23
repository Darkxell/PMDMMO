package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.Cutscene;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;

public class CutscenesTabController implements Initializable, ListCellParent<Cutscene>
{

	public static CutscenesTabController instance;

	/** Currently edited Cutscene. */
	public Cutscene currentCutscene;

	@FXML
	private ListView<Cutscene> cutscenesList;
	@FXML
	public EditCutsceneController editCutsceneController;
	@FXML
	private TitledPane editCutscenePane;

	@Override
	public Node graphicFor(Cutscene item)
	{
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		CustomList.setup(this, this.cutscenesList, "Cutscene", true, true, true, true, false);
		this.reloadCutsceneList();

		this.editCutscenePane.setVisible(false);
	}

	@Override
	public void onCreate(Cutscene cutscene)
	{
		this.onCreateCutscene(null);
	}

	public void onCreateCutscene()
	{
		this.onCreate(null);
	}

	private void onCreateCutscene(Cutscene cutscene)
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("New Cutscene");
		dialog.setHeaderText(null);
		dialog.setContentText(
				"Type in the name of the new Cutscene.\nIt will be the same as its path in the cutscenes folder.\nUse slashes to put in subfolders, and don't include extension.");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent()) this.onRename(null, name.get());
	}

	@Override
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

	@Override
	public void onEdit(Cutscene cutscene)
	{
		this.currentCutscene = cutscene;
		this.editCutscenePane.setVisible(true);
		this.editCutscenePane.setText(this.currentCutscene.name);
		this.editCutsceneController.setupFor(this.currentCutscene);
	}

	@Override
	public void onMove(Cutscene item, int newIndex)
	{}

	@Override
	public void onRename(Cutscene cutscene, String name)
	{
		if (Cutscenes.containsKey(name)) new Alert(AlertType.ERROR, "There is already a Cutscene named '" + name + "'.", ButtonType.OK).showAndWait();
		else
		{
			if (cutscene != null)
			{
				Cutscenes.remove(cutscene.name);
				cutscene.name = name;
			}
			Cutscene c = cutscene == null ? new Cutscene(name) : cutscene;
			Cutscenes.add(c);
			if (cutscene == null) this.cutscenesList.getItems().add(c);
			this.cutscenesList.getItems().sort(Comparator.naturalOrder());
		}
	}

	public void reloadCutsceneList()
	{
		this.cutscenesList.getItems().clear();
		this.cutscenesList.getItems().addAll(Cutscenes.values());
		this.cutscenesList.getItems().sort(Comparator.naturalOrder());
	}

}
