package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import fr.darkxell.dataeditor.application.controller.cutscene.CutscenesTabController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable
{

	@FXML
	private Tab cutscenesTab;
	@FXML
	private CutscenesTabController cutscenesTabPageController;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{}

	public void onReloadLang()
	{
		System.out.println("reload");
	}

}
