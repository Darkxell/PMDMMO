package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.Cutscene;

import fr.darkxell.dataeditor.application.DEPersistance;
import fr.darkxell.dataeditor.application.controls.CutscenesTreeCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class CutscenesTabController implements Initializable
{

	public static CutscenesTabController instance;

	/** Currently edited Cutscene. */
	public Cutscene currentCutscene;
	@FXML
	private ListView<Cutscene> cutscenesList;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		this.cutscenesList.setCellFactory(param -> {
			return new CutscenesTreeCell();
		});
		this.cutscenesList.getItems().addAll(DEPersistance.cutscenes.values());
		this.cutscenesList.getItems().sort(Comparator.naturalOrder());
	}

	public void onCreateCutscene()
	{
		// TODO Auto-generated method stub

	}

	public void onDelete(Cutscene item)
	{
		this.cutscenesList.getItems().remove(item);
		DEPersistance.cutscenes.remove(item.name);
	}

	public void onEdit(Cutscene cutscene)
	{
		this.currentCutscene = cutscene;
	}

}
