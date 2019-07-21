package fr.darkxell.dataeditor.application;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.client.resources.images.SpriteLoader;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Util;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.weather.Weather;

import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DataEditor extends Application {

	public static Stage primaryStage;

	public static void main(String[] args) {
		Launcher.isRunning = true;
		ClientSettings.load();
		Logger.load("DATA EDITOR");
		Localization.load(false);
		SpriteFactory.load();
		Registries.load();
		SpriteLoader.loadCommon();
		PokemonSpritesets.loadData("../PMDMMOc/resources/pokemons/data");
		PokemonPortrait.load();
		Animations.loadData();
		SoundsHolder.load("../PMDMMOc/resources");
		Persistence.soundmanager = new SoundManager();
		Persistence.player = Util.createDefaultPlayer();

		new Weather(-1, null);

		// new DiscordEventHandlerForPMDMMO("Developing game",
		// "main_develop").start();

		Cutscenes.load();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		DataEditor.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(DataEditor.class.getResource("/layouts/main.fxml"));
			Scene scene = new Scene(root, 1000, 850);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons()
					.add(new Image(DataEditor.class.getResourceAsStream("/hud/framebackgrounds/icon.png")));
			primaryStage.setTitle("PMDMMO Data Editor");
			// primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		Launcher.stopGame();
		System.exit(0);
	}
}
