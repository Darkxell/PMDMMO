package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.model.pokemon.BaseStatsModel;
import com.darkxell.common.model.pokemon.EvolutionModel;
import com.darkxell.common.model.pokemon.LearnsetModel;
import com.darkxell.common.model.pokemon.PokemonSpeciesModel;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.Move;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendArea;

public class PokemonSpecies implements Registrable<PokemonSpecies> {
    public static final double SHINY_CHANCE = 1d / 100;
    // public static final double SHINY_CHANCE = .5; // Used to test shiny Pokemon

    /** The list of different forms Pokemon of this species can have. */
    private final ArrayList<PokemonSpecies> forms;

    private final PokemonSpeciesModel model;

    /** Constructor for XML */
    public PokemonSpecies(Element xml) {
        this.model = new PokemonSpeciesModel();
        this.model.setID(Integer.parseInt(xml.getAttributeValue("id")));
        this.model.setFormOf(XMLUtils.getAttribute(xml, "form-id", -1));
        this.model.setType1(PokemonType.valueOf(xml.getAttributeValue("type1")));
        this.model.setType2(
                xml.getAttribute("type2") == null ? null : PokemonType.valueOf(xml.getAttributeValue("type2")));
        this.model.setBaseXP(Integer.parseInt(xml.getAttributeValue("base-xp")));
        this.model.setHeight(Float.parseFloat(xml.getAttributeValue("height")));
        this.model.setWeight(Float.parseFloat(xml.getAttributeValue("weight")));
        this.model.setRecruitChance(XMLUtils.getAttribute(xml, "recruit", -999f));
        this.model.setRecruitLimitation(RecruitLimitation.valueOf(XMLUtils.getAttribute(xml, "recruitlimi", "NONE")));
        this.model.setAbilities(XMLUtils.readIntArrayAsList(xml.getChild("abilities", xml.getNamespace())));
        this.model.setMobility(xml.getAttribute("mobility") == null ? Mobility.defaultMobility(this.model)
                : Mobility.valueOf(xml.getAttributeValue("mobility")));
        this.model.setBaseStats(new ArrayList<>());
        this.model.setLearnset(new ArrayList<>());
        this.model.setTms(XMLUtils.readIntArrayAsList(xml.getChild("tms", xml.getNamespace())));
        this.model.setEvolutions(new ArrayList<>());

        if (xml.getChild("statline", xml.getNamespace()) != null) {
            int[][] statline = XMLUtils.readIntMatrix(xml.getChild("statline", xml.getNamespace()));
            int level = 1;
            for (int[] stat : statline)
                this.model.getBaseStats().add(new BaseStatsModel(level++, stat));
        }

        if (xml.getChild("experience", xml.getNamespace()) != null) {
            String[] lvls = xml.getChildText("experience", xml.getNamespace()).split(",");
            Integer[] experiencePerLevel = new Integer[lvls.length];
            for (int lvl = 0; lvl < lvls.length; lvl++)
                experiencePerLevel[lvl] = Integer.parseInt(lvls[lvl]);
            this.model.setExperiencePerLevel(experiencePerLevel);
        } else
            this.model.setExperiencePerLevel(new Integer[0]);

        if (xml.getChild("evolves", xml.getNamespace()) != null)
            for (Element e : xml.getChild("evolves", xml.getNamespace()).getChildren())
                this.model.getEvolutions().add(new EvolutionModel(e));

        if (xml.getChild("tms", xml.getNamespace()) != null)
            for (Element tm : xml.getChild("tms", xml.getNamespace()).getChildren())
                this.model.getTms().add(Integer.parseInt(tm.getAttributeValue("id")));

        if (xml.getChild("learnset", xml.getNamespace()) != null)
            for (Element level : xml.getChild("learnset", xml.getNamespace()).getChildren())
                this.model.getLearnset().add(new LearnsetModel(Integer.parseInt(level.getAttributeValue("l")),
                        XMLUtils.readIntArrayAsList(level)));

        this.model.setFriendAreaID(xml.getAttributeValue("area"));

        this.forms = new ArrayList<>();
        for (Element form : xml.getChildren("form", xml.getNamespace())) {
            PokemonSpecies f = createForm(form);
            this.forms.add(f);
        }
    }

