package fr.darkxell.dataeditor.application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable
{

	@FXML
	private Tab cutscenesTab;
	@FXML
	private CutscenesTabController cutscenesTabPageController;
	@FXML
	private Tab langTab;
	@FXML
	public LangTabController langTabPageController;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{}

}
