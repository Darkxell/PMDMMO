package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.render.RenderProfile;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.DespawnCutsceneEventModel;
import com.darkxell.client.model.cutscene.event.SpawnCutsceneEventModel;
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
    public ListView<CutsceneEventModel> eventList;
    public EventList listManager;

    @Override
    public List<CutsceneEventModel> availableEvents() {
        return this.eventList.getItems();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        (this.listManager = new EventList()).setup(this, this.eventList);
    }

    public ArrayList<CutsceneEntityModel> listAvailableEntities(CutsceneEventModel event) {
        ArrayList<CutsceneEntityModel> entities = new ArrayList<>(
                this.cutsceneCreationController.entitiesList.getItems());
        for (CutsceneEventModel e : this.eventList.getItems()) {
            if (e == event)
                break;
            if (e instanceof SpawnCutsceneEventModel)
                entities.add(((SpawnCutsceneEventModel) e).getEntity());
            if (e instanceof DespawnCutsceneEventModel)
                entities.removeIf(ent -> ent.getCutsceneID() == ((DespawnCutsceneEventModel) e).getTarget());
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
    public void onEditConfirm(CutsceneEventModel event) {
        if (event == null)
            return;
        ObservableList<CutsceneEventModel> events = this.eventList.getItems();
        if (this.listManager.editing == null) {
            int index = this.eventList.getSelectionModel().getSelectedIndex();
            if (index == events.size() - 1 || index == -1)
                events.add(event);
            else
                events.add(index + 1, event);
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
        CutsceneModel c = new CutsceneModel(CutscenesTabController.instance.currentCutscene.getName(),
                this.cutsceneCreationController.getCreation(), new ArrayList<>(this.eventList.getItems()),
                this.cutsceneEndController.getEnd());
        CutscenesTabController.instance.currentCutscene = c;
        Cutscenes.update(c);
        CutscenesTabController.instance.reloadCutsceneList();
    }

    public void setupFor(CutsceneModel cutscene) {
        this.cutsceneCreationController.setupFor(cutscene);
        this.cutsceneEndController.setupFor(cutscene);
        this.eventList.getItems().clear();
        this.eventList.getItems().addAll(cutscene.getEvents());
    }

    public void test() {
        Cutscene test = new Cutscene(new CutsceneModel(CutscenesTabController.instance.currentCutscene.getName(),
                this.cutsceneCreationController.getCreation(), new ArrayList<>(this.eventList.getItems()),
                this.cutsceneEndController.getEnd()));

        Persistence.frame = new Frame();
        Persistence.frame.canvas.requestFocus();
        Persistence.stateManager = new PrincipalMainState();
        Persistence.cutsceneState = new CutsceneState(test);
        test.creation.create();
        Persistence.stateManager.setState(Persistence.cutsceneState);

        Launcher.isRunning = true;
        Launcher.setProcessingProfile(RenderProfile.PROFILE_SYNCHRONIZED);
    }

}
