package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.entities.FriendPokemonEntity;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaFreezone extends FreezoneMap {

    public final FriendArea friendArea;

    public FriendAreaFreezone(int defaultX, int defaultY, FreezoneInfo info) {
        super("/freezones/friend/" + info.id + ".xml", defaultX, defaultY, info);

        this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 0, this.mapWidth, 1)));
        this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 0, 1, this.mapHeight)));
        this.triggerzones.add(
                new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, this.mapHeight, this.mapWidth, 1)));
        this.triggerzones.add(
                new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(this.mapWidth, 0, 1, this.mapHeight)));

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
