package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventListener;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

/** Represents a Pokemon in a Dungeon. */
public class DungeonPokemon implements ItemContainer, EventListener {
    public enum DungeonPokemonType {
        BOSS,
        MINIBOSS,
        RESCUEABLE,
        TEAM_MEMBER,
        WILD;

        public boolean isAlliedWith(DungeonPokemonType type) {
            switch (this) {
            case BOSS:
            case MINIBOSS:
            case WILD:
                return type != TEAM_MEMBER;

            case RESCUEABLE:
                return true;

            case TEAM_MEMBER:
                return type == TEAM_MEMBER || type == RESCUEABLE;

            default:
                return true;
            }
        }
    }

    public static final int DEFAULT_BELLY_SIZE = 100;
    public static final byte REGULAR_ATTACKS = 0, MOVES = 1, LINKED_MOVES = 2;

    /** The attacks this Pokemon has received. Use in experience calculation. */
    private byte attacksReceived = REGULAR_ATTACKS;
    /** This Pokemon's current belly points. */
    private double belly;
    /** This Pokemon's belly size. */
    private int bellySize;
    /** The direction this Pokemon is facing. */
    private Direction facing = Direction.SOUTH;
    /** This Pokemon's current Hit Points. */
    private int hp;
    /**
     * The original Pokemon that entered the Dungeon. This object is necessary for Dungeons that modify the visiting
     * Pokemon, such as Dungeons resetting the level to 1.
     */
    public final Pokemon originalPokemon;
    /** Variable used to compute HP regeneration. */
    private int regenCounter = 0;
    /** This Pokemon's stats for the current dungeon. */
    public final DungeonStats stats;
    /** This Pokemon's active Status Conditions. */
    private final ArrayList<AppliedStatusCondition> statusConditions;
    /** The tile this Pokemon is standing on. */
    private Tile tile;
    /** This Pokemon's {@link DungeonPokemonType}. */
    public DungeonPokemonType type;
    /** The Pokemon to use in the current Dungeon. See {@link DungeonPokemon#originalPokemon}. */
    public final Pokemon usedPokemon;

    public DungeonPokemon(Pokemon pokemon) {
        this.usedPokemon = pokemon;
        this.originalPokemon = pokemon;
        this.stats = new DungeonStats(this);
        this.belly = this.bellySize = DEFAULT_BELLY_SIZE;
        this.hp = this.stats.getHealth();
        this.statusConditions = new ArrayList<>();
        this.type = pokemon.player() == null ? DungeonPokemonType.WILD : DungeonPokemonType.TEAM_MEMBER;
        pokemon.dungeonPokemon = this;
    }

    public Ability ability() {
        return this.usedPokemon.ability();
    }

    public AppliedStatusCondition[] activeStatusConditions() {
        AppliedStatusCondition[] conditions = new AppliedStatusCondition[this.statusConditions.size()];
        for (int i = 0; i < conditions.length; ++i)
            conditions[i] = this.statusConditions.get(i);
        return conditions;
    }

    @Override
    public void addItem(ItemStack item) {
        this.usedPokemon.addItem(item);
        if (this.isCopy())
            this.originalPokemon.addItem(item);
    }

    @Override
    public int canAccept(ItemStack item) {
        return this.usedPokemon.canAccept(item);
    }

    /** @return <code>false</code> if this Pokemon has no other choice but to skip their turn. */
    public boolean canAct(Floor floor) {
        return this.canAttack(floor) || this.canMove(floor); // TODO Add (hasItem() && canUseItem(getItem())) when Item
                                                             // use AI is done
    }

    public boolean canAttack(Floor floor) {
        for (AppliedStatusCondition instance : this.statusConditions)
            if (instance.condition.preventsUsingMove(null, this, floor))
                return false;
        return true;
    }

    public boolean canMove(Floor floor) {
        for (AppliedStatusCondition instance : this.statusConditions)
            if (instance.condition.preventsMoving(this, floor))
                return false;
        return true;
    }

    /** @return True if this Pokemon can regenerate HP. */
    public boolean canRegen() {
        if (this.type != DungeonPokemonType.TEAM_MEMBER)
            return false;
        return !this.hasStatusCondition(StatusConditions.Badly_poisoned)
                && !this.hasStatusCondition(StatusConditions.Poisoned);
    }

    public boolean canUse(Item item, Floor floor) {
        for (AppliedStatusCondition instance : this.statusConditions)
            if (instance.condition.preventsUsingItem(item, this, floor))
                return false;
        return true;
    }

    public boolean canUse(LearnedMove move, Floor floor) {
        for (AppliedStatusCondition instance : this.statusConditions)
            if (instance.condition.preventsUsingMove(move, this, floor))
                return false;
        return move.isEnabled();
    }

    @Override
    public long containerID() {
        return this.id();
    }

    @Override
    public Message containerName() {
        return this.usedPokemon.containerName();
    }

