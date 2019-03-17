package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import com.darkxell.common.util.Direction;

public class SelectVariantController implements Initializable {

    @FXML
    public ListView<Direction> directionList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.directionList.getItems().addAll(Direction.DIRECTIONS);
        this.directionList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            this.onSelect(newValue);
        });
    }

    public void onCancel() {
        EditAnimationController.variantPopup.close();
    }

    private void onSelect(Direction direction) {
        EditAnimationController.variantPopup.close();
        EditAnimationController.instance.onEditVariant(direction);
    }

}
