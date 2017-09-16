package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.TextRenderer;
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
		stats.addReplacement("<level>", TextRenderer.instance.alignNumber(p.getLevel(), 7));
		stats.addReplacement("<exp>", TextRenderer.instance.alignNumber(p.totalExperience(), 7));
		stats.addReplacement("<exp-next>", TextRenderer.instance.alignNumber(p.experienceToNextLevel(), 7));
		stats.addReplacement("<hp>", TextRenderer.instance.alignNumber(p.getStats().health, 3));
		stats.addReplacement("<hp-max>", TextRenderer.instance.alignNumber(p.getStats().health, 3));
		stats.addReplacement("<atk>", TextRenderer.instance.alignNumber(p.getStats().attack, 7));
		stats.addReplacement("<def>", TextRenderer.instance.alignNumber(p.getStats().defense, 7));
		stats.addReplacement("<spa>", TextRenderer.instance.alignNumber(p.getStats().specialAttack, 7));
		stats.addReplacement("<spd>", TextRenderer.instance.alignNumber(p.getStats().specialDefense, 7));
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
