package fr.darkxell.dataeditor.application.controller.sprites;

import fr.darkxell.dataeditor.application.util.CustomTreeItem;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;

public class SpritesTabController
{

	@FXML
	public EditSpriteController editSpriteController;
	@FXML
	private TitledPane editSpritePane;
	@FXML
	private TitledPane spritePreviewPane;
	@FXML
	public TreeView<CustomTreeItem> spritesTreeView;
	@FXML
	public SpritePreviewController testSpriteController;

	public void onCreate()
	{}

	public void onDelete()
	{}

}
