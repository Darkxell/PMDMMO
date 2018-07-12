package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.SetAnimatedCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class SetAnimatedEventController extends EventController
{

	@FXML
	private CheckBox animatedCheckbox;
	@FXML
	private ComboBox<CutsceneEntity> targetCombobox;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new SetAnimatedCutsceneEvent(this.id(), this.targetCombobox.getSelectionModel().getSelectedItem(), this.animatedCheckbox.isSelected());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.targetCombobox.getItems().addAll(EditCutsceneController.instance.listAvailableEntities(EditCutsceneController.editing));
		this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
		if (!this.targetCombobox.getItems().isEmpty()) this.targetCombobox.getSelectionModel().select(0);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		for (CutsceneEntity e : this.targetCombobox.getItems())
			if (e.id == ((SetAnimatedCutsceneEvent) event).target)
			{
				this.targetCombobox.getSelectionModel().select(e);
				break;
			}
		this.animatedCheckbox.setSelected(((SetAnimatedCutsceneEvent) event).animated);
	}

}
