package com.darkxell.common.model.pokemon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.RecruitLimitation;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonSpeciesModel {

    /** ID of the species. */
    private int id;

    /** Identifier for this Form. */
    private int formID;

    /** This Pokemon's type. */
    private PokemonType type1;

    /** This Pokemon's second type. May be null. */
    private PokemonType type2;

    /** Base experience gained when this Pokemon is defeated. */
    private int baseXP;

    /** Pokemon's height. */
    private float height;

    /** Pokemon's weight. */
    private float weight;

    /** The base recruit change for this species (in percent). */
    private float recruitChance;

    /** Limitations on how this Pokemon can be recruited */
    private RecruitLimitation recruitLimitation;

    /** Mobility of this Species. */
    private Mobility mobility;

    /** This species' stat gains at each level. First in this Array is the stats at level 1. */
    private ArrayList<BaseStatsModel> baseStats;

    /** List of possible Abilities for this Pokemon. */
    private ArrayList<Integer> abilities;

    /** Lists the required experience to level up for each level. */
    private int[] experiencePerLevel;

    /** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
    private ArrayList<LearnsetModel> learnset;

    /** List of TMs that can be taught. */
    private ArrayList<Integer> tms;

    /** List of species this Pokemon can evolve into. */
    private ArrayList<Evolution> evolutions;

    /** ID of the Friend Area this Pokemon lives in. */
    private String friendAreaID;
    
    /** Alternate forms of this species. */
    private ArrayList<PokemonSpeciesModel> forms;

    public PokemonSpeciesModel() {
    }

    public PokemonSpeciesModel(int id, int formID, PokemonType type1, PokemonType type2, int baseXP,
            ArrayList<BaseStatsModel> baseStats, float height, float weight, float recruitChance,
            RecruitLimitation recruitLimitation, Mobility mobility, ArrayList<Integer> abilities,
            int[] experiencePerLevel, ArrayList<LearnsetModel> learnset, ArrayList<Integer> tms,
            ArrayList<Evolution> evolutions, ArrayList<PokemonSpeciesModel> forms, String friendAreaID) {
        this.id = id;
        this.formID = formID;
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
        this.forms = forms;
        this.friendAreaID = friendAreaID;
    }

    public ArrayList<Integer> getAbilities() {
        return abilities;
    }

    public ArrayList<BaseStatsModel> getBaseStats() {
        return baseStats;
    }

    public int getBaseXP() {
        return baseXP;
    }

    public ArrayList<Evolution> getEvolutions() {
        return evolutions;
    }

    public int[] getExperiencePerLevel() {
        return experiencePerLevel;
    }

    public int getFormID() {
        return formID;
    }

    public ArrayList<PokemonSpeciesModel> getForms() {
        return forms;
    }

    public String getFriendAreaID() {
        return friendAreaID;
    }

    public float getHeight() {
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

    public float getRecruitChance() {
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

    public float getWeight() {
        return weight;
    }

    public void setAbilities(ArrayList<Integer> abilities) {
        this.abilities = abilities;
    }

    public void setBaseStats(ArrayList<BaseStatsModel> baseStats) {
        this.baseStats = baseStats;
    }

    public void setBaseXP(int baseXP) {
        this.baseXP = baseXP;
    }

    public void setEvolutions(ArrayList<Evolution> evolutions) {
        this.evolutions = evolutions;
    }

    public void setExperiencePerLevel(int[] experiencePerLevel) {
        this.experiencePerLevel = experiencePerLevel;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public void setForms(ArrayList<PokemonSpeciesModel> forms) {
        this.forms = forms;
    }

    public void setFriendAreaID(String friendAreaID) {
        this.friendAreaID = friendAreaID;
    }

    public void setHeight(float height) {
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

    public void setRecruitChance(float recruitChance) {
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

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
