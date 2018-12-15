package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;
import com.darkxell.client.mechanics.cutscene.event.OptionResultCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class OptionResultEventController extends EventController implements EventEditionListener {

	@FXML
	private ListView<CutsceneEvent> eventList;
	private EventList listManager;
	@FXML
	private TextField optionTextfield;
	@FXML
	private ComboBox<CutsceneEvent> targetCombobox;

	@Override
	public List<CutsceneEvent> availableEvents() {
		ArrayList<CutsceneEvent> events = new ArrayList<>(this.listener.availableEvents());
		int i = events.indexOf(this.listener.listManager().editing);
		if (i == -1) i = 0;
		for (CutsceneEvent e : this.eventList.getItems())
			events.add(i++, e);
		return events;
	}

	@Override
	public CutsceneEvent generateEvent() {
		if (this.optionTextfield.getText().equals("")) {
			FXUtils.showAlert("Type in option lazyguy");
			return null;
		}
		if (this.targetCombobox.getValue().id == -1) FXUtils
				.showAlert("Warning: your target option selection event doesn't have and ID. Don't forget to add one!");
		return new OptionResultCutsceneEvent(this.id(), Integer.parseInt(this.optionTextfield.getText()),
				this.targetCombobox.getValue(),
				this.eventList.getItems().toArray(new CutsceneEvent[this.eventList.getItems().size()]));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		(this.listManager = new EventList()).setup(this, this.eventList);

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
	public EventList listManager() {
		return this.listManager;
	}

	@Override
	public void onEditCancel() {
		this.listManager.editEventPopup.close();
	}

	@Override
	public void onEditConfirm(CutsceneEvent event) {
		if (event == null) return;
		ObservableList<CutsceneEvent> events = this.eventList.getItems();
		if (this.listManager.editing == null) events.add(event);
		else {
			int index = events.indexOf(this.listManager.editing);
			events.remove(index);
			events.add(index, event);
		}
	}

	@Override
	public void onEventTypeCancel() {
		this.listManager.selectEventTypePopup.close();
	}

	@Override
	public void onEventTypeSelect(CutsceneEventType type) {
		this.listManager.selectEventTypePopup.close();
		this.listManager.onCreate(null, type);
	}

	@Override
	public void setup(CutsceneEvent event) {
		super.setup(event);

		this.targetCombobox.setValue(((OptionResultCutsceneEvent) event).target);
		this.optionTextfield.setText(String.valueOf(((OptionResultCutsceneEvent) event).option));
		this.eventList.getItems().clear();
		this.eventList.getItems().addAll(((OptionResultCutsceneEvent) event).results);
	}

}
