package com.darkxell.client.mechanics.freezones.zones;

import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.entities.FriendPokemonEntity;
import com.darkxell.common.Registries;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.Position;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaFreezone extends FreezoneMap {

    public final FriendArea friendArea;
    private HashMap<Pair<PokemonSpecies, Boolean>, Position> friendLocations;

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
                FriendPokemonEntity p = new FriendPokemonEntity(pokemon);
                this.addEntity(p);

                Position position = this.friendLocations.get(new Pair<>(pokemon.species(), pokemon.isShiny()));
                if (position == null) {
                    Logger.w("Friend " + pokemon + " has no friend area location!");
                    position = new Position(Math.random() * this.mapWidth, Math.random() * this.mapHeight);
                }
                p.startX = p.posX = position.x;
                p.startY = p.posY = position.y;
            }
    }

    @Override
    protected void loadAdditional(Element root) {
        super.loadAdditional(root);

        this.friendLocations = new HashMap<>();
        if (root.getChild("friendlocations") != null)
            for (Element e : root.getChild("friendlocations").getChildren("friend")) {
                int species = XMLUtils.getAttribute(e, "species", -1);
                if (species == -1 || Registries.species().find(species) == null)
                    continue;
                double x = XMLUtils.getAttribute(e, "x", Math.random() * this.mapWidth);
                double y = XMLUtils.getAttribute(e, "y", Math.random() * this.mapHeight);
                boolean shiny = XMLUtils.getAttribute(e, "shiny", false);
                this.friendLocations.put(new Pair<>(Registries.species().find(species), shiny), new Position(x, y));
            }
    }
}
