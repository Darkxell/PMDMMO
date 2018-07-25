package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.util.language.Lang;

import fr.darkxell.dataeditor.application.controller.animation.AnimationsTabController;
import fr.darkxell.dataeditor.application.controller.cutscene.CutscenesTabController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable
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
	{}

	public void onReloadLang()
	{
		Lang.load();
	}

}
