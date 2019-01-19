package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezones.entities.OtherPlayerEntity;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

/**
 * A tiled map of a freezone. Freezones are the areas where you can move freely and don't have to fight.
 */
public abstract class FreezoneMap {
    protected FreezoneTerrain terrain;

    /**
     * True if there shouldn't be an ally entity or other player entities.
     */
    public boolean playerOnly = false;

    public String freezonebgm = "";

    public final FreezoneInfo info;
    public final int defaultX, defaultY;

    /**
     * List the entities in this map. Note that the player isn't actually an entity.
     */
    private ArrayList<FreezoneEntity> entities = new ArrayList<>();
    public ArrayList<TriggerZone> triggerzones = new ArrayList<>();

    public final EntityRendererHolder<FreezoneEntity> entityRenderers = new EntityRendererHolder<>();
    public final EntityRendererHolder<CutsceneEntity> cutsceneEntityRenderers = new EntityRendererHolder<>();

    public FreezoneMap(String xmlPath, int defaultX, int defaultY, FreezoneInfo info) {
        try {
            this.terrain = new FreezoneTerrain(xmlPath);
        } catch (Exception e) {
            Logger.e("Could not build freezone map from XML: " + e + " (path: " + xmlPath + ")");
            e.printStackTrace();
        }

        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.info = info;
    }

    public void addEntity(FreezoneEntity entity) {
        this.entities.add(entity);
        this.entityRenderers.register(entity, entity.createRenderer());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<FreezoneEntity> entities() {
        return (ArrayList<FreezoneEntity>) this.entities.clone();
    }

    public void removeEntity(FreezoneEntity entity) {
        this.entityRenderers.unregister(entity);
        entities.remove(entity);
    }

    private int flushcounter = 0;
    private static final long FLUSHTIMEOUT = 1000000000;

    public void update() {
        Persistence.currentplayer.update();

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
        }

        if (flushcounter >= 120) {
            flushcounter = 0;
            long ct = System.nanoTime();
            for (int i = 0; i < entities.size(); ++i) {
                if (entities.get(i) instanceof OtherPlayerEntity && ((OtherPlayerEntity) entities.get(
                        i)).lastupdate < ct - FLUSHTIMEOUT) {
                    this.removeEntity(entities.get(i));
                    --i;
                }
            }
        } else {
            ++flushcounter;
        }
    }

    /**
     * Update another player's info from packet data (JSON).
     */
    public void updateOtherPlayers(JsonValue data) {
        JsonObject obj = data.asObject();
        String name = obj.getString("name", "");

        if (name.equals("") || Persistence.player.name().equals(name)) {
            return;
        }

        double pfx = obj.getDouble("posfx", 0d);
        double pfy = obj.getDouble("posfy", 0d);
        int spriteID = Integer.parseInt(obj.getString("currentpokemon", "0"));
        for (int i = 0; i < entities.size(); i++) {
            boolean isPlayer = entities.get(i) instanceof OtherPlayerEntity;
            OtherPlayerEntity entity = (OtherPlayerEntity) entities.get(i);
            if (isPlayer && entity.name.equals(name)) {
                entity.applyServerUpdate(pfx, pfy, spriteID);
                return;
            }
        }

        this.addEntity(new OtherPlayerEntity(pfx, pfy, spriteID, name, System.nanoTime()));
    }

    /**
     * Return current freezone metadata.
     */
    public FreezoneInfo getInfo() {
        return this.info;
    }

    /**
     * @return Default X position for a Player in this Map.
     */
    public int defaultX() {
        return this.defaultX;
    }

    /**
     * @return Default Y position for a Player in this Map.
     */
    public int defaultY() {
        return this.defaultY;
    }

    public FreezoneTerrain getTerrain() {
        return this.terrain;
    }
}
