package com.darkxell.client.mechanics.freezone.customzones;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.FreezoneMap;
import com.darkxell.client.mechanics.freezone.entity.FriendPokemonEntity;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.zones.FreezoneInfo;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaFreezone extends FreezoneMap {

    public final FriendArea friendArea;

    public FriendAreaFreezone(String xmlPath, FreezoneInfo info) {
        super(xmlPath, info);
        this.friendArea = FriendArea.find(this.info.id);

        this.createFriendEntities();
    }

    private void createFriendEntities() {
        Player player = Persistence.player;
        for (Pokemon pokemon : player.pokemonInZones.values())
            if (pokemon.species().friendArea() == this.friendArea) {
                this.addEntity(new FriendPokemonEntity(pokemon));
            }
    }

}
