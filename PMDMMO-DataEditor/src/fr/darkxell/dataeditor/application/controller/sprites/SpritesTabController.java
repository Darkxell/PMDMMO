package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.util.XMLUtils;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.PokemonSpritesetManager;
import fr.darkxell.dataeditor.application.util.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;

public class SpritesTabController implements Initializable, ListCellParent<PokemonSpritesetData>
{
	public PokemonSpritesetData currentSprite;

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
	public Node graphicFor(PokemonSpritesetData item)
	{
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.editSpritePane.setVisible(false);
		this.spritePreviewPane.setVisible(false);

		this.reloadList();
		CustomList.setup(this, this.spritesList, "Pokemon Sprite", true, false, true, true, false);
	}

	public void onCreate()
	{
		this.onCreate(null);
	}

	@Override
	public void onCreate(PokemonSpritesetData nullItem)
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("New Spriteset");
		dialog.setHeaderText(null);
		dialog.setContentText("Type in the ID of the Pokemon corresponding to this Spriteset.");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent())
		{
			try
			{
				Integer id = Integer.parseInt(name.get());
				if (PokemonSpritesets.dataExists(id))
					new Alert(AlertType.ERROR, "There is already a Spriteset with ID " + name.get(), ButtonType.OK).showAndWait();
				else
				{
					PokemonSpritesetData data = new PokemonSpritesetData(id);
					XMLUtils.saveFile(FileManager.create(FileManager.filePaths.get(FileManager.POKEMON_SPRITES) + "/" + id + ".xml"), data.toXML());
					PokemonSpritesets.loadSpritesetData(data.id);
					this.reloadList();
					data = PokemonSpritesets.getData(data.id);
					this.spritesList.getSelectionModel().select(data);
					this.onEdit(data);
				}
			} catch (NumberFormatException e)
			{
				new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK).showAndWait();
			}
		}
	}

	@Override
	public void onDelete(PokemonSpritesetData item)
	{
		if (item == this.currentSprite)
		{
			this.currentSprite = null;
			this.editSpritePane.setVisible(false);
			this.spritePreviewPane.setVisible(false);
		}
		PokemonSpritesetManager.remove(item);
		this.reloadList();
	}

	@Override
	public void onEdit(PokemonSpritesetData item)
	{
		this.currentSprite = item;
		this.editSpritePane.setVisible(true);
		this.spritePreviewPane.setVisible(true);

		this.editSpriteController.setupFor(item);
		this.testSpriteController.setupFor(item);
	}

	@Override
	public void onMove(PokemonSpritesetData item, int newIndex)
	{}

	@Override
	public void onRename(PokemonSpritesetData item, String name)
	{}

	private void reloadList()
	{
		int selected = this.spritesList.getSelectionModel().getSelectedIndex();
		this.spritesList.getItems().clear();
		this.spritesList.getItems().addAll(PokemonSpritesets.listSpritesetData());
		if (selected != -1) this.spritesList.getSelectionModel().select(selected);
	}

}
