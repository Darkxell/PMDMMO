package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class RotateEventController extends EventController
{

	@FXML
	private TextField distanceTextfield;
	@FXML
	private TextField speedTextfield;
	@FXML
	private ComboBox<CutsceneEntity> targetCombobox;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new RotateCutsceneEvent(this.id(), this.targetCombobox.getSelectionModel().getSelectedItem(),
				Double.valueOf(this.distanceTextfield.getText()).intValue(), Double.valueOf(this.distanceTextfield.getText()).intValue());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.targetCombobox.getItems().addAll(EditCutsceneController.instance.listAvailableEntities(EditCutsceneController.editing));
		this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
		if (!this.targetCombobox.getItems().isEmpty()) this.targetCombobox.getSelectionModel().select(0);

		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.distanceTextfield.setTextFormatter(formatter);

		Pattern pattern2 = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter2 = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.speedTextfield.setTextFormatter(formatter2);
		this.speedTextfield.setText(String.valueOf(RotateCutsceneEvent.DEFAULT_SPEED));
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		for (CutsceneEntity e : this.targetCombobox.getItems())
			if (e.id == ((RotateCutsceneEvent) event).target)
			{
				this.targetCombobox.getSelectionModel().select(e);
				break;
			}
		this.speedTextfield.setText(String.valueOf(((RotateCutsceneEvent) event).speed));
		this.distanceTextfield.setText(String.valueOf(((RotateCutsceneEvent) event).distance));
	}

}
