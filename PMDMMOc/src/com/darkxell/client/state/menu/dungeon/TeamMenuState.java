package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class TeamMenuState extends OptionSelectionMenuState {

	public static interface TeamMemberSelectionListener {
		public void teamMemberSelected(Pokemon pokemon);
	}

	public final TeamMemberSelectionListener listener;
	private Pokemon[] pokemon;

	public TeamMenuState(AbstractState background) {
		this(background, null);
	}

	public TeamMenuState(AbstractState background, TeamMemberSelectionListener listener) {
		super(background);
		this.pokemon = Persistance.player.getTeam();
		this.listener = listener;

		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab t = new MenuTab("menu.team");
		this.tabs.add(t);
		for (Pokemon p : this.pokemon)
			t.addOption(new MenuOption(p.getNickname()));
	}

	@Override
	protected void onExit() {
		Persistance.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		Pokemon p = this.pokemon[this.optionIndex()];
		if (this.listener == null) {
			Message stats = new Message("summary.stats.content");
			stats.addReplacement("<level>", TextRenderer.instance.alignNumber(p.getLevel(), 7));
			stats.addReplacement("<exp>", TextRenderer.instance.alignNumber(p.totalExperience(), 7));
			stats.addReplacement("<exp-next>", TextRenderer.instance.alignNumber(p.experienceToNextLevel(), 7));
			stats.addReplacement("<hp>", TextRenderer.instance.alignNumber(p.getStats().getHealth(), 3));
			stats.addReplacement("<hp-max>", TextRenderer.instance.alignNumber(p.getStats().getHealth(), 3));
			stats.addReplacement("<atk>", TextRenderer.instance.alignNumber(p.getStats().getAttack(), 7));
			stats.addReplacement("<def>", TextRenderer.instance.alignNumber(p.getStats().getDefense(), 7));
			stats.addReplacement("<spa>", TextRenderer.instance.alignNumber(p.getStats().getSpecialAttack(), 7));
			stats.addReplacement("<spd>", TextRenderer.instance.alignNumber(p.getStats().getSpecialDefense(), 7));
			stats.addReplacement("<item>", p.getItem() == null ? new Message("item.none") : p.getItem().name());
			stats.addReplacement("<iq>", "<star>");

			Message features = new Message("summary.features.content");
			features.addReplacement("<firsttype>", p.species.type1.getName());
			features.addReplacement("<secondtype>",
					p.species.type2 == null ? new Message("", false) : p.species.type2.getName());
			features.addReplacement("<ability>", p.getAbility().description());

			Message info = new Message("summary.info.content");
			info.addReplacement("<species>", p.species.name());
			info.addReplacement("<area>", new Message("Stratos Lookout", false));
			info.addReplacement("<joined>", new Message("Pokémon Square", false));
			info.addReplacement("<evolve>", p.evolutionStatus());

			Persistance.stateManager.setState(new InfoState(
					this.backgroundState, this, new Message[] { new Message("summary.stats"),
							new Message("summary.features"), new Message("summary.info") },
					new Message[] { stats, features, info }));
		} else
			this.listener.teamMemberSelected(p);
	}

}
