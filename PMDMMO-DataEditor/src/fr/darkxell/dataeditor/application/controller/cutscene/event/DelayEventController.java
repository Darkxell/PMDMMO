package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		Pattern pattern = Pattern.compile("\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.delayTextfield.setTextFormatter(formatter);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.delayTextfield.setText(String.valueOf(((DelayCutsceneEvent) event).duration));
	}

}