    @Override
    public ItemContainerType containerType() {
        return ItemContainerType.DUNGEON_POKEMON;
    }

    @Override
    public void deleteItem(int index) {
        this.originalPokemon.deleteItem(index);
    }

    /** Clears references to this Dungeon Pokemon in the Pokemon object. */
    public void dispose() {
        this.originalPokemon.dungeonPokemon = null;
    }

    /**
     * @return The multiplier to apply to base energy values for the team leader's actions. Used to determine how much
     *         belly is lost for that action.
     */
    public double energyMultiplier() {
        return 1;
    }

    /** @return The amount of experience gained when defeating this Pokemon. */
    public int experienceGained() {
        int base = this.usedPokemon.species().baseXP;
        base += Math.floor(base * (this.originalPokemon.level() - 1) / 10);
        if (this.attacksReceived == REGULAR_ATTACKS)
            base = (int) (base * 0.5);
        else if (this.attacksReceived == LINKED_MOVES)
            base = (int) (base * 1.5);
        return base;
    }

    /** @return The direction this Pokemon is facing. */
    public Direction facing() {
        return this.facing;
    }

    public int gender() {
        return this.usedPokemon.gender();
    }

    public BaseStats getBaseStats() {
        return this.usedPokemon.getBaseStats();
    }

    public double getBelly() {
        return this.belly;
    }

    public int getBellySize() {
        return this.bellySize;
    }

    public int getHp() {
        return this.hp;
    }

    /** @return The percentage of HP left for this Pokemon (0-1). */
    public double getHpPercentage() {
        return this.getHp() * 1d / this.getMaxHP();
    }

    public ItemStack getItem() {
        return this.usedPokemon.getItem();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.usedPokemon.getItem(index);
    }

    public int getMaxHP() {
        return this.getBaseStats().getHealth();
    }

    public Message getNickname() {
        return this.usedPokemon.getNickname();
    }

    /**
     * @return The instance of the input Status Condition, if this Pokemon is affected by it; <code>null</code> else.
     */
    public AppliedStatusCondition getStatusCondition(StatusCondition condition) {
        for (AppliedStatusCondition c : this.statusConditions)
            if (c.condition == condition)
                return c;
        return null;
    }

    public boolean hasItem() {
        return this.usedPokemon.hasItem();
    }

    /**
     * @return True if this Pokemon is affected by the input Status Condition. If input condition is null, checks if it
     *         has any Status Condition.
     */
    public boolean hasStatusCondition(StatusCondition condition) {
        if (condition == null)
            return !this.statusConditions.isEmpty();
        for (AppliedStatusCondition c : this.statusConditions)
            if (c.condition == condition)
                return true;
        return false;
    }

    public long id() {
        return this.originalPokemon.id();
    }

    public void increaseBelly(double quantity) {
        this.belly += quantity;
        this.belly = this.belly < 0 ? 0 : this.belly > this.getBellySize() ? this.getBellySize() : this.belly;
    }

    public void increaseBellySize(int quantity) {
        this.bellySize += quantity;
        this.bellySize = this.bellySize < 0 ? 0 : this.bellySize > 200 ? 200 : this.bellySize;
        this.increaseBelly(quantity);
    }

    public void increaseIQ(int iq) {
        this.usedPokemon.increaseIQ(iq);
    }

    public void inflictStatusCondition(AppliedStatusCondition condition) {
        this.statusConditions.add(condition);
    }

    public boolean isAlliedWith(DungeonPokemon pokemon) {
        if (this == pokemon)
            return true;
        if (!this.type.isAlliedWith(pokemon.type))
            return false;
        return this.player() == pokemon.player() || this.type == DungeonPokemonType.RESCUEABLE
                || pokemon.type == DungeonPokemonType.RESCUEABLE;
    }

    public boolean isBellyFull() {
        return this.getBelly() == this.getBellySize();
    }

    /**
     * @return True if this Pokemon is the original Pokemon that visits this Dungeon. Only false for Dungeons that
     *         modify the visiting Pokemon, such as Dungeons resetting the level to 1.
     */
    public boolean isCopy() {
        return this.usedPokemon != this.originalPokemon;
    }

    /** @return <code>true</code> if this Pokemon is a foe to Players. */
    public boolean isEnemy() {
        return !this.type.isAlliedWith(DungeonPokemonType.TEAM_MEMBER);
    }

    public boolean isFainted() {
        return this.getHp() <= 0;
    }

    public boolean isFamished() {
        return this.getBelly() == 0;
    }

    public boolean isStruggling() {
        for (int i = 0; i < this.moveCount(); ++i)
            if (this.move(i).pp() > 0)
                return false;
        return true;
    }

    public boolean isTeamLeader() {
        return this.player() != null && this.player().getTeamLeader() == this.originalPokemon;
    }

