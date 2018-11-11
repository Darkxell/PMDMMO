package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class AnimateEventController extends EventController
{

	@FXML
	private TextField animationTextfield;
	@FXML
	private ComboBox<CutsceneEntity> targetCombobox;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new AnimateCutsceneEvent(this.id(), Integer.parseInt(this.animationTextfield.getText()), this.targetCombobox.getValue());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.targetCombobox.getItems().addAll(EditCutsceneController.instance.listAvailableEntities(EditCutsceneController.instance.listManager.editing));
		this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
		if (!this.targetCombobox.getItems().isEmpty()) this.targetCombobox.getSelectionModel().select(0);

		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.animationTextfield.setTextFormatter(formatter);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		for (CutsceneEntity e : this.targetCombobox.getItems())
			if (e.id == ((AnimateCutsceneEvent) event).target)
			{
				this.targetCombobox.getSelectionModel().select(e);
				break;
			}
		this.animationTextfield.setText(String.valueOf(((AnimateCutsceneEvent) event).animationID));
	}

}
