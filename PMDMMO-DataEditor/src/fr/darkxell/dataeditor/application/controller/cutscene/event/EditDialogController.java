package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;
import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditDialogController implements Initializable {

    private ArrayList<CutsceneEntity> allEntities = new ArrayList<>();
    @FXML
    public Button cancelButton;
    @FXML
    private ComboBox<PortraitEmotion> emotionCombobox;
    @FXML
    public Button okButton;
    @FXML
    private ComboBox<DialogPortraitLocation> portraitCombobox;
    @FXML
    private Label portraitLabel;
    @FXML
    private CheckBox targetCheckbox;
    @FXML
    private ComboBox<CutsceneEntity> targetCombobox;
    @FXML
    private TextField textTextfield;
    @FXML
    private CheckBox translatedCheckbox;

    public CutsceneDialogScreen getScreen() {
        CutsceneEntity target = this.targetCheckbox.isSelected()
                ? this.targetCombobox.getSelectionModel().getSelectedItem()
                : null;
        if (target != null && target.id == -1)
            new Alert(AlertType.WARNING, "Your target needs an ID to work properly.", ButtonType.OK);
        return new CutsceneDialogScreen(this.textTextfield.getText(), this.translatedCheckbox.isSelected(),
                this.emotionCombobox.getValue(), target, this.portraitCombobox.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.allEntities.addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            this.targetCombobox.setDisable(!newValue);
            this.portraitLabel.setDisable(!newValue);
            this.portraitCombobox.setDisable(!newValue);
        });
        this.targetCombobox.getItems().addAll(this.allEntities);
        this.targetCombobox.getSelectionModel().select(0);
        this.targetCombobox.setDisable(true);

        this.portraitCombobox.getItems().addAll(DialogPortraitLocation.values());
        this.portraitCombobox.getSelectionModel().select(DialogPortraitLocation.BOTTOM_LEFT);

        this.emotionCombobox.getItems().addAll(PortraitEmotion.values());
        this.emotionCombobox.setValue(PortraitEmotion.Normal);
    }

    public void onCancel() {
        DialogEventController.editDialogPopup.close();
    }

    public void onOk() {
        this.onCancel();
        DialogEventController.instance.onEditConfirm(this.getScreen());
    }

    public void setup(CutsceneDialogScreen screen) {
        this.textTextfield.setText(screen.message.id);
        this.translatedCheckbox.setSelected(screen.message.shouldTranslate);
        this.targetCheckbox.setSelected(screen.pokemon != -1);
        if (this.targetCheckbox.isSelected())
            for (CutsceneEntity e : this.targetCombobox.getItems())
                if (e.id == screen.pokemon) {
                    this.targetCombobox.getSelectionModel().select(e);
                    break;
                }
        if (screen.portraitLocation != null)
            this.portraitCombobox.getSelectionModel().select(screen.portraitLocation);
        this.emotionCombobox.setValue(screen.emotion);
    }

}
