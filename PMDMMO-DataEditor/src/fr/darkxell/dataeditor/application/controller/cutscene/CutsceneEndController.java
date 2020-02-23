package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.cutscene.end.CutsceneEndModel;
import com.darkxell.client.model.cutscene.end.EnterDungeonCutsceneEndModel;
import com.darkxell.client.model.cutscene.end.LoadFreezoneCutsceneEndModel;
import com.darkxell.client.model.cutscene.end.PlayCutsceneCutsceneEndModel;
import com.darkxell.client.model.cutscene.end.ResumeExplorationCutsceneEndModel;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;
import com.darkxell.common.zones.FreezoneInfo;

import fr.darkxell.dataeditor.application.controller.cutscene.CutsceneEndController.CutsceneEndMode;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CutsceneEndController implements Initializable, ChangeListener<CutsceneEndMode> {

    public enum CutsceneEndMode {
        CUTSCENE("Play Cutscene"),
        DUNGEON("Enter Dungeon"),
        EXPLORE("Resume Dungeon Exploration"),
        FREEZONE("Load Freezone");

        public final String name;

        CutsceneEndMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @FXML
    private ComboBox<CutsceneModel> cutsceneCombobox;
    @FXML
    private ComboBox<Direction> directionCombobox;
    @FXML
    private ComboBox<Dungeon> dungeonCombobox;
    @FXML
    private CheckBox facingCheckbox;
    @FXML
    private CheckBox fadingCheckbox;
    @FXML
    private ComboBox<FreezoneInfo> freezoneCombobox;
    @FXML
    private TextField freezoneXTextfield;
    @FXML
    private TextField freezoneYTextfield;
    @FXML
    private TextField functionTextfield;
    @FXML
    private ComboBox<CutsceneEndMode> modeCombobox;

    @Override
    public void changed(ObservableValue<? extends CutsceneEndMode> observable, CutsceneEndMode oldValue,
            CutsceneEndMode newValue) {
        this.cutsceneCombobox.setVisible(newValue == CutsceneEndMode.CUTSCENE);
        this.dungeonCombobox.setVisible(newValue == CutsceneEndMode.DUNGEON);
        this.freezoneCombobox.setVisible(newValue == CutsceneEndMode.FREEZONE);
        this.freezoneXTextfield.setVisible(newValue == CutsceneEndMode.FREEZONE);
        this.freezoneYTextfield.setVisible(newValue == CutsceneEndMode.FREEZONE);
    }

    public CutsceneEndModel getEnd() {
        String function = this.functionTextfield.getText();
        if (function.equals(""))
            function = null;
        switch (this.modeCombobox.getSelectionModel().getSelectedItem()) {
        case CUTSCENE:
            return new PlayCutsceneCutsceneEndModel(this.cutsceneCombobox.getSelectionModel().getSelectedItem().getName(),
                    function, this.fadingCheckbox.isSelected());

        case DUNGEON:
            return new EnterDungeonCutsceneEndModel(this.dungeonCombobox.getSelectionModel().getSelectedItem().getID(), function,
                    this.fadingCheckbox.isSelected());

        case FREEZONE:
            return new LoadFreezoneCutsceneEndModel(this.freezoneCombobox.getValue(),
                    Integer.parseInt(this.freezoneXTextfield.getText()),
                    Integer.parseInt(this.freezoneYTextfield.getText()),
                    this.facingCheckbox.isSelected() ? this.directionCombobox.getValue() : null, function,
                    this.fadingCheckbox.isSelected());

        case EXPLORE:
            return new ResumeExplorationCutsceneEndModel(function, this.fadingCheckbox.isSelected());
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pattern pattern = Pattern.compile("-?\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.freezoneXTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.freezoneYTextfield.setTextFormatter(formatter);
        Pattern pattern2 = Pattern.compile("(\\w\\/?)*");
        TextFormatter<String> formatter2 = new TextFormatter<>(change -> {
            return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.functionTextfield.setTextFormatter(formatter2);

        this.facingCheckbox.selectedProperty()
                .addListener((x, oldValue, newValue) -> this.directionCombobox.setDisable(!newValue));

        this.dungeonCombobox.getItems().addAll(Registries.dungeons().toList());
        this.modeCombobox.getItems().addAll(CutsceneEndMode.values());
        this.freezoneCombobox.getItems().addAll(FreezoneInfo.values());
        this.directionCombobox.getItems().addAll(Direction.DIRECTIONS);

        this.modeCombobox.getSelectionModel().selectedItemProperty().addListener(this);
        this.modeCombobox.getSelectionModel().select(0);
        this.dungeonCombobox.getSelectionModel().select(0);
        this.freezoneCombobox.getSelectionModel().select(0);
        this.directionCombobox.setValue(Direction.SOUTH);
        this.fadingCheckbox.setSelected(true);
    }

    public void setupFor(CutsceneModel cutscene) {
        this.cutsceneCombobox.getItems().clear();
        this.cutsceneCombobox.getItems().addAll(Cutscenes.values());
        this.cutsceneCombobox.getItems().sort(Comparator.naturalOrder());
        this.cutsceneCombobox.getSelectionModel().select(0);

        CutsceneEndModel end = cutscene.getEnd();
        if (end instanceof PlayCutsceneCutsceneEndModel) {
            this.cutsceneCombobox.getSelectionModel().select(Cutscenes.get(((PlayCutsceneCutsceneEndModel) end).getCutsceneID()));
            this.modeCombobox.getSelectionModel().select(CutsceneEndMode.CUTSCENE);
        }
        if (end instanceof EnterDungeonCutsceneEndModel) {
            this.dungeonCombobox.getSelectionModel()
                    .select(Registries.dungeons().find(((EnterDungeonCutsceneEndModel) end).getDungeonID()));
            this.modeCombobox.getSelectionModel().select(CutsceneEndMode.DUNGEON);
        }
        if (end instanceof LoadFreezoneCutsceneEndModel) {
            LoadFreezoneCutsceneEndModel e = (LoadFreezoneCutsceneEndModel) end;
            this.freezoneCombobox.setValue(e.getFreezone());
            this.freezoneXTextfield.setText(String.valueOf(e.getXPos()));
            this.freezoneYTextfield.setText(String.valueOf(e.getYPos()));
            this.modeCombobox.getSelectionModel().select(CutsceneEndMode.FREEZONE);
            this.facingCheckbox.setSelected(e.getDirection() != null);
            if (e.getDirection() != null)
                this.directionCombobox.setValue(e.getDirection());
        }
        if (end instanceof ResumeExplorationCutsceneEndModel)
            this.modeCombobox.getSelectionModel().select(CutsceneEndMode.EXPLORE);
        if (end.getFunctionID() != null)
            this.functionTextfield.setText(end.getFunctionID());
        else
            this.functionTextfield.setText("");

        this.fadingCheckbox.setSelected(end.isFadesOut());
    }

}
