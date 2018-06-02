package com.darkxell.client.state.menu;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class TeamMenuState extends OptionSelectionMenuState implements ItemActionMessageHandler
{

	public static interface TeamMemberSelectionListener
	{
		public void teamMemberSelected(Pokemon pokemon);
	}

	public final TeamMemberSelectionListener listener;
	private final AbstractState parent;
	private Pokemon[] pokemon;

	public TeamMenuState(AbstractState parent, AbstractState background)
	{
		this(parent, background, null);
	}

	public TeamMenuState(AbstractState parent, AbstractState background, TeamMemberSelectionListener listener)
	{
		super(background);
		this.pokemon = Persistance.player.getTeam();
		this.parent = parent;
		this.listener = listener;

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
	public void handleMessage(JsonObject message)
	{
		if (this.parent != null && this.parent instanceof ItemActionMessageHandler) ((ItemActionMessageHandler) this.parent).handleMessage(message);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		Pokemon p = this.pokemon[this.optionIndex()];
		DungeonPokemon dp = p.getDungeonPokemon();
		if (this.listener == null)
		{
			Message stats = new Message("summary.stats.content");
			stats.addReplacement("<level>", TextRenderer.alignNumber(p.level(), 7));
			stats.addReplacement("<exp>", TextRenderer.alignNumber(p.totalExperience(), 7));
			stats.addReplacement("<exp-next>", TextRenderer.alignNumber(p.experienceToNextLevel(), 7));
			stats.addReplacement("<hp>", TextRenderer.alignNumber(dp == null ? p.getBaseStats().getHealth() : dp.getHp(), 3));
			stats.addReplacement("<hp-max>", TextRenderer.alignNumber(p.getBaseStats().getHealth(), 3));
			stats.addReplacement("<atk>", TextRenderer.alignNumber(p.getBaseStats().getAttack(), 7));
			stats.addReplacement("<def>", TextRenderer.alignNumber(p.getBaseStats().getDefense(), 7));
			stats.addReplacement("<spa>", TextRenderer.alignNumber(p.getBaseStats().getSpecialAttack(), 7));
			stats.addReplacement("<spd>", TextRenderer.alignNumber(p.getBaseStats().getSpecialDefense(), 7));
			stats.addReplacement("<item>", p.getItem() == null ? new Message("item.none") : p.getItem().name());

			String iq = "";
			int iqLevel = p.getIQLevel();
			if (iqLevel == 12) iq = "MAX";
			else for (int i = 0; i < iqLevel; ++i)
				iq += "<star>";
			stats.addReplacement("<iq>", iq);

			Message features = new Message("summary.features.content");
			features.addReplacement("<firsttype>", p.species().type1.getName());
			features.addReplacement("<secondtype>", p.species().type2 == null ? new Message("", false) : p.species().type2.getName());
			features.addReplacement("<ability>", p.ability().description());

			Message info = new Message("summary.info.content");
			info.addReplacement("<species>", p.species().formName());
			info.addReplacement("<area>", new Message("Stratos Lookout", false));
			info.addReplacement("<joined>", new Message("Pokémon Square", false));
			info.addReplacement("<evolve>", p.evolutionStatus());

			Persistance.stateManager.setState(new InfoState(this.backgroundState, this,
					new Message[] { new Message("summary.stats"), new Message("summary.features"), new Message("summary.info") },
					new Message[] { stats, features, info }));
		} else this.listener.teamMemberSelected(p);
	}

}
