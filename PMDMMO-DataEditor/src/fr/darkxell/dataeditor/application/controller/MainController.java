package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.util.language.Lang;

import fr.darkxell.dataeditor.application.controller.animation.AnimationsTabController;
import fr.darkxell.dataeditor.application.controller.cutscene.CutscenesTabController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable, ChangeListener<Boolean>
{

	@FXML
	private Tab cutscenesTab;
	@FXML
	private Tab animationsTab;
	@FXML
	private CutscenesTabController cutscenesTabPageController;
	@FXML
	private AnimationsTabController animationsTabPageController;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.animationsTab.selectedProperty().addListener(this);
	}

	public void onReloadLang()
	{
		Lang.load();
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		if (newValue) this.animationsTabPageController.editAnimationController.reload();
		else this.animationsTabPageController.editAnimationController.exitTab();
	}

}
