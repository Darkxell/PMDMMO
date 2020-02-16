package com.darkxell.common.model.pokemon;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.RecruitLimitation;
import com.darkxell.common.util.XMLUtils.IntegerArrayAdapter;
import com.darkxell.common.util.XMLUtils.IntegerListAdapter;

@XmlRootElement(name = "species")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonSpeciesModel implements Comparable<PokemonSpeciesModel> {

    /** ID of the species. */
    @XmlAttribute
    private int id;

    /** Identifier of the main Pokemon if this is a form. */
    @XmlAttribute
    private Integer formOf;

    /** This Pokemon's type. */
    @XmlAttribute
    private PokemonType type1;

    /** This Pokemon's second type. May be null. */
    @XmlAttribute
    private PokemonType type2;

    /** Base experience gained when this Pokemon is defeated. */
    @XmlAttribute
    private Integer baseXP;

    /** Pokemon's height. */
    @XmlAttribute
    private Float height;

    /** Pokemon's weight. */
    @XmlAttribute
    private Float weight;

    /** The base recruit change for this species (in percent). */
    @XmlAttribute
    private Float recruitChance;

    /** Limitations on how this Pokemon can be recruited */
    @XmlAttribute
    private RecruitLimitation recruitLimitation;

    /** Mobility of this Species. */
    @XmlAttribute
    private Mobility mobility;

    /** ID of the Friend Area this Pokemon lives in. */
    @XmlAttribute
    private String friendAreaID;

    /** This species' stat gains at each level. First in this Array is the stats at level 1. */
    @XmlElement(name = "stats")
    @XmlElementWrapper(name = "basestats")
    private ArrayList<BaseStatsModel> baseStats;

    /** List of possible Abilities for this Pokemon. */
    @XmlElement(name = "ability")
    @XmlJavaTypeAdapter(IntegerListAdapter.class)
    private ArrayList<Integer> abilities;

    /** Lists the required experience to level up for each level. */
    @XmlElement(name = "experience")
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] experiencePerLevel;

    /** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
    @XmlElement(name = "moves")
    @XmlElementWrapper(name = "learnset")
    private ArrayList<LearnsetModel> learnset;

    /** List of TMs that can be taught. */
    @XmlElement
    @XmlJavaTypeAdapter(IntegerListAdapter.class)
    private ArrayList<Integer> tms;

    /** List of species this Pokemon can evolve into. */
    @XmlElement(name = "evolves")
    @XmlElementWrapper(name = "evolutions")
    private ArrayList<EvolutionModel> evolutions = new ArrayList<>();

    public PokemonSpeciesModel() {
    }

    public PokemonSpeciesModel(int id, Integer formOf, PokemonType type1, PokemonType type2, int baseXP,
            ArrayList<BaseStatsModel> baseStats, Float height, Float weight, Float recruitChance,
            RecruitLimitation recruitLimitation, Mobility mobility, ArrayList<Integer> abilities,
            Integer[] experiencePerLevel, ArrayList<LearnsetModel> learnset, ArrayList<Integer> tms,
            ArrayList<EvolutionModel> evolutions, String friendAreaID) {
        this.id = id;
        this.formOf = formOf;
        this.type1 = type1;
        this.type2 = type2;
        this.baseXP = baseXP;
        this.baseStats = baseStats;
        this.height = height;
        this.weight = weight;
        this.recruitChance = recruitChance;
        this.recruitLimitation = recruitLimitation;
        this.mobility = mobility;
        this.abilities = abilities;
        this.experiencePerLevel = experiencePerLevel;
        this.learnset = learnset;
        this.tms = tms;
        this.evolutions = evolutions;
        this.friendAreaID = friendAreaID;
    }

    @Override
    public int compareTo(PokemonSpeciesModel o) {
        return Integer.compare(this.id, o.id);
    }

    public PokemonSpeciesModel copy() {
        ArrayList<BaseStatsModel> baseStats = new ArrayList<>();
        this.getBaseStats().forEach(s -> baseStats.add(s.copy()));

        ArrayList<LearnsetModel> learnset = new ArrayList<>();
        this.getLearnset().forEach(l -> learnset.add(l.copy()));

        ArrayList<EvolutionModel> evolutions = new ArrayList<>();
        this.getEvolutions().forEach(e -> evolutions.add(e.copy()));

        return new PokemonSpeciesModel(id, formOf, type1, type2, baseXP, baseStats, height, weight, recruitChance,
                recruitLimitation, mobility, new ArrayList<>(abilities), experiencePerLevel.clone(), learnset,
                new ArrayList<>(tms), evolutions, friendAreaID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PokemonSpeciesModel))
            return false;
        PokemonSpeciesModel o = (PokemonSpeciesModel) obj;
        return o.id == this.id && o.formOf.equals(this.formOf) && this.type1 == o.type1 && this.type2 == o.type2
                && this.baseXP.equals(o.baseXP) && this.height.equals(o.height) && this.weight.equals(o.weight)
                && this.recruitChance.equals(o.recruitChance) && this.recruitLimitation == o.recruitLimitation
                && this.mobility == o.mobility && this.friendAreaID.equals(o.friendAreaID)
                && this.baseStats.equals(o.baseStats) && this.abilities.equals(o.abilities)
                && Arrays.deepEquals(this.experiencePerLevel, o.experiencePerLevel) && this.learnset.equals(o.learnset)
                && this.tms.equals(o.tms) && this.evolutions.equals(o.evolutions);
    }

    public ArrayList<Integer> getAbilities() {
        return abilities;
    }

    public ArrayList<BaseStatsModel> getBaseStats() {
        return baseStats;
    }

    public Integer getBaseXP() {
        return baseXP;
    }

    public ArrayList<EvolutionModel> getEvolutions() {
        return evolutions;
    }

    public Integer[] getExperiencePerLevel() {
        return experiencePerLevel;
    }

    public Integer getFormOf() {
        return formOf;
    }

    public String getFriendAreaID() {
        return friendAreaID;
    }

    public Float getHeight() {
        return height;
    }

    public int getID() {
        return id;
    }

    public ArrayList<LearnsetModel> getLearnset() {
        return learnset;
    }

    public Mobility getMobility() {
        return mobility;
    }

    public Float getRecruitChance() {
        return recruitChance;
    }

    public RecruitLimitation getRecruitLimitation() {
        return recruitLimitation;
    }

    public ArrayList<Integer> getTms() {
        return tms;
    }

    public PokemonType getType1() {
        return type1;
    }

    public PokemonType getType2() {
        return type2;
    }

    public Float getWeight() {
        return weight;
    }

    public boolean isType(PokemonType type) {
        return this.getType1() == type || this.getType2() == type;
    }

    public void setAbilities(ArrayList<Integer> abilities) {
        this.abilities = abilities;
    }

    public void setBaseStats(ArrayList<BaseStatsModel> baseStats) {
        this.baseStats = baseStats;
    }

    public void setBaseXP(Integer baseXP) {
        this.baseXP = baseXP;
    }

    public void setEvolutions(ArrayList<EvolutionModel> evolutions) {
        this.evolutions = evolutions;
    }

    public void setExperiencePerLevel(Integer[] experiencePerLevel) {
        this.experiencePerLevel = experiencePerLevel;
    }

    public void setFormOf(Integer formOf) {
        this.formOf = formOf;
    }

    public void setFriendAreaID(String friendAreaID) {
        this.friendAreaID = friendAreaID;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setLearnset(ArrayList<LearnsetModel> learnset) {
        this.learnset = learnset;
    }

    public void setMobility(Mobility mobility) {
        this.mobility = mobility;
    }

    public void setRecruitChance(Float recruitChance) {
        this.recruitChance = recruitChance;
    }

    public void setRecruitLimitation(RecruitLimitation recruitLimitation) {
        this.recruitLimitation = recruitLimitation;
    }

    public void setTms(ArrayList<Integer> tms) {
        this.tms = tms;
    }

    public void setType1(PokemonType type1) {
        this.type1 = type1;
    }

    public void setType2(PokemonType type2) {
        this.type2 = type2;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

}
