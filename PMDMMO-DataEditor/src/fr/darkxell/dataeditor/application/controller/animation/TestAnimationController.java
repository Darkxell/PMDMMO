package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Direction;

import fr.darkxell.dataeditor.application.util.AnimationListItem;
import fr.darkxell.dataeditor.application.util.AnimationPreviewThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

public class TestAnimationController implements Initializable
{
	private static Floor floor;
	public static DungeonState state;

	private AnimationListItem animation;
	private PokemonAnimation current;
	@FXML
	public ComboBox<Direction> directionCombobox;
	@FXML
	public ImageView imageView;
	@FXML
	public ComboBox<PokemonSpecies> pokemonCombobox;
	@FXML
	public ProgressBar progressBar;
	@FXML
	public CheckBox shinyCheckbox;
	@FXML
	public ComboBox<PokemonSpriteState> stateCombobox;
	private DungeonPokemon tester;
	public AnimationPreviewThread thread;

	public void exitTab()
	{
		this.thread.exit();
	}

	private DungeonPokemon generateTester()
	{
		PokemonSpecies s = this.pokemonCombobox.getValue();
		if (s == null) s = PokemonRegistry.find(1);
		DungeonPokemon pokemon = new DungeonPokemon(s.generate(new Random(), 1, this.shinyCheckbox.isSelected() ? 1 : 0));
		floor.summonPokemon(pokemon, floor.getWidth() / 2, floor.getHeight() / 2, new ArrayList<>());
		pokemon.setFacing(this.directionCombobox.getValue());
		state.pokemonRenderer.register(pokemon);
		state.pokemonRenderer.getRenderer(pokemon).sprite().setDefaultState(this.stateCombobox.getValue(), true);
		return pokemon;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

		this.pokemonCombobox.getItems().addAll(PokemonRegistry.list());
		this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
		this.directionCombobox.getItems().addAll(Direction.directions);

		this.pokemonCombobox.getSelectionModel().select(1);
		this.stateCombobox.setValue(PokemonSpriteState.IDLE);
		this.directionCombobox.setValue(Direction.SOUTH);
	}

	private PokemonAnimation loadAnimation()
	{
		try
		{
			return Animations.getAnimation(tester, this.animation.group + "/" + this.animation.id, thread);
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error while creating animation", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}

	public void onPropertiesChanged()
	{
		Persistance.dungeonState.pokemonRenderer.getRenderer(tester).removeAnimation(this.current);
		state.pokemonRenderer.unregister(tester);
		tester = this.generateTester();
		if (this.current != null) this.playAnimation(true);
	}

	public void onReload()
	{
		this.setAnimation(this.animation);
	}

	public void playAnimation(boolean onChange)
	{
		if (this.animation == null) return;
		if (!onChange && (this.current != null && this.current.plays == -1)) return;
		if (this.current != null)
		{
			this.current.stop();
			if (Persistance.dungeonState.pokemonRenderer.getRenderer(tester) != null)
				Persistance.dungeonState.pokemonRenderer.getRenderer(tester).removeAnimation(this.current);
		}
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = this.current = this.loadAnimation();
		if (s.animation != null) state.setSubstate(s);
	}

	public void reload()
	{
		floor = Persistance.floor = new Floor(1, Layout.find(Layout.LAYOUT_SINGLEROOM),
				Persistance.dungeon = DungeonRegistry.find(1).newInstance(new Random().nextLong()), new Random(), false);
		floor.generate();
		state = Persistance.dungeonState = new DungeonState();

		tester = this.generateTester();

		state.setCamera(tester);
		Persistance.stateManager = new PrincipalMainState();
		// Persistance.stateManager.setState(state);

		Launcher.isRunning = true;
		Launcher.setProcessingProfile(Launcher.PROFILE_SYNCHRONIZED);

		new Thread(this.thread = new AnimationPreviewThread(this)).start();
	}

	public void setAnimation(AnimationListItem animation)
	{
		this.animation = animation;
		thread.cooldown = 0;

		this.playAnimation(true);
	}

	public void updateProgressBar(boolean shouldBeFull)
	{
		if (shouldBeFull) this.progressBar.setProgress(1);
		else if (this.current == null || this.current.duration() == -1) this.progressBar.setProgress(0);
		else this.progressBar.setProgress(Math.min(1, this.current.tick() * 1. / this.current.duration()));
	}

}
