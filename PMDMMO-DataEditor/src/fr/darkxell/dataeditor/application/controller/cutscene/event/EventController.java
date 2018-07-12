package fr.darkxell.dataeditor.application.controller.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.Initializable;

public abstract class EventController implements Initializable
{

	public void onCancel()
	{
		EditCutsceneController.editEventPopup.close();
	}

	public void onOk()
	{
		this.onCancel();
		CutsceneEvent e = this.generateEvent();
		EditCutsceneController.instance.onEditConfirm(e);
	}

	public abstract CutsceneEvent generateEvent();

	public abstract void setup(CutsceneEvent event);

}
