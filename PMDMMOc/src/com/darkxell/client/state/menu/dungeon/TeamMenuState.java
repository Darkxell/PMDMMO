package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class TeamMenuState extends OptionSelectionMenuState
{

	private Pokemon[] pokemon;

	public TeamMenuState(AbstractState background)
	{
		super(background);
		this.pokemon = new Pokemon[]
		{ DungeonPersistance.player.getPokemon() };

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab t = new MenuTab("menu.team");
		this.tabs.add(t);
		for (Pokemon p : this.pokemon)
			t.addOption(new MenuOption(p.getNickname()));
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		Pokemon p = this.pokemon[this.optionIndex()];
		Message stats = new Message("summary.stats.content");
		stats.addReplacement("<level>", Integer.toString(p.getLevel()));
		stats.addReplacement("<exp>", Integer.toString(p.totalExperience()));
		stats.addReplacement("<exp-next>", Integer.toString(p.experienceToNextLevel()));
		stats.addReplacement("<hp>", Integer.toString(p.getStats().health));
		stats.addReplacement("<hp-max>", Integer.toString(p.getStats().health));
		stats.addReplacement("<atk>", Integer.toString(p.getStats().attack));
		stats.addReplacement("<def>", Integer.toString(p.getStats().defense));
		stats.addReplacement("<spa>", Integer.toString(p.getStats().specialAttack));
		stats.addReplacement("<spd>", Integer.toString(p.getStats().specialDefense));
		stats.addReplacement("<item>", p.getItem() == null ? new Message("item.none") : p.getItem().name());
		stats.addReplacement("<iq>", "<star>");

		Message features = new Message("summary.features.content");
		features.addReplacement("<firsttype>", p.species.type1.getName());
		features.addReplacement("<secondtype>", p.species.type2 == null ? new Message("", false) : p.species.type2.getName());
		features.addReplacement("<ability>", p.getAbility().description());

		Message info = new Message("summary.info.content");
		info.addReplacement("<species>", p.species.name());
		info.addReplacement("<area>", new Message("Stratos Lookout", false));
		info.addReplacement("<joined>", new Message("Pokémon Square", false));
		info.addReplacement("<evolve>", p.evolutionStatus());

		Launcher.stateManager.setState(new InfoState(this.backgroundState, this, new Message[]
		{ new Message("summary.stats"), new Message("summary.features"), new Message("summary.info") }, new Message[]
		{ stats, features, info }));
	}

}
