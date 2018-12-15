package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.event.DespawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SpawnCutsceneEvent;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.ui.Frame;

import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventList;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class EditCutsceneController implements Initializable, EventEditionListener {

	public static EditCutsceneController instance;

	@FXML
	public CutsceneCreationController cutsceneCreationController;
	@FXML
	private CutsceneEndController cutsceneEndController;
	@FXML
	public ListView<CutsceneEvent> eventList;
	public EventList listManager;

	@Override
	public List<CutsceneEvent> availableEvents() {
		return this.eventList.getItems();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		(this.listManager = new EventList()).setup(this, this.eventList);
	}

	public ArrayList<CutsceneEntity> listAvailableEntities(CutsceneEvent event) {
		ArrayList<CutsceneEntity> entities = new ArrayList<>();
		for (CutsceneEntity entity : this.cutsceneCreationController.entitiesList.getItems())
			entities.add(entity);
		for (CutsceneEvent e : this.eventList.getItems()) {
			if (e == event) break;
			if (e instanceof SpawnCutsceneEvent) entities.add(((SpawnCutsceneEvent) e).entity);
			if (e instanceof DespawnCutsceneEvent)
				entities.removeIf(ent -> ent.id == ((DespawnCutsceneEvent) e).target);
		}
		return entities;
	}

	@Override
	public EventList listManager() {
		return this.listManager;
	}

	public void onCreateEvent() {
		this.listManager.onCreate(null);
	}

	@Override
	public void onEditCancel() {
		this.listManager.editEventPopup.close();
	}

	@Override
	public void onEditConfirm(CutsceneEvent event) {
		if (event == null) return;
		ObservableList<CutsceneEvent> events = this.eventList.getItems();
		if (this.listManager.editing == null) {
			int index = this.eventList.getSelectionModel().getSelectedIndex();
			if (index == events.size() - 1 || index == -1) events.add(event);
			else events.add(index + 1, event);
		} else {
			int index = events.indexOf(this.listManager.editing);
			events.remove(index);
			events.add(index, event);
		}

		this.eventList.getSelectionModel().select(event);
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

	public void saveChanges() {
		Cutscene c = new Cutscene(CutscenesTabController.instance.currentCutscene.name,
				this.cutsceneCreationController.getCreation(), this.cutsceneEndController.getEnd(),
				new ArrayList<>(this.eventList.getItems()));
		CutscenesTabController.instance.currentCutscene = c;
		Cutscenes.update(c);
		CutscenesTabController.instance.reloadCutsceneList();
	}

	public void setupFor(Cutscene cutscene) {
		this.cutsceneCreationController.setupFor(cutscene);
		this.cutsceneEndController.setupFor(cutscene);
		this.eventList.getItems().clear();
		this.eventList.getItems().addAll(cutscene.events);
	}

	public void test() {
		Cutscene temp = new Cutscene(CutscenesTabController.instance.currentCutscene.name,
				this.cutsceneCreationController.getCreation(), this.cutsceneEndController.getEnd(),
				new ArrayList<>(this.eventList.getItems()));
		Cutscene test = new Cutscene("test", temp.toXML());
		test.onFinish = new CloseTesterCutsceneEnd(test);

		Persistance.frame = new Frame();
		Persistance.frame.canvas.requestFocus();
		Persistance.stateManager = new PrincipalMainState();
		Persistance.cutsceneState = new CutsceneState(test);
		test.creation.create();
		Persistance.stateManager.setState(Persistance.cutsceneState);

		Launcher.isRunning = true;
		Launcher.setProcessingProfile(Launcher.PROFILE_SYNCHRONIZED);
	}

}
