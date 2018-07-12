package fr.darkxell.dataeditor.application.controller.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DelayEventController extends EventController
{

	@FXML
	private TextField delayTextfield;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new DelayCutsceneEvent(this.id(), Integer.parseInt(this.delayTextfield.getText()));
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.delayTextfield.setText(String.valueOf(((DelayCutsceneEvent) event).duration));
	}

}
