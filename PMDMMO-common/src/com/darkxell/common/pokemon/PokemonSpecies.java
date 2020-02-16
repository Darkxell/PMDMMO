package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.model.pokemon.BaseStatsModel;
import com.darkxell.common.model.pokemon.EvolutionModel;
import com.darkxell.common.model.pokemon.LearnsetModel;
import com.darkxell.common.model.pokemon.PokemonSpeciesModel;
import com.darkxell.common.move.Move;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendArea;

public class PokemonSpecies implements Registrable<PokemonSpecies> {
    public static final double SHINY_CHANCE = 1d / 100;
    // public static final double SHINY_CHANCE = .5; // Used to test shiny Pokemon

    private final PokemonSpeciesModel model;

    public PokemonSpecies(PokemonSpeciesModel model) {
        this.model = model;
    }

    public BaseStatsModel baseStatsIncreaseAtLevel(int level) {
        ArrayList<BaseStatsModel> stats = this.getBaseStats();
        for (BaseStatsModel s : stats) {
            if (s.getLevel() == level)
                return s;
        }
        return new BaseStatsModel(level, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public int compareTo(PokemonSpecies o) {
        return Integer.compare(this.getID(), o.getID());
    }

    /** @return The amount of experience needed to level up from the input level. */
    public int experienceToNextLevel(int level) {
        return this.getExperiencePerLevel()[level - 1];
    }

    /** @return This form's name. */
    public Message formName() {
        return new Message("pokemon." + this.getID());
    }

    public final FriendArea friendArea() {
        return FriendArea.find(this.getFriendAreaID());
    }

    public Pokemon generate(int level) {
        return this.generate(new Random(), level);
    }

    /**
     * Generates a Pokemon of this species.
     *
     * @param level - The level of the Pokemon to generate.
     */
    public Pokemon generate(Random random, int level) {
        return this.generate(random, level, SHINY_CHANCE);
    }

    /**
     * Generates a Pokemon of this species.
     *
     * @param level       - The level of the Pokemon to generate.
     * @param shinyChance - The chance for the generated Pokemon to be a shiny (0 to 1).
     */
    public Pokemon generate(Random random, int level, double shinyChance) {
        ArrayList<Integer> moves = new ArrayList<>();

        int m1 = this.latestMove(level, moves);
        moves.add(m1);
        int m2 = this.latestMove(level, moves);
        moves.add(m2);
        int m3 = this.latestMove(level, moves);
        moves.add(m3);
        int m4 = this.latestMove(level, moves);

        LearnedMove move1 = m1 == -1 ? null : new LearnedMove(m1);
        LearnedMove move2 = m2 == -1 ? null : new LearnedMove(m2);
        LearnedMove move3 = m3 == -1 ? null : new LearnedMove(m3);
        LearnedMove move4 = m4 == -1 ? null : new LearnedMove(m4);

        return new Pokemon(0, this, null, null, this.statsForLevel(level), this.randomAbility(random), 0, level, move1,
                move2, move3, move4, this.randomGender(random), 0, random.nextDouble() <= shinyChance);
    }

    public ArrayList<Integer> getAbilities() {
        return new ArrayList<>(this.model.getAbilities());
    }

    public ArrayList<BaseStatsModel> getBaseStats() {
        ArrayList<BaseStatsModel> s = new ArrayList<>();
        this.model.getBaseStats().forEach(stat -> s.add(stat.copy()));
        return s;
    }

    public int getBaseXP() {
        return this.model.getBaseXP();
    }

    public ArrayList<EvolutionModel> getEvolutions() {
        ArrayList<EvolutionModel> e = new ArrayList<>();
        this.model.getEvolutions().forEach(ev -> e.add(ev.copy()));
        return e;
    }

    public Integer[] getExperiencePerLevel() {
        return this.model.getExperiencePerLevel().clone();
    }

    public int getFormOf() {
        return this.model.getFormOf();
    }

    public String getFriendAreaID() {
        return this.model.getFriendAreaID();
    }

    public float getHeight() {
        return this.model.getHeight();
    }

    public int getID() {
        return this.model.getID();
    }

    public ArrayList<LearnsetModel> getLearnset() {
        ArrayList<LearnsetModel> l = new ArrayList<>();
        this.model.getLearnset().forEach(set -> l.add(set.copy()));
        return l;
    }

    public LearnsetModel getLearnsetAtLevel(int level) {
        for (LearnsetModel l : this.getLearnset()) {
            if (l.getLevel() == level)
                return l;
        }
        return null;
    }

    public Mobility getMobility() {
        return this.model.getMobility();
    }

    public PokemonSpeciesModel getModel() {
        return this.model;
    }

    public float getRecruitChance() {
        return this.model.getRecruitChance();
    }

    public RecruitLimitation getRecruitLimitation() {
        return this.model.getRecruitLimitation();
    }

    public ArrayList<Integer> getTms() {
        return new ArrayList<>(this.model.getTms());
    }

    public PokemonType getType1() {
        return this.model.getType1();
    }

    public PokemonType getType2() {
        return this.model.getType2();
    }

    public float getWeight() {
        return this.model.getWeight();
    }

    /** @return True if one of this Pokemon's type equals the input type. */
    public boolean isType(PokemonType type) {
        return this.getType1() == type || this.getType2() == type;
    }

    /**
     * @param  level        - The level of the Pokemon.
     * @param  learnedMoves - Moves to exclude because they're already learned.
     * @return              The latest learned move's ID.
     */
    public int latestMove(int level, ArrayList<Integer> learnedMoves) {
        while (level > 0) {
            LearnsetModel learnset = this.getLearnsetAtLevel(level);
            if (learnset != null) {
                ArrayList<Integer> moves = learnset.getLearnedMoves();
                moves.removeAll(learnedMoves);
                if (moves.size() != 0 && Registries.moves().find(moves.get(0)) != null)
                    return moves.get(0);
            }
            --level;
        }
        return -1;
    }

    /** @return The list of learned moves at the input level. */
    public ArrayList<Move> learnedMoves(int level) {
        ArrayList<Move> moves = new ArrayList<>();
        LearnsetModel learnset = this.getLearnsetAtLevel(level);
        if (learnset != null)
            for (Integer id : learnset.getLearnedMoves())
                moves.add(Registries.moves().find(id));
        return moves;
    }

    public PokemonSpecies parent() {
        if (this.getFormOf() == -1)
            return this;
        PokemonSpecies parent = Registries.species().parentSpecies(this);
        return parent == null ? this : parent;
    }

    /** @return A random ability for this Pokemon. */
    public int randomAbility(Random random) {
        if (this.getAbilities().size() == 0)
            return 0;
        return this.getAbilities().get(random.nextInt(this.getAbilities().size()));
    }

    /** @return A random gender for this Pokemon. */
    public byte randomGender(Random random) {
        // todo: include gender probability.
        return (byte) random.nextInt(3);
    }

    /** @return This species' name. */
    public Message speciesName() {
        if (this.getFormOf() == -1) {
            if (Localization.containsKey("pokemon.species." + this.getID()))
                return new Message("pokemon.species." + this.getID());
            return this.formName();
        } else
            return this.parent().speciesName();
    }

    /** @return Regular stats for a Pokemon at the input level. */
    public PokemonBaseStats statsForLevel(int level) {
        ArrayList<BaseStatsModel> baseStats = this.getBaseStats();
        PokemonBaseStats stats = new PokemonBaseStats(0, 0, 0, 0, 0, 0);
        baseStats.removeIf(s -> s.getLevel() > level);
        baseStats.forEach(s -> stats.add(s));
        return stats;
    }

    @Override
    public String toString() {
        return this.formName().toString();
    }

}
