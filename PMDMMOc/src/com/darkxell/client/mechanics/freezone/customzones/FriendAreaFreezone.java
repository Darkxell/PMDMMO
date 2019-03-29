package com.darkxell.client.mechanics.freezone.customzones;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.FreezoneMap;
import com.darkxell.client.mechanics.freezone.entity.FriendPokemonEntity;
import com.darkxell.common.Registries;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.Position;
import com.darkxell.common.util.xml.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaFreezone extends FreezoneMap {

    public final FriendArea friendArea;
    private HashMap<Pair<PokemonSpecies, Boolean>, Position> friendLocations;

    public FriendAreaFreezone(String xmlPath, FreezoneInfo info) {
        super(xmlPath, info);
        this.friendArea = FriendArea.find(this.info.id);

        this.createFriendEntities();
    }

    private void createFriendEntities() {
        Player player = Persistence.player;

        ArrayList<Position> availableLocations = new ArrayList<>(this.friendLocations.values()); // List of locations that are not taken yet.
        ArrayList<Pokemon> noLocation = new ArrayList<>(); // List of Pokemon who couldn't be placed with their default locations.

        for (Pokemon pokemon : player.pokemonInZones.values())
            if (pokemon.species().friendArea() == this.friendArea) {

                Position position = this.friendLocations.get(new Pair<>(pokemon.species(), pokemon.isShiny()));

                if (position == null) { // If it has no default location, add it to nolocation list
                    Logger.w("Friend " + pokemon + " has no friend area location!");
                    noLocation.add(pokemon);
                } else if (!availableLocations.contains(position)) // If location is already taken, add it to nolocation list
                    noLocation.add(pokemon);
                else { // Else, place it and remove position from available list
                    FriendPokemonEntity p = new FriendPokemonEntity(pokemon);
                    this.addEntity(p);
                    p.spawnAt(position.x, position.y);
                    availableLocations.remove(position);
                }
            }

        // Place Pokemon that couldn't be placed previously
        for (Pokemon pokemon : noLocation) {
            if (availableLocations.isEmpty()) {
                Logger.e("Friend area " + this.friendArea + " has too many friends!");
                break;
            }

            Position position = availableLocations.remove(0);
            FriendPokemonEntity p = new FriendPokemonEntity(pokemon);
            this.addEntity(p);
            p.spawnAt(position.x, position.y);
        }
    }

    @Override
    protected void loadAdditional(Element root) {
        super.loadAdditional(root);

        this.friendLocations = new HashMap<>();
        if (root.getChild("friendlocations") != null) for (Element e : root.getChild("friendlocations").getChildren("friend")) {
            int species = XMLUtils.getAttribute(e, "species", -1);
            if (species == -1 || Registries.species().find(species) == null) continue;
            double x = XMLUtils.getAttribute(e, "x", Math.random() * this.terrain.getWidth());
            double y = XMLUtils.getAttribute(e, "y", Math.random() * this.terrain.getHeight());
            boolean shiny = XMLUtils.getAttribute(e, "shiny", false);
            this.friendLocations.put(new Pair<>(Registries.species().find(species), shiny), new Position(x, y));
        }
    }

}
