package com.darkxell.client.mechanics.freezones;

import static com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset.TILE_SIZE;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezones.entities.OtherPlayerEntity;
import com.darkxell.client.mechanics.freezones.xmlstorage.FreezoneModelRegistry;
import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.freezone.FreezoneTileModel;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonValue;

/**
 * A tiled map of a freezone. Freezones are the areas where you can move freely and don't have to fight.
 */
public abstract class FreezoneMap {

    public static FreezoneModel readModel(String modelPath) {
        return FreezoneModelRegistry.getFreezone(modelPath);
    }

    private final FreezoneModel model;

    protected FreezoneTile[][] tiles;

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

    /**
     * Tileset cache.
     */
    private HashMap<String, AbstractFreezoneTileset> tilesets = new HashMap<>();

    public FreezoneMap(FreezoneModel model, int defaultX, int defaultY, FreezoneInfo info) {
        this.model = model;
        this.info = info;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.loadTiles(model);
    }

    protected void loadTiles(FreezoneModel model) {
        this.tiles = new FreezoneTile[this.getWidth()][this.getHeight()];
        for (FreezoneTileModel tileModel : model.getTiles()) {
            this.tiles[tileModel.getX()][tileModel.getY()] = new FreezoneTile(tileModel);
        }
    }

    AbstractFreezoneTileset getTileset(String key) {
        if (this.tilesets.containsKey(key)) {
            return this.tilesets.get(key);
        }
        AbstractFreezoneTileset tileset = AbstractFreezoneTileset.getTileSet(key, this.getWidth() * TILE_SIZE,
                this.getHeight() * TILE_SIZE);
        this.tilesets.put(key, tileset);
        return tileset;
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
                if (entities.get(i) instanceof OtherPlayerEntity
                        && ((OtherPlayerEntity) entities.get(i)).lastupdate < ct - FLUSHTIMEOUT) {
                    this.removeEntity(entities.get(i));
                    --i;
                }
            }
        } else {
            ++flushcounter;
        }
    }

    /**
     * Update the OtherPlayer entity destinations and last update timestamp according to the parsed json values for the
     * specified entity.
     */
    public void updateOtherPlayers(JsonValue data) {
        String dataname = data.asObject().getString("name", "");
        if (Persistence.player.name().equals(dataname)) {
            return;
        }
        double pfx = data.asObject().getDouble("posfx", 0d);
        double pfy = data.asObject().getDouble("posfy", 0d);
        int spriteID = Integer.parseInt(data.asObject().getString("currentpokemon", "0"));
        boolean found = false;
        if (!dataname.equals("")) {
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) instanceof OtherPlayerEntity
                        && ((OtherPlayerEntity) entities.get(i)).name.equals(dataname)) {
                    OtherPlayerEntity etty = (OtherPlayerEntity) entities.get(i);
                    etty.applyServerUpdate(pfx, pfy, spriteID);
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.addEntity(new OtherPlayerEntity(pfx, pfy, spriteID, dataname, System.nanoTime()));
            }
        }
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

    public int getHeight() {
        return this.model.getHeight();
    }

    public int getWidth() {
        return this.model.getWidth();
    }

    public FreezoneTile getTileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= this.tiles.length || y >= this.tiles[x].length)
            return this.tiles[0][0];
        return this.tiles[x][y];
    }
}
