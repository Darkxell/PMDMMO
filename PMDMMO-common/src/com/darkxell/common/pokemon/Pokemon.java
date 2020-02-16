package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.item.ItemAction;
import com.darkxell.common.item.ItemContainer;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.pokemon.BaseStatsModel;
import com.darkxell.common.model.pokemon.EvolutionModel;
import com.darkxell.common.move.Move;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.language.Message;

public class Pokemon implements ItemContainer, HasID {
    /**
     * Pokemon gender.
     * <ul>
     * <li>MALE = 0</li>
     * <li>FEMALE = 1</li>
     * <li>GENDERLESS = 2</li>
     * </ul>
     */
    public static final int MALE = 0, FEMALE = 1, GENDERLESS = 2;
    public static final String XML_ROOT = "pokemon";

    private static ArrayList<DatabaseIdentifier> createMovesList(LearnedMove... moves) {
        ArrayList<DatabaseIdentifier> ids = new ArrayList<>();
        for (LearnedMove move : moves)
            if (move != null)
                ids.add(new DatabaseIdentifier(move.getData().id));
        return ids;
    }

    private DBPokemon data;

    /** A reference to the Dungeon entity of this Pokemon if in a Dungeon. null else. */
    DungeonPokemon dungeonPokemon;

    /** This Pokemon's held Item's ID. -1 for no Item. */
    private ItemStack item;

    /** This Pokemon's moves. */
    private LearnedMove[] moves;

    /** The Player controlling this Pokemon. null if it's an NPC. */
    private Player player;

    /** This Pokemon's species. */
    private PokemonSpecies species;

    /** This Pokemon's stats. */
    private PokemonBaseStats stats;

    public Pokemon(DBPokemon data) {
        this.setData(data);
    }

    public Pokemon(long id, PokemonSpecies species, String nickname, ItemStack item, PokemonBaseStats stats,
            int abilityid, long experience, int level, LearnedMove move1, LearnedMove move2, LearnedMove move3,
            LearnedMove move4, int gender, int iq, boolean shiny) {
        this(new DBPokemon(id, species.getID(), abilityid, gender, nickname, level, experience, iq,
                shiny, stats.getAttack(), stats.getDefense(), stats.getSpecialAttack(), stats.getSpecialDefense(),
                stats.getHealth(), item == null ? null : new DatabaseIdentifier(item.getData().id),
                createMovesList(move1, move2, move3, move4)));
        this.item = item;
        this.moves = new LearnedMove[] { move1, move2, move3, move4 };
    }

    public Pokemon(Pokemon pokemon) {
        this(pokemon.getData().id, pokemon.species(), pokemon.nickname(), pokemon.item(), pokemon.getBaseStats(),
                pokemon.abilityID(), pokemon.experience(), pokemon.level(), pokemon.move(0), pokemon.move(1),
                pokemon.move(2), pokemon.move(3), pokemon.gender(), pokemon.iq(), pokemon.isShiny());
    }

    public Ability ability() {
        return Ability.find(this.abilityID());
    }

    public int abilityID() {
        return this.data.abilityid;
    }

    @Override
    public void addItem(ItemStack item) {
        this.setItem(item);
    }

    @Override
    public int canAccept(ItemStack item) {
        return (!this.hasItem() || (item.item().isStackable() && this.getItem().item().getID() == item.item().getID()))
                ? 0
                : -1;
    }

    @Override
    public long containerID() {
        return this.id();
    }

    @Override
    public Message containerName() {
        return new Message("inventory.held").addReplacement("<pokemon>", this.getNickname());
    }

    @Override
    public ItemContainerType containerType() {
        return ItemContainerType.POKEMON;
    }

    public DungeonPokemon createDungeonPokemon() {
        return new DungeonPokemon(this);
    }

    @Override
    public void deleteItem(int index) {
        this.setItem(null);
    }

    public Message evolutionStatus() {
        ArrayList<EvolutionModel> evolutions = this.species().getEvolutions();
        if (evolutions.isEmpty())
            return new Message("evolve.none");
        for (EvolutionModel evolution : evolutions) {
            if (evolution.getMethod() == EvolutionModel.LEVEL && this.level() >= evolution.getValue())
                return new Message("evolve.possible");
            if (evolution.getMethod() == EvolutionModel.ITEM)
                return new Message("evolve.item");
        }
        return new Message("evolve.not_now");
    }

    public long experience() {
        return this.data.experience;
    }

    /** @return The amount of experience to gain in order to level up. */
    public long experienceLeftNextLevel() {
        return this.species().experienceToNextLevel(this.level()) - this.experience();
    }

    public long experienceToNextLevel() {
        return this.species().experienceToNextLevel(this.level());
    }

    /**
     * @param  amount - The amount of experience gained.
     * @return        The number of levels this experience granted.
     */
    public void gainExperience(ExperienceGainedEvent event, ArrayList<Event> events) {
        int amount = event.experience;
        int levelsup = 0;
        long next = this.experienceLeftNextLevel();
        while (amount != 0) {
            if (next <= amount) {
                amount -= next;
                this.setExperience(0);
                events.add(new LevelupEvent(event.floor, event, this));
            } else {
                this.setExperience(this.experience() + amount);
                amount = 0;
            }
            ++levelsup;
            next = this.species().experienceToNextLevel(this.level() + levelsup);
        }
    }

