package com.darkxell.common.model.io;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkxell.common.dungeon.floor.TileType.Mobility;
import com.darkxell.common.model.pokemon.BaseStatsModel;
import com.darkxell.common.model.pokemon.EvolutionModel;
import com.darkxell.common.model.pokemon.PokemonListModel;
import com.darkxell.common.model.pokemon.PokemonSpeciesModel;
import com.darkxell.common.pokemon.RecruitLimitation;

public class PokemonModelIOHandler extends ModelIOHandler<PokemonListModel> {

    public PokemonModelIOHandler() {
        super(PokemonListModel.class);
    }

    @Override
    protected PokemonListModel handleAfterImport(PokemonListModel object) {

        for (PokemonSpeciesModel pokemon : object.pokemon) {
            handlePokemonAfterImport(pokemon);
        }

        return super.handleAfterImport(object);
    }

    @Override
    protected PokemonListModel handleBeforeExport(PokemonListModel object) {

        object = object.copy();

        for (PokemonSpeciesModel pokemon : object.pokemon) {
            handlePokemonBeforeExport(pokemon);
        }

        return super.handleBeforeExport(object);
    }

    private void handleEvolutionAfterImport(EvolutionModel evolution) {
        if (evolution.getMethod() == null)
            evolution.setMethod(EvolutionModel.LEVEL);
        if (evolution.getSpeciesForm() == null)
            evolution.setSpeciesForm(0);
    }

    private void handleEvolutionBeforeExport(EvolutionModel evolution) {
        if (evolution.getMethod().equals(EvolutionModel.LEVEL))
            evolution.setMethod(null);
        if (evolution.getSpeciesForm().equals(0))
            evolution.setSpeciesForm(null);
    }

    private void handleFormAfterImport(PokemonSpeciesModel form, PokemonSpeciesModel parent) {
        if (form.getAbilities() == null)
            form.setAbilities(new ArrayList<>(parent.getAbilities()));
        if (form.getBaseStats() == null) {
            form.setBaseStats(new ArrayList<>());
            parent.getBaseStats().forEach(s -> form.getBaseStats().add(s.copy()));
        }
        if (form.getBaseXP() == null)
            form.setBaseXP(parent.getBaseXP());
        if (form.getEvolutions() == null) {
            form.setEvolutions(new ArrayList<>());
            parent.getEvolutions().forEach(e -> form.getEvolutions().add(e.copy()));
        }
        if (form.getExperiencePerLevel() == null)
            form.setExperiencePerLevel(parent.getExperiencePerLevel().clone());
        form.setForms(new ArrayList<>());
        if (form.getFriendAreaID() == null)
            form.setFriendAreaID(parent.getFriendAreaID());
        if (form.getHeight() == null)
            form.setHeight(parent.getHeight());
        if (form.getLearnset() == null) {
            form.setLearnset(new ArrayList<>());
            parent.getLearnset().forEach(l -> form.getLearnset().add(l.copy()));
        }
        if (form.getMobility() == null)
            form.setMobility(parent.getMobility());
        if (form.getRecruitChance() == null)
            form.setRecruitChance(parent.getRecruitChance());
        if (form.getRecruitLimitation() == null)
            form.setRecruitLimitation(parent.getRecruitLimitation());
        if (form.getTms() == null)
            form.setTms(new ArrayList<>(parent.getTms()));
        if (form.getType1() == null)
            form.setType1(parent.getType1());
        if (form.getType2() == null)
            form.setType2(parent.getType2());
        if (form.getWeight() == null)
            form.setWeight(parent.getWeight());
    }