    public PokemonSpecies(int id, int formID, PokemonType type1, PokemonType type2, int baseXP,
            ArrayList<BaseStatsModel> baseStats, float height, float weight, float recruitChance,
            RecruitLimitation recruitLimitation, Mobility mobility, ArrayList<Integer> abilities,
            Integer[] experiencePerLevel, ArrayList<LearnsetModel> learnset, ArrayList<Integer> tms,
            ArrayList<EvolutionModel> evolutions, ArrayList<PokemonSpecies> forms, String friendAreaID) {
        this.model = new PokemonSpeciesModel(id, formID, type1, type2, baseXP, baseStats, height, weight, recruitChance,
                recruitLimitation, mobility, abilities, experiencePerLevel, learnset, tms, evolutions, friendAreaID);
        this.forms = forms;
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

    private PokemonSpecies createForm(Element xml) {
        int formID = XMLUtils.getAttribute(xml, "form", 0);
        int id = XMLUtils.getAttribute(xml, "id", 0);

        PokemonType type1 = xml.getAttribute("type1") == null ? this.getType1()
                : PokemonType.valueOf(xml.getAttributeValue("type1"));
        PokemonType type2 = xml.getAttribute("type2") == null ? this.getType2()
                : xml.getAttributeValue("type2").equals("null") ? null
                        : PokemonType.valueOf(xml.getAttributeValue("type2"));
        int baseXP = XMLUtils.getAttribute(xml, "base-xp", this.getBaseXP());
        float height = XMLUtils.getAttribute(xml, "height", this.getHeight());
        float weight = XMLUtils.getAttribute(xml, "weight", this.getWeight());
        float recruitChance = XMLUtils.getAttribute(xml, "recruit", this.getRecruitChance());
        RecruitLimitation recruitLimitation = RecruitLimitation
                .valueOf(XMLUtils.getAttribute(xml, "recruitlimit", "NONE"));
        ArrayList<Integer> abilities = xml.getChild("abilities", xml.getNamespace()) == null ? this.getAbilities()
                : XMLUtils.readIntArrayAsList(xml.getChild("abilities", xml.getNamespace()));
        ArrayList<BaseStatsModel> baseStats = new ArrayList<>();
        ArrayList<LearnsetModel> learnset = new ArrayList<>();
        ArrayList<Integer> tms = xml.getChild("tms", xml.getNamespace()) == null ? this.getTms()
                : XMLUtils.readIntArrayAsList(xml.getChild("tms", xml.getNamespace()));
        ArrayList<EvolutionModel> evolutions = new ArrayList<>();

        if (xml.getChild("statline", xml.getNamespace()) == null)
            baseStats = this.getBaseStats();
        else {
            int[][] statline = XMLUtils.readIntMatrix(xml.getChild("statline", xml.getNamespace()));
            int level = 1;
            for (int[] stat : statline)
                baseStats.add(new BaseStatsModel(level++, stat));
        }

        Integer[] experiencePerLevel;
        if (xml.getChild("experience", xml.getNamespace()) == null)
            experiencePerLevel = this.getExperiencePerLevel();
        else {
            String[] lvls = xml.getChildText("experience", xml.getNamespace()).split(",");
            experiencePerLevel = new Integer[lvls.length];
            for (int lvl = 0; lvl < lvls.length; lvl++)
                experiencePerLevel[lvl] = Integer.parseInt(lvls[lvl]);
        }

        if (xml.getChild("evolves", xml.getNamespace()) == null)
            evolutions = this.getEvolutions();
        else
            for (Element e : xml.getChild("evolves", xml.getNamespace()).getChildren())
                evolutions.add(new EvolutionModel(e));

        if (xml.getChild("tms", xml.getNamespace()) == null)
            tms = this.getTms();
        else
            for (Element tm : xml.getChild("tms", xml.getNamespace()).getChildren())
                tms.add(Integer.parseInt(tm.getAttributeValue("id")));

        if (xml.getChild("learnset", xml.getNamespace()) == null)
            learnset = this.getLearnset();
        else
            for (Element level : xml.getChild("learnset", xml.getNamespace()).getChildren())
                learnset.add(new LearnsetModel(Integer.parseInt(level.getAttributeValue("l")),
                        XMLUtils.readIntArrayAsList(level)));

        String friendArea = XMLUtils.getAttribute(xml, "area", this.getFriendAreaID());

        PokemonSpeciesModel m = new PokemonSpeciesModel(id, formID, type1, type2, baseXP, baseStats, height, weight,
                recruitChance, recruitLimitation, null, abilities, experiencePerLevel, learnset, tms, evolutions,
                friendArea);
        Mobility mobility = xml.getAttribute("mobility") == null
                ? this.getMobility() == Mobility.defaultMobility(this.model) ? Mobility.defaultMobility(m)
                        : this.getMobility()
                : Mobility.valueOf(xml.getAttributeValue("mobility")); // Must be at the end to use Abilities

        return new PokemonSpecies(id, formID, type1, type2, baseXP, baseStats, height, weight, recruitChance,
                recruitLimitation, mobility, abilities, experiencePerLevel, learnset, tms, evolutions, forms,
                friendArea);
    }

    /** @return The amount of experience needed to level up from the input level. */
    public int experienceToNextLevel(int level) {
        return this.getExperiencePerLevel()[level - 1];
    }

    /** @return This form's name. */
    public Message formName() {
        return new Message("pokemon." + this.getID());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PokemonSpecies> forms() {
        return (ArrayList<PokemonSpecies>) this.forms.clone();
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

    public ArrayList<PokemonSpecies> getForms() {
        return this.forms;
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

    public Element toXML() {
        Element root = new Element("pokemon");
        root.setAttribute("id", Integer.toString(this.getID()));
        XMLUtils.setAttribute(root, "form-id", this.getFormOf(), -1);
        root.setAttribute("type1", this.getType1().name());
        if (this.getType2() != null)
            root.setAttribute("type2", this.getType2().name());
        root.setAttribute("base-xp", Integer.toString(this.getBaseXP()));
        root.setAttribute("height", Float.toString(this.getHeight()));
        root.setAttribute("weight", Float.toString(this.getWeight()));
        XMLUtils.setAttribute(root, "recruit", this.getRecruitChance(), -999);
        XMLUtils.setAttribute(root, "recruitlimit", this.getRecruitLimitation().toString(),
                RecruitLimitation.NONE.toString());
        if (this.getMobility() != Mobility.defaultMobility(this.model))
            root.setAttribute("mobility", this.getMobility().name());

        int[][] line = new int[100][];
        for (int lvl = 0; lvl < this.getBaseStats().size(); lvl++) {
            BaseStatsModel stats = this.getBaseStats().get(lvl);
            if (stats.getMoveSpeed() != 1)
                line[lvl] = new int[6];
            else
                line[lvl] = new int[5];

            line[lvl][Stat.Attack.id] = stats.getAttack();
            line[lvl][Stat.Defense.id] = stats.getDefense();
            line[lvl][Stat.Health.id] = stats.getHealth();
            line[lvl][Stat.SpecialAttack.id] = stats.getSpecialAttack();
            line[lvl][Stat.SpecialDefense.id] = stats.getSpecialDefense();
            if (stats.getMoveSpeed() != 0)
                line[lvl][5] = stats.getMoveSpeed();
        }
        root.addContent(XMLUtils.toXML("statline", line));

        String s = "";
        for (int lvl = 0; lvl < this.getExperiencePerLevel().length; lvl++) {
            if (lvl != 0)
                s += ",";
            s += this.getExperiencePerLevel()[lvl];
        }
        root.addContent(new Element("experience").setText(s));

        if (this.getEvolutions().size() != 0) {
            Element evolutions = new Element("evolves");
            for (EvolutionModel evolution : this.getEvolutions())
                evolutions.addContent(evolution.toXML());
            root.addContent(evolutions);
        }

        if (this.getAbilities().size() != 0)
            root.addContent(XMLUtils.toXML("abilities", this.getAbilities()));

        if (this.getTms().size() != 0)
            root.addContent(XMLUtils.toXML("tms", this.getTms()));

        if (this.getLearnset().size() != 0) {
            Element learnset = new Element("learnset");
            for (LearnsetModel l : this.getLearnset()) {
                Element lv = XMLUtils.toXML("level", l.getLearnedMoves());
                lv.setAttribute("l", Integer.toString(l.getLevel()));
                learnset.addContent(lv);
            }
            root.addContent(learnset);
        }

        for (PokemonSpecies form : this.forms)
            this.toXML(root, form);

        root.setAttribute("area", this.getFriendAreaID());

        return root;
    }

    private void toXML(Element root, PokemonSpecies form) {
        Element e = new Element("form");
        e.setAttribute("id", Integer.toString(form.getFormOf()));
        if (this.getType1() != form.getType1())
            e.setAttribute("type1", form.getType1().name());
        if (this.getType2() != form.getType2())
            e.setAttribute("type2", form.getType2() == null ? "null" : form.getType2().name());
        if (this.getBaseXP() != form.getBaseXP())
            e.setAttribute("base-xp", Integer.toString(form.getBaseXP()));
        if (this.getHeight() != form.getHeight())
            e.setAttribute("height", Float.toString(form.getHeight()));
        if (this.getWeight() != form.getWeight())
            e.setAttribute("weight", Float.toString(form.getWeight()));
        if (this.getRecruitChance() != form.getRecruitChance())
            e.setAttribute("recruit", Float.toString(form.getRecruitChance()));
        if (this.getRecruitLimitation() != form.getRecruitLimitation())
            e.setAttribute("recruitlimit", form.getRecruitLimitation().toString());
        if (this.getMobility() != form.getMobility())
            e.setAttribute("mobility", form.getMobility().name());

        if (!this.getBaseStats().equals(form.getBaseStats())) {
            int[][] line = new int[100][];
            for (int lvl = 0; lvl < form.getBaseStats().size(); lvl++) {
                BaseStatsModel stats = form.getBaseStats().get(lvl);
                if (stats.getMoveSpeed() != 1)
                    line[lvl] = new int[6];
                else
                    line[lvl] = new int[5];

                line[lvl][Stat.Attack.id] = stats.getAttack();
                line[lvl][Stat.Defense.id] = stats.getDefense();
                line[lvl][Stat.Health.id] = stats.getHealth();
                line[lvl][Stat.SpecialAttack.id] = stats.getSpecialAttack();
                line[lvl][Stat.SpecialDefense.id] = stats.getSpecialDefense();
                if (stats.getMoveSpeed() != 0)
                    line[lvl][5] = stats.getMoveSpeed();
            }
            e.addContent(XMLUtils.toXML("statline", line));
        }

        {
            boolean flag = false;
            for (int i = 0; i < this.getExperiencePerLevel().length; ++i)
                if (this.getExperiencePerLevel()[i] != form.getExperiencePerLevel()[i]) {
                    flag = true;
                    break;
                }
            if (flag) {
                String s = "";
                for (int lvl = 0; lvl < form.getExperiencePerLevel().length; lvl++) {
                    if (lvl != 0)
                        s += ",";
                    s += form.getExperiencePerLevel()[lvl];
                }
                e.addContent(new Element("experience").setText(s));
            }
        }

        if (!this.getEvolutions().equals(form.getEvolutions())) {
            Element evolutions = new Element("evolves");
            for (EvolutionModel evolution : form.getEvolutions())
                evolutions.addContent(evolution.toXML());
            e.addContent(evolutions);
        }

        if (!this.getAbilities().equals(form.getAbilities()))
            e.addContent(XMLUtils.toXML("abilities", form.getAbilities()));

        if (!this.getTms().equals(form.getTms()))
            e.addContent(XMLUtils.toXML("tms", form.getTms()));

        if (!this.getLearnset().equals(form.getLearnset())) {
            Element learnset = new Element("learnset");
            for (LearnsetModel l : this.getLearnset()) {
                Element lv = XMLUtils.toXML("level", l.getLearnedMoves());
                lv.setAttribute("l", Integer.toString(l.getLevel()));
                learnset.addContent(lv);
            }
            e.addContent(learnset);
        }

        XMLUtils.setAttribute(root, "area", form.getFriendAreaID(), this.getFriendAreaID());
    }

}
