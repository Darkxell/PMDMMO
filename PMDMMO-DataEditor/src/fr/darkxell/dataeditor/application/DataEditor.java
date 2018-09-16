package fr.darkxell.dataeditor.application;

import com.darkxell.client.discord.DiscordEventHandlerForPMDMMO;
import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.client.resources.images.SpriteLoader;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Util;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.weather.Weather;

import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DataEditor extends Application
{

	public static Stage primaryStage;

	public static void main(String[] args)
	{
		Launcher.isRunning = true;
		ClientSettings.load();
		Logger.loadClient();
		Lang.load();
		SpriteFactory.load();
		PokemonRegistry.load();
		MoveRegistry.load();
		ItemRegistry.load();
		TrapRegistry.load();
		DungeonRegistry.load();
		SpriteLoader.loadCommon();
		PokemonSpritesets.loadData();
		Animations.loadData();
		SoundsHolder.load("../PMDMMOc/");
		Persistance.soundmanager = new SoundManager();
		Persistance.player = Util.createDefaultPlayer();

		new Weather(-1, null);

		new DiscordEventHandlerForPMDMMO("Developing game", "main_develop").start();

		Cutscenes.load();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		DataEditor.primaryStage = primaryStage;
		try
		{
			Parent root = FXMLLoader.load(DataEditor.class.getResource("/layouts/main.fxml"));
			Scene scene = new Scene(root, 1000, 850);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(DataEditor.class.getResourceAsStream("/hud/framebackgrounds/icon.png")));
			primaryStage.setTitle("PMDMMO Data Editor");
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
		super.stop();
		Launcher.stopGame();
		System.exit(0);
	}
}
