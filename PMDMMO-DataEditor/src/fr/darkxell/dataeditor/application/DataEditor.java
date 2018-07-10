package fr.darkxell.dataeditor.application;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Lang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DataEditor extends Application
{

	public static void main(String[] args)
	{
		ClientSettings.load();
		Logger.loadClient();
		Lang.load();
		PokemonRegistry.load();
		MoveRegistry.load();
		ItemRegistry.load();
		TrapRegistry.load();
		DungeonRegistry.load();
		PokemonSpritesets.loadData();
		Animations.loadData();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			Parent root = FXMLLoader.load(DataEditor.class.getResource("/layouts/main.fxml"));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(DataEditor.class.getResourceAsStream("/hud/framebackgrounds/icon.png")));
			primaryStage.setTitle("PMDMMO Data Editor");
			primaryStage.setHeight(600);
			primaryStage.setWidth(800);
			// primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception
	{
		// Called when app exits
		super.stop();
	}
}
