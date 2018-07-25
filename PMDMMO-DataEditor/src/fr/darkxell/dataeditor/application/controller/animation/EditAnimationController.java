package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;

import fr.darkxell.dataeditor.application.util.AnimationListItem;
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

	private AnimationListItem animation;
	private AbstractAnimation current;

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
			// Persistance.stateManager.setState(state);

			Launcher.isRunning = true;
			Launcher.setProcessingProfile(Launcher.PROFILE_SYNCHRONIZED);

			new Thread(thread = new AnimationPreviewThread()).start();
		}
	}

	private AbstractAnimation loadAnimation()
	{
		switch (this.animation.folder)
		{
			case "custom":
				return Animations.getCustomAnimation(tester, this.animation.id, thread);

			case "abilities":
				return Animations.getAbilityAnimation(tester, Ability.find(this.animation.id), thread);

			case "items":
				return Animations.getItemAnimation(tester, ItemRegistry.find(this.animation.id), thread);

			case "moves":
				return Animations.getMoveAnimation(tester, MoveRegistry.find(this.animation.id), thread);

			case "statuses":
				return Animations.getStatusAnimation(tester, StatusCondition.find(this.animation.id), thread);

			case "targets":
				return Animations.getMoveTargetAnimation(tester, MoveRegistry.find(this.animation.id), thread);

			default:
				return null;
		}
	}

	public void onReload()
	{
		Animations.loadData();

		this.setAnimation(this.animation);
	}

	public void playAnimation()
	{
		if (this.animation == null) return;
		Persistance.dungeonState.pokemonRenderer.getRenderer(tester).removeAnimation(this.current);
		AnimationState s = new AnimationState(Persistance.dungeonState);
		this.current = s.animation = this.loadAnimation();
		if (s.animation != null) state.setSubstate(s);
	}

	public void setAnimation(AnimationListItem animation)
	{
		this.animation = animation;

		this.playAnimation();
	}

}
