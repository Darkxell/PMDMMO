package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;
import com.darkxell.client.mechanics.cutscene.event.OptionResultCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class OptionResultEventController extends EventController
{

	@FXML
	private ListView<CutsceneEvent> eventList;
	@FXML
	private TextField optionTextfield;
	@FXML
	private ComboBox<CutsceneEvent> targetCombobox;

	@Override
	public CutsceneEvent generateEvent()
	{
		if (this.optionTextfield.getText().equals(""))
		{
			FXUtils.showAlert("Type in option lazyguy");
			return null;
		}
		return new OptionResultCutsceneEvent(this.id(), Integer.parseInt(this.optionTextfield.getText()), this.targetCombobox.getValue(),
				this.eventList.getItems().toArray(new CutsceneEvent[this.eventList.getItems().size()]));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);

		Pattern pattern = Pattern.compile("\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.optionTextfield.setTextFormatter(formatter);

		ArrayList<CutsceneEvent> events = new ArrayList<>(EditCutsceneController.instance.eventList.getItems());
		events.removeIf(e -> e.type != CutsceneEventType.option);
		this.targetCombobox.getItems().addAll(events);
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);

		this.targetCombobox.setValue(((OptionResultCutsceneEvent) event).target);
		this.optionTextfield.setText(String.valueOf(((OptionResultCutsceneEvent) event).option));
		this.eventList.getItems().clear();
		this.eventList.getItems().addAll(((OptionResultCutsceneEvent) event).results);
	}

}
