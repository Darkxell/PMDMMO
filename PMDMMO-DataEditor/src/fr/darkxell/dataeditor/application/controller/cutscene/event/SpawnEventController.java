package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SpawnCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditEntityController;
import javafx.fxml.FXML;

public class SpawnEventController extends EventController
{

	@FXML
	private EditEntityController editEntityController;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new SpawnCutsceneEvent(this.id(), this.editEntityController.getEntity());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.editEntityController.okButton.setVisible(false);
		this.editEntityController.cancelButton.setVisible(false);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.editEntityController.setupFor(((SpawnCutsceneEvent) event).entity);
	}

}
