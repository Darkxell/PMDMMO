package com.darkxell.client.state.menu.menus;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonObject;

public class TeamMenuState extends OptionSelectionMenuState implements ItemActionMessageHandler {

    public static interface TeamMemberSelectionListener {
        public void teamMemberSelected(Pokemon pokemon);
    }

    public static InfoState createSummaryState(AbstractGraphiclayer background, AbstractState parent,
            DungeonPokemon dungeon, Pokemon pokemon) {
        Message stats = new Message("summary.stats.content");
        stats.addReplacement("<level>", TextRenderer.alignNumber(pokemon.level(), 7));
        stats.addReplacement("<exp>", TextRenderer.alignNumber(pokemon.totalExperience(), 7));
        stats.addReplacement("<exp-next>", TextRenderer.alignNumber(pokemon.experienceToNextLevel(), 7));
        stats.addReplacement("<hp>",
                TextRenderer.alignNumber(dungeon == null ? pokemon.getBaseStats().getHealth() : dungeon.getHp(), 3));
        stats.addReplacement("<hp-max>", TextRenderer.alignNumber(pokemon.getBaseStats().getHealth(), 3));
        stats.addReplacement("<atk>", TextRenderer.alignNumber(pokemon.getBaseStats().getAttack(), 7));
        stats.addReplacement("<def>", TextRenderer.alignNumber(pokemon.getBaseStats().getDefense(), 7));
        stats.addReplacement("<spa>", TextRenderer.alignNumber(pokemon.getBaseStats().getSpecialAttack(), 7));
        stats.addReplacement("<spd>", TextRenderer.alignNumber(pokemon.getBaseStats().getSpecialDefense(), 7));
        stats.addReplacement("<item>", pokemon.hasItem() ? pokemon.getItem().name() : new Message("item.none"));

        String iq = "";
        int iqLevel = pokemon.getIQLevel();
        if (iqLevel == 12)
            iq = "MAX";
        else
            for (int i = 0; i < iqLevel; ++i)
                iq += "<star>";
        stats.addReplacement("<iq>", iq);

        Message features = new Message("summary.features.content");
        features.addReplacement("<firsttype>", pokemon.species().type1.getName());
        features.addReplacement("<secondtype>",
                pokemon.species().type2 == null ? new Message("", false) : pokemon.species().type2.getName());
        features.addReplacement("<ability>", pokemon.ability().description());

        Message info = new Message("summary.info.content");
        info.addReplacement("<species>", pokemon.species().formName());
        info.addReplacement("<area>", FreezoneInfo.find(pokemon.species().friendAreaID).getName());
        info.addReplacement("<joined>", new Message("Pokemon Square", false));
        info.addReplacement("<evolve>", pokemon.evolutionStatus());

        return new InfoState(background, parent, new Message[] { new Message("summary.stats"),
                new Message("summary.features"), new Message("summary.info") },
                new Message[] { stats, features, info });
    }

    public static InfoState createSummaryState(AbstractGraphiclayer background, AbstractState parent,
            Pokemon pokemon) {
        return createSummaryState(background, parent, null, pokemon);
    }

    public final TeamMemberSelectionListener listener;
    private final AbstractState parent;
    private Pokemon[] pokemon;

    public TeamMenuState(AbstractState parent, AbstractGraphiclayer background) {
        this(parent, background, null);
    }

    public TeamMenuState(AbstractState parent, AbstractGraphiclayer background, TeamMemberSelectionListener listener) {
        super(background);
        this.pokemon = Persistence.player.getTeam();
        this.parent = parent;
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
    public void handleMessage(JsonObject message) {
        if (this.parent != null && this.parent instanceof ItemActionMessageHandler)
            ((ItemActionMessageHandler) this.parent).handleMessage(message);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        Pokemon p = this.pokemon[this.optionIndex()];
        DungeonPokemon dp = p.getDungeonPokemon();
        if (this.listener == null)
            Persistence.stateManager.setState(createSummaryState(this.background, this, dp, p));
        else
            this.listener.teamMemberSelected(p);
    }

}
