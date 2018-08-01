package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class WaitEventController extends EventController
{

	public static class EventListCell extends ListCell<CutsceneEvent>
	{
		protected void updateItem(CutsceneEvent item, boolean empty)
		{
			super.updateItem(item, empty);
			this.setText(empty ? null : item.toString());
			this.setGraphic(empty ? null : EditCutsceneController.instance.graphicFor(item));
		};
	};

	@FXML
	private Button addButton;
	@FXML
	private ListView<CutsceneEvent> addedEventsList;
	@FXML
	private CheckBox allCheckbox;
	private ArrayList<CutsceneEvent> allEvents = new ArrayList<>();
	@FXML
	private ListView<CutsceneEvent> existingEventsList;
	@FXML
	private Button removeButton;
	private Comparator<CutsceneEvent> sorter = new Comparator<CutsceneEvent>() {
		@Override
		public int compare(CutsceneEvent o1, CutsceneEvent o2)
		{
			return Integer.compare(allEvents.indexOf(o1), allEvents.indexOf(o2));
		}
	};

	private void add(CutsceneEvent event)
	{
		this.existingEventsList.getItems().remove(event);
		this.addedEventsList.getItems().add(event);
		this.sortLists();
	}

	public void addSelected()
	{
		CutsceneEvent e = this.existingEventsList.getSelectionModel().getSelectedItem();
		if (e != null) this.add(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CutsceneEvent generateEvent()
	{
		ArrayList<CutsceneEvent> events = this.allCheckbox.isSelected() ? (ArrayList<CutsceneEvent>) this.allEvents.clone()
				: new ArrayList<>(this.addedEventsList.getItems());

		if (!this.allCheckbox.isSelected())
		{
			ArrayList<CutsceneEvent> noid = new ArrayList<>(events);
			noid.removeIf(e -> e.id != -1);
			if (!noid.isEmpty())
			{
				String message = "The following events need an ID to be waited for properly:";
				for (CutsceneEvent e : noid)
					message += "\n" + e;
				new Alert(AlertType.WARNING, message, ButtonType.OK).showAndWait();
			}
		}
		return new WaitCutsceneEvent(this.id(), this.allCheckbox.isSelected(), events);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);
		this.allCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			this.addedEventsList.setDisable(newValue);
			this.existingEventsList.setDisable(newValue);
			this.addButton.setDisable(newValue);
			this.removeButton.setDisable(newValue);
		});

		ObservableList<CutsceneEvent> events = EditCutsceneController.instance.eventList.getItems();
		this.allEvents.addAll(events);
		if (EditCutsceneController.editing != null) this.allEvents.removeIf(e -> events.indexOf(e) >= events.indexOf(EditCutsceneController.editing));
		this.existingEventsList.getItems().addAll(this.allEvents);
		this.existingEventsList.setCellFactory(param -> {
			return new EventListCell();
		});
		this.addedEventsList.setCellFactory(param -> {
			return new EventListCell();
		});

		this.allCheckbox.setSelected(true);
	}

	public void removeSelected()
	{
		CutsceneEvent e = this.addedEventsList.getSelectionModel().getSelectedItem();
		if (e != null)
		{
			this.addedEventsList.getItems().remove(e);
			this.existingEventsList.getItems().add(e);
			this.sortLists();
		}
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.allCheckbox.setSelected(((WaitCutsceneEvent) event).all);
		if (!this.allCheckbox.isSelected()) for (CutsceneEvent e : ((WaitCutsceneEvent) event).events)
			this.add(e);
	}

	private void sortLists()
	{
		this.addedEventsList.getItems().sort(this.sorter);
		this.existingEventsList.getItems().sort(this.sorter);
	}

}
