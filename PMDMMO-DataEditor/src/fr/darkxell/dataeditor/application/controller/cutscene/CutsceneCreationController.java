package fr.darkxell.dataeditor.application.controller.cutscene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.CutsceneCreationModel;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.common.zones.FreezoneInfo;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class CutsceneCreationController implements Initializable, ListCellParent<CutsceneEntityModel> {

    public static Stage editEntityPopup;

    @FXML
    private TextField cameraXTextfield;
    @FXML
    private TextField cameraYTextfield;
    @FXML
    private CheckBox drawMapCheckbox;
    @FXML
    public ListView<CutsceneEntityModel> entitiesList;
    private int entityEditing;
    @FXML
    private ComboBox<String> fadingCombobox;
    @FXML
    private ComboBox<FreezoneInfo> freezoneCombobox;

    public CutsceneCreationModel getCreation() {
        return new CutsceneCreationModel(this.freezoneCombobox.getValue(),
                Double.parseDouble(this.cameraXTextfield.getText()),
                Double.parseDouble(this.cameraYTextfield.getText()),
                this.fadingCombobox.getSelectionModel().getSelectedIndex() == 0, this.drawMapCheckbox.isSelected(),
                new ArrayList<>(this.entitiesList.getItems()));
    }

    @Override
    public Node graphicFor(CutsceneEntityModel item) {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fadingCombobox.getItems().addAll("No fading", "Fading in");
        this.fadingCombobox.getSelectionModel().select(0);

        this.freezoneCombobox.getItems().addAll(FreezoneInfo.values());
        this.freezoneCombobox.getSelectionModel().select(0);

        Pattern pattern = Pattern.compile("-?\\d*(\\.\\d*)?");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.cameraXTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.cameraYTextfield.setTextFormatter(formatter);

        CustomList.setup(this, this.entitiesList, "Cutscene Entity", true, true, true, true, false);
        this.drawMapCheckbox.setSelected(true);
    }

    @Override
    public void onCreate(CutsceneEntityModel entity) {
        this.onEdit(null);
    }

    public void onCreateEntity() {
        this.onCreate(null);
    }

    @Override
    public void onDelete(CutsceneEntityModel item) {
        this.entitiesList.getItems().remove(item);
    }

    @Override
    public void onEdit(CutsceneEntityModel entity) {
        try {
            FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/cutscenes/edit_entity.fxml"));
            Parent root = loader.load();
            EditEntityController controller = loader.getController();
            if (entity != null) {
                controller.setupFor(entity);
                this.entityEditing = this.entitiesList.getItems().indexOf(entity);
            } else
                this.entityEditing = -1;
            editEntityPopup = FXUtils.showPopup(root, (entity == null ? "New" : "Edit") + " Cutscene Entity");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEntityEdited(CutsceneEntityModel entity) {
        if (this.entityEditing == -1)
            this.entitiesList.getItems().add(entity);
        else
            this.entitiesList.getItems().set(this.entityEditing, entity);
    }

    @Override
    public void onMove(CutsceneEntityModel item, int newIndex) {
    }

    @Override
    public void onRename(CutsceneEntityModel item, String name) {
    }

    public void setupFor(CutsceneModel cutscene) {
        CutsceneCreationModel creation = cutscene.getCreation();
        this.freezoneCombobox.setValue(creation.getFreezone());
        this.cameraXTextfield.setText(String.valueOf(creation.getCameraX()));
        this.cameraYTextfield.setText(String.valueOf(creation.getCameraY()));
        this.fadingCombobox.getSelectionModel().select(creation.isFading() ? 1 : 0);
        this.drawMapCheckbox.setSelected(creation.isDrawmap());
        this.entitiesList.getItems().clear();
        this.entitiesList.getItems().addAll(creation.getEntities());
    }

}
