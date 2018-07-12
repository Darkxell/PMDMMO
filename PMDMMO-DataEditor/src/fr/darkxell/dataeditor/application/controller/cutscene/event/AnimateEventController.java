package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AnimateEventController extends EventController
{

	@FXML
	private TextField animationTextfield;
	@FXML
	private CheckBox targetCheckbox;
	@FXML
	private ComboBox<CutsceneEntity> targetCombobox;

	@Override
	public CutsceneEvent generateEvent()
	{
		CutsceneEntity entity = this.targetCheckbox.isSelected() ? this.targetCombobox.getValue() : null;
		return new AnimateCutsceneEvent(this.id(), this.animationTextfield.getText(), entity);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.targetCombobox.getItems().addAll(EditCutsceneController.instance.listAvailableEntities(EditCutsceneController.editing));
		this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
		if (!this.targetCombobox.getItems().isEmpty()) this.targetCombobox.getSelectionModel().select(0);

		this.targetCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			this.targetCombobox.setDisable(!newValue);
		});
		this.targetCombobox.setDisable(true);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.targetCheckbox.setSelected(((AnimateCutsceneEvent) event).target != -1);
		if (((AnimateCutsceneEvent) event).target != -1) for (CutsceneEntity e : this.targetCombobox.getItems())
			if (e.id == ((AnimateCutsceneEvent) event).target)
			{
				this.targetCombobox.getSelectionModel().select(e);
				break;
			}
		this.animationTextfield.setText(((AnimateCutsceneEvent) event).animation);
	}

}
