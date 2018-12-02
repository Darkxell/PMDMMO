package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EditTableController implements Initializable
{

	@FXML
	public TableColumn directionColumn;
	public SpritesTabController parent;
	@FXML
	public TableColumn sequenceColumn;
	@FXML
	public TableColumn stateColumn;
	@FXML
	public TableView table;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{}

	public void setupFor(PokemonSpritesetData item)
	{}

}
