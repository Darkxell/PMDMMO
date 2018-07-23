package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonRegistry;

import fr.darkxell.dataeditor.application.util.AnimationPreviewThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class EditAnimationController implements Initializable
{
	private static Floor floor;
	public static EditAnimationController instance;
	public static DungeonState state;
	private static DungeonPokemon tester;
	public static AnimationPreviewThread thread;

	@FXML
	public ImageView imageView;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;

		if (floor == null)
		{
			floor = Persistance.floor = new Floor(1, Layout.find(Layout.LAYOUT_SINGLEROOM),
					Persistance.dungeon = DungeonRegistry.find(1).newInstance(new Random().nextLong()), new Random(), false);
			floor.generate();

			tester = new DungeonPokemon(PokemonRegistry.find(1).generate(new Random(), 5));
			floor.summonPokemon(tester, floor.getWidth() / 2, floor.getHeight() / 2, new ArrayList<>());

			state = Persistance.dungeonState = new DungeonState();
			state.setCamera(tester);
			Persistance.stateManager = new PrincipalMainState();
			//Persistance.stateManager.setState(state);

			Launcher.isRunning = true;
			Launcher.setProcessingProfile(Launcher.PROFILE_SYNCHRONIZED);

			new Thread(thread = new AnimationPreviewThread()).start();
		}
	}

	public void onReload()
	{}

}
