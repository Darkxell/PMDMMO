package com.darkxell.client.mechanics.freezones.zones;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FriendLocationsRegistry;
import com.darkxell.client.mechanics.freezones.entities.FriendPokemonEntity;
import com.darkxell.client.model.friendlocations.FriendLocationModel;
import com.darkxell.client.model.friendlocations.FriendLocationsModel;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;
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

        FriendLocationsModel locations = FriendLocationsRegistry.get(this.friendArea.freezone);

        // List of locations that are not taken yet.
        ArrayList<FriendLocationModel> availableLocations = new ArrayList<>(locations.getLocations());
        // List of Pokemon who couldn't be placed with their default locations.
        ArrayList<Pokemon> noLocation = new ArrayList<>();
        // List of Pokemon to place in this freezone.
        ArrayList<Pokemon> friends = new ArrayList<>();

        for (Pokemon pokemon : player.allies)
            if (pokemon.species().friendArea() == this.friendArea)
                friends.add(pokemon);
        for (Pokemon pokemon : player.pokemonInZones.values())
            if (pokemon.species().friendArea() == this.friendArea)
                friends.add(pokemon);

        for (Pokemon pokemon : friends) {
            FriendLocationModel position = locations.findLocation(pokemon.species().getID(), pokemon.isShiny());
            if (position == null) { // If it has no default location, add it to nolocation list
                Logger.w("Friend " + pokemon + " has no friend area location!");
                noLocation.add(pokemon);
            } else if (!availableLocations.contains(position)) // If location is already taken, add it to nolocation
                                                               // list
                noLocation.add(pokemon);
            else { // Else, place it and remove position from available list
                FriendPokemonEntity p = new FriendPokemonEntity(pokemon);
                this.addEntity(p);
                p.spawnAt(position.getX(), position.getY());
                availableLocations.remove(position);
            }
        }

        // Place Pokemon that couldn't be placed previously
        for (Pokemon pokemon : noLocation) {
            if (availableLocations.isEmpty()) {
                Logger.e("Friend area " + this.friendArea + " has too many friends, or is missing locations!");
                break;
            }

            FriendLocationModel position = availableLocations.remove(0);
            FriendPokemonEntity p = new FriendPokemonEntity(pokemon);
            this.addEntity(p);
            p.spawnAt(position.getX(), position.getY());
        }
    }
}