    @Override
    public ArrayList<ItemAction> legalItemActions(boolean inDungeon) {
        return this.usedPokemon.legalItemActions(inDungeon);
    }

    public int level() {
        return this.usedPokemon.level();
    }

    public LearnedMove move(int slot) {
        return this.usedPokemon.move(slot);
    }

    public int moveCount() {
        return this.usedPokemon.moveCount();
    }

    public int moveIndex(LearnedMove move) {
        for (int m = 0; m < this.moveCount(); ++m)
            if (this.move(m) == move)
                return m;
        return -1;
    }

    /** Called when this Pokemon enters a new Floor or when it spawns. */
    public void onFloorStart(Floor floor, ArrayList<Event> events) {
        this.statusConditions.clear();
        this.stats.onFloorStart(floor, events);
        this.ability().onFloorStart(floor, this, events);
        this.regenCounter = 0;
        floor.aiManager.getAI(this).visibility.onPokemonMoved();
        // events.add(new StatusConditionCreatedEvent(floor, StatusConditions.Paralyzed.create(this, floor,
        // floor.random)));
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        EventListener.super.onPostEvent(floor, event, concerned, resultingEvents);

        if (this.hasItem())
            this.getItem().item().effect().onPostEvent(floor, event, concerned, resultingEvents, this.getItem(), this,
                    0);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        EventListener.super.onPreEvent(floor, event, concerned, resultingEvents);

        if (this.hasItem())
            this.getItem().item().effect().onPreEvent(floor, event, concerned, resultingEvents, this.getItem(), this,
                    0);
    }

    /** Called at the beginning of each turn. */
    public void onTurnStart(Floor floor, ArrayList<Event> events) {
        if (this.canRegen()) {
            int recoveryRate = 200;
            int healthGain = 0;

            this.regenCounter += this.regenCounter + this.getBaseStats().health;
            healthGain += this.getBaseStats().health / recoveryRate;
            if (this.regenCounter >= recoveryRate) {
                healthGain += 1;
                this.regenCounter %= recoveryRate;
            }
            this.setHP(this.getHp() + healthGain);
        }

        if (this.stats.speedBuffs() > 0 || this.stats.speedDebuffs() > 0)
            this.stats.onTurnStart(floor, events);

        for (AppliedStatusCondition condition : this.statusConditions)
            if (condition.actedWhileApplied())
                condition.tick(floor, events);

        this.ability().onTurnStart(floor, this, events);
    }

    public Player player() {
        return this.usedPokemon.player();
    }

    public void receiveMove(byte attackType) {
        if (attackType > this.attacksReceived)
            this.attacksReceived = attackType;
    }

    /** @return <code>true</code> if the Pokemon indeed had the condition. */
    public boolean removeStatusCondition(AppliedStatusCondition condition) {
        return this.statusConditions.remove(condition);
    }

    public void removeStatusCondition(StatusCondition condition) {
        this.statusConditions.removeIf(t -> t.condition == condition);
    }

    public void revive() {
        this.setHP(this.getMaxHP());
        for (int m = 0; m < this.moveCount(); ++m)
            this.move(m).setPP(this.move(m).maxPP());
        this.stats.resetSpeed();
        this.stats.resetStages();
        this.statusConditions.clear();
        this.belly = this.getBellySize();
    }

    /** Changes the direction this Pokemon is facing. */
    public void setFacing(Direction direction) {
        this.facing = direction;
    }

    public void setHP(int hp) {
        this.hp = hp;
        if (this.hp < 0)
            this.hp = 0;
        if (this.hp > this.getBaseStats().health)
            this.hp = this.getBaseStats().health;
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.usedPokemon.setItem(index, item);
        if (this.isCopy())
            this.originalPokemon.setItem(index, item);
    }

    public void setItem(ItemStack item) {
        this.usedPokemon.setItem(item);
        this.originalPokemon.setItem(item);
    }

    public void setMove(int slot, LearnedMove move) {
        this.usedPokemon.setMove(slot, move);
    }

    public void setNickname(String nickname) {
        this.usedPokemon.setNickname(nickname);
        this.originalPokemon.setNickname(nickname);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public int size() {
        return this.usedPokemon.size();
    }

    public PokemonSpecies species() {
        return this.usedPokemon.species();
    }

    public void switchMoves(int slot1, int slot2) {
        this.usedPokemon.switchMoves(slot1, slot2);
    }

    public Tile tile() {
        return this.tile;
    }

    @Override
    public String toString() {
        return this.usedPokemon.toString();
    }

    /** Called when this Pokemon tries to move in the input direction. */
    public boolean tryMoveTo(Direction direction, boolean allowSwitching) {
        boolean success = false;
        if (this.tile != null) {
            Tile t = this.tile.adjacentTile(direction);
            if (t.canMoveTo(this, direction, allowSwitching))
                success = true;
        }
        this.setFacing(direction);
        return success;
    }

}
