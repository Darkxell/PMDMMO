package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
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

    private ArrayList<CutsceneEntityModel> allEntities = new ArrayList<>();
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
    private ComboBox<CutsceneEntityModel> targetCombobox;
    @FXML
    private TextField textTextfield;
    @FXML
    private CheckBox translatedCheckbox;

    public CutsceneDialogScreenModel getScreen() {
        CutsceneEntityModel target = this.targetCheckbox.isSelected()
                ? this.targetCombobox.getSelectionModel().getSelectedItem()
                : null;
        if (target != null && target.getCutsceneID() == -1)
            new Alert(AlertType.WARNING, "Your target needs an ID to work properly.", ButtonType.OK);
        return new CutsceneDialogScreenModel(this.textTextfield.getText(), this.translatedCheckbox.isSelected(),
                target == null ? null : target.getCutsceneID(), this.emotionCombobox.getValue(),
                this.portraitCombobox.getValue());
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

    public void setup(CutsceneDialogScreenModel dialog) {
        this.textTextfield.setText(dialog.getText());
        this.translatedCheckbox.setSelected(dialog.getTranslate());
        this.targetCheckbox.setSelected(dialog.getTarget() != null);
        if (this.targetCheckbox.isSelected())
            for (CutsceneEntityModel e : this.targetCombobox.getItems())
                if (e.getCutsceneID().equals(dialog.getTarget())) {
                    this.targetCombobox.getSelectionModel().select(e);
                    break;
                }
        if (dialog.getPortraitLocation() != null)
            this.portraitCombobox.getSelectionModel().select(dialog.getPortraitLocation());
        this.emotionCombobox.setValue(dialog.getEmotion());
    }

}
