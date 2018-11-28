package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class SpritesTabController implements Initializable
{

	@FXML
	public EditSpriteController editSpriteController;
	@FXML
	private TitledPane editSpritePane;
	@FXML
	private TitledPane spritePreviewPane;
	@FXML
	public ListView<PokemonSpritesetData> spritesList;
	@FXML
	public SpritePreviewController testSpriteController;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.editSpritePane.setVisible(false);
		this.spritePreviewPane.setVisible(false);
		
		this.spritesList.getItems().addAll(PokemonSpritesets.listSpritesetData());
	}

	public void onCreate()
	{}

	public void onDelete()
	{}

}
