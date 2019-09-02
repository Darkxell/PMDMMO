package com.darkxell.client.state.dialog.friendarea;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class CheckFriendMenuOption extends MenuOption {

    public static Message createOption(PokemonSpecies species) {

        Message name = species.speciesName();
        int id = species.id;
        String number = " " + id + "  ";
        if (id < 100) {
            number = " " + number;
            if (id < 10)
                number = " " + number;
        }

        name.addPrefix(number);

        if (hasRecruited(species)) {
            name.addPrefix("<green>");
            name.addSuffix("</color>");
        } else if (Persistence.player.friendAreas.contains(species.friendArea())) {
            name.addPrefix("<green>");
            name.addSuffix("</color>");
        }

        return name;
    }

    private static boolean hasRecruited(PokemonSpecies species) {
        for (Pokemon pokemon : Persistence.player.pokemonInZones.values())
            if (pokemon.species() == species)
                return true;
        for (Pokemon pokemon : Persistence.player.getTeam())
            if (pokemon.species() == species)
                return true;
        return false;
    }

    public final PokemonSpecies species;

    public CheckFriendMenuOption(PokemonSpecies species) {
        super(createOption(species));
        this.species = species;
    }

}