    private void handleFormBeforeExport(PokemonSpeciesModel form, PokemonSpeciesModel parent) {
        if (form.getAbilities().equals(parent.getAbilities()))
            form.setAbilities(null);
        if (form.getBaseStats().equals(parent.getBaseStats()))
            form.setBaseStats(null);
        if (form.getBaseXP().equals(parent.getBaseXP()))
            form.setBaseXP(null);
        if (form.getEvolutions().equals(parent.getEvolutions()))
            form.setEvolutions(null);
        if (Arrays.deepEquals(form.getExperiencePerLevel(), parent.getExperiencePerLevel()))
            form.setExperiencePerLevel(null);
        form.setForms(null);
        if (form.getFriendAreaID().equals(parent.getFriendAreaID()))
            form.setFriendAreaID(null);
        if (form.getHeight().equals(parent.getHeight()))
            form.setHeight(null);
        if (form.getLearnset().equals(parent.getLearnset()))
            form.setLearnset(null);
        if (form.getMobility().equals(parent.getMobility()))
            form.setMobility(null);
        if (form.getRecruitChance().equals(parent.getRecruitChance()))
            form.setRecruitChance(null);
        if (form.getRecruitLimitation().equals(parent.getRecruitLimitation()))
            form.setRecruitLimitation(null);
        if (form.getTms().equals(parent.getTms()))
            form.setTms(null);
        if (form.getType1().equals(parent.getType1()))
            form.setType1(null);
        if (form.getType2() == parent.getType2())
            form.setType2(null);
        if (form.getWeight().equals(parent.getWeight()))
            form.setWeight(null);
    }

    private void handlePokemonAfterImport(PokemonSpeciesModel pokemon) {
        if (pokemon.getFormID() == null)
            pokemon.setFormID(0);
        if (pokemon.getRecruitLimitation() == null)
            pokemon.setRecruitLimitation(RecruitLimitation.NONE);
        if (pokemon.getMobility() == null)
            pokemon.setMobility(Mobility.defaultMobility(pokemon));

        for (BaseStatsModel stat : pokemon.getBaseStats())
            handleStatsAfterImport(stat);
        for (EvolutionModel evolution : pokemon.getEvolutions())
            handleEvolutionAfterImport(evolution);
        if (pokemon.getTms() == null)
            pokemon.setTms(new ArrayList<>());
        for (PokemonSpeciesModel form : pokemon.getForms())
            handleFormAfterImport(form, pokemon);
    }

    private void handlePokemonBeforeExport(PokemonSpeciesModel pokemon) {
        for (PokemonSpeciesModel form : pokemon.getForms())
            handleFormBeforeExport(form, pokemon); // Handle form before setting defaults to null in parent

        if (pokemon.getFormID().equals(0))
            pokemon.setFormID(null);
        if (pokemon.getRecruitLimitation() == RecruitLimitation.NONE)
            pokemon.setRecruitLimitation(null);
        if (pokemon.getMobility() == Mobility.defaultMobility(pokemon))
            pokemon.setMobility(null);

        for (BaseStatsModel stat : pokemon.getBaseStats())
            handleStatsBeforeExport(stat);
        for (EvolutionModel evolution : pokemon.getEvolutions())
            handleEvolutionBeforeExport(evolution);
    }

    private void handleStatsAfterImport(BaseStatsModel stat) {
        if (stat.getAttack() == null)
            stat.setAttack(0);
        if (stat.getDefense() == null)
            stat.setDefense(0);
        if (stat.getHealth() == null)
            stat.setHealth(0);
        if (stat.getSpecialAttack() == null)
            stat.setSpecialAttack(0);
        if (stat.getSpecialDefense() == null)
            stat.setSpecialDefense(0);
        if (stat.getMoveSpeed() == null)
            stat.setMoveSpeed(0);
    }

    private void handleStatsBeforeExport(BaseStatsModel stat) {
        if (stat.getAttack().equals(0))
            stat.setAttack(null);
        if (stat.getDefense().equals(0))
            stat.setDefense(null);
        if (stat.getHealth().equals(0))
            stat.setHealth(null);
        if (stat.getSpecialAttack().equals(0))
            stat.setSpecialAttack(null);
        if (stat.getSpecialDefense().equals(0))
            stat.setSpecialDefense(null);
        if (stat.getMoveSpeed().equals(0))
            stat.setMoveSpeed(null);
    }

}