    public int gender() {
        return this.data.gender;
    }

    public PokemonBaseStats getBaseStats() {
        return this.stats;
    }

    public DBPokemon getData() {
        return this.data;
    }

    public DungeonPokemon getDungeonPokemon() {
        return this.dungeonPokemon;
    }

    public int getIQLevel() {
        final int[] levels = { 10, 50, 100, 150, 200, 300, 400, 500, 600, 700, 990, Integer.MAX_VALUE };
        for (int i = 0; i < levels.length; ++i)
            if (levels[i] > this.iq())
                return i + 1;
        return 1;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getItem();
    }

    public Message getNickname() {
        return (this.nickname() == null ? this.species().speciesName() : new Message(this.nickname(), false))
                .addPrefix(this.player == null ? "<blue>" : "<yellow>").addSuffix("</color>");
    }

    public String getRawNickname() {
        return this.nickname();
    }

    public boolean hasItem() {
        return this.getItem() != null;
    }

    @Override
    public long id() {
        return this.data.id;
    }

    public void increaseIQ(int iq) {
        this.setIq(this.iq() + iq);
    }

    public int iq() {
        return this.data.iq;
    }

    public boolean isAlliedWith(Pokemon pokemon) {
        return this.player() != null && this.player().isAlly(pokemon);
    }

    public boolean isShiny() {
        return this.data.isshiny;
    }

    public ItemStack item() {
        return this.item;
    }

    @Override
    public ArrayList<ItemAction> legalItemActions(boolean inDungeon) {
        ArrayList<ItemAction> actions = new ArrayList<>();
        actions.add(ItemAction.TAKE);
        return actions;
    }

    public int level() {
        return this.data.level;
    }

    public void levelUp(LevelupEvent levelupEvent, ArrayList<Event> events) {
        this.setLevel(this.level() + 1);
        BaseStatsModel stats = this.species().getBaseStats().get(this.level() - 1);
        this.stats.add(stats);
        if (this.dungeonPokemon != null)
            this.dungeonPokemon.stats.onStatChange();

        ArrayList<Move> moves = this.species().learnedMoves(this.level());
        for (Move move : moves)
            events.add(new MoveDiscoveredEvent(levelupEvent.floor, levelupEvent, this, move));
    }

    @Override
    public Tile locationOnFloor() {
        return this.getDungeonPokemon() == null ? null : this.getDungeonPokemon().tile();
    }

    public LearnedMove move(int slot) {
        if (slot < 0 || slot >= this.moves.length)
            return null;
        return this.moves[slot];
    }

    public int moveCount() {
        if (this.moves[3] != null)
            return 4;
        if (this.moves[2] != null)
            return 3;
        if (this.moves[1] != null)
            return 2;
        if (this.moves[0] != null)
            return 1;
        return 0;
    }

    private String nickname() {
        return this.data.nickname;
    }

    public Player player() {
        return this.player;
    }

    public void setData(DBPokemon data) {
        this.data = data;
        this.item = null;
        this.moves = new LearnedMove[4];
        this.species = Registries.species().find(this.data.specieid);
        this.stats = new PokemonBaseStats(this.data.stat_atk, this.data.stat_def, this.data.stat_hp,
                this.data.stat_speatk, this.data.stat_spedef, 1);
        this.stats.pokemon = this;
    }

    private void setExperience(long experience) {
        this.data.experience = experience;
    }

    @Override
    public void setId(long id) {
        this.data.id = id;
        if (this.player != null)
            if (this.player.getTeamLeader() == this)
                this.player.getData().mainpokemon = new DatabaseIdentifier(id);
            else
                this.player.getData().pokemonsinparty.set(this.player.allies.indexOf(this), new DatabaseIdentifier(id));
    }

    private void setIq(int iq) {
        this.data.iq = iq;
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.setItem(item);
    }

    public void setItem(ItemStack item) {
        this.item = item;
        if (item == null)
            this.data.holdeditem = null;
        else
            this.data.holdeditem = new DatabaseIdentifier(item.getData().id);
    }

    private void setLevel(int level) {
        this.data.level = level;
    }

    public void setMove(int slot, LearnedMove move) {
        if (move != null && slot >= 0 && slot < this.moves.length) {
            while (slot > this.moveCount())
                --slot;
            this.moves[slot] = move;
            this.moves[slot].setSlot(slot);
            if (slot == this.data.learnedmoves.size())
                this.data.learnedmoves.add(new DatabaseIdentifier(move.id()));
            else
                this.data.learnedmoves.set(slot, new DatabaseIdentifier(move.id()));
        }
    }

    public void setNickname(String nickname) {
        this.data.nickname = nickname;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public int size() {
        return this.hasItem() ? 1 : 0;
    }

    public PokemonSpecies species() {
        return this.species;
    }

    public void switchMoves(int slot1, int slot2) {
        if (slot1 < 0 || slot1 >= this.moves.length || slot2 < 0 || slot2 >= this.moves.length)
            return;
        LearnedMove temp = this.move(slot1);
        this.setMove(slot1, this.move(slot2));
        this.setMove(slot2, temp);
    }

    @Override
    public String toString() {
        return this.getNickname().toString();
    }

    public long totalExperience() {
        long xp = this.experience();
        for (int lvl = 1; lvl < this.level(); ++lvl)
            xp += this.species().experienceToNextLevel(lvl);
        return xp;
    }

}
