package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.util.language.Localization;

import fr.darkxell.dataeditor.application.controller.animation.AnimationsTabController;
import fr.darkxell.dataeditor.application.controller.cutscene.CutscenesTabController;
import fr.darkxell.dataeditor.application.controller.dungeon.DungeonsTabController;
import fr.darkxell.dataeditor.application.controller.item.ItemsTabController;
import fr.darkxell.dataeditor.application.controller.move.MovesTabController;
import fr.darkxell.dataeditor.application.controller.sprites.SpritesTabController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable, ChangeListener<Boolean> {

    @FXML
    private Tab animationsTab;
    @FXML
    private AnimationsTabController animationsTabPageController;
    @FXML
    private Tab cutscenesTab;
    @FXML
    private CutscenesTabController cutscenesTabPageController;
    @FXML
    private Tab dungeonsTab;
    @FXML
    private DungeonsTabController dungeonsTabPageController;
    @FXML
    private Tab itemsTab;
    @FXML
    private ItemsTabController itemsTabPageController;
    @FXML
    private Tab movesTab;
    @FXML
    private MovesTabController movesTabPageController;
    @FXML
    private Tab pkspritesTab;
    @FXML
    private SpritesTabController pkspritesTabPageController;

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (observable == this.animationsTab.selectedProperty()) {
            if (newValue)
                this.animationsTabPageController.testAnimationController.reload();
            else
                this.animationsTabPageController.testAnimationController.exitTab();
        } else if (newValue)
            this.pkspritesTabPageController.testSpriteController.reload();
        else
            this.pkspritesTabPageController.testSpriteController.exitTab();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.animationsTab.selectedProperty().addListener(this);
        this.pkspritesTab.selectedProperty().addListener(this);
    }

    public void onReloadLang() {
        Localization.load(true);
    }

}
