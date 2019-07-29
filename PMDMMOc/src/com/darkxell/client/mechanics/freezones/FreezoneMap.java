package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezones.entities.OtherPlayerEntity;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonValue;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import static com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset.TILE_SIZE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A tiled map of a freezone. Freezones are the areas where you can move freely and don't have to fight.
 */
public abstract class FreezoneMap {
    public FreezoneTile[] tiles;

    /**
     * The width of the map, in tiles.
     */
    public int mapWidth;

    /**
     * The height of the map, in tiles.
     */
    public int mapHeight;

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

    public FreezoneMap(String xmlfilepath, int defaultX, int defaultY, FreezoneInfo info) {
        try {
            this.loadFreezoneData(xmlfilepath);
        } catch (Exception e) {
            Logger.e("Could not build freezonemap from XML file: " + e + " / path : " + xmlfilepath);
            e.printStackTrace();
        }

        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.info = info;
    }

    private int tagIntText(Element root, String name) {
        return Integer.parseInt(root.getChild(name).getText());
    }

    private int tagIntAttr(Element el, String name) {
        return Integer.parseInt(el.getAttributeValue(name));
    }

    private FreezoneTile getReferredTile(Element el) {
        int tileX = this.tagIntAttr(el, "x") / TILE_SIZE;
        int tileY = this.tagIntAttr(el, "y") / TILE_SIZE;
        int id = (this.mapWidth * tileY) + tileX;
        return this.tiles[id];
    }

    private AbstractFreezoneTileset getTileset(String key) {
        if (this.tilesets.containsKey(key)) {
            return this.tilesets.get(key);
        }
        AbstractFreezoneTileset tileset = AbstractFreezoneTileset.getTileSet(key, this.mapWidth * TILE_SIZE,
                this.mapHeight * TILE_SIZE);
        this.tilesets.put(key, tileset);
        return tileset;
    }
    
    protected void loadAdditional(Element root) {}

    private void loadTiles(Element root) {
        List<Element> tileEls = root.getChild("tiles").getChildren();

        this.tiles = new FreezoneTile[mapWidth * mapHeight];
        for (int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE, null);
        }

        AbstractFreezoneTileset defaultTileset = null;
        for (Element el : tileEls) {
            FreezoneTile refTile = this.getReferredTile(el);
            String bgName = el.getAttributeValue("bgName");

            if (bgName.equals("terrain")) {
                boolean solid = el.getAttributeValue("xo").equals("0");
                refTile.type = solid ? FreezoneTile.TYPE_SOLID : FreezoneTile.TYPE_WALKABLE;
            } else {
                AbstractFreezoneTileset refTileset = this.getTileset(bgName);
                if (defaultTileset == null) {
                    defaultTileset = refTileset;
                }

                int xo = this.tagIntAttr(el, "xo") / TILE_SIZE;
                int yo = this.tagIntAttr(el, "yo") / TILE_SIZE;
                refTile.sprite = refTileset.getSprite(xo, yo);
            }
        }

        for (FreezoneTile t : this.tiles) {
            if (t.sprite == null) {
                t.sprite = defaultTileset.getDefault();
            }
        }
    }

    protected void loadFreezoneData(String xmlFilepath) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Element root = builder.build(Res.get(xmlFilepath)).getRootElement();

        this.mapWidth = this.tagIntText(root, "width") / TILE_SIZE;
        this.mapHeight = this.tagIntText(root, "height") / TILE_SIZE;

        this.loadTiles(root);
        this.loadAdditional(root);
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

    public byte getTileTypeAt(double x, double y) {
        int calc = mapWidth * (int) y + (int) x;
        if (calc >= this.tiles.length || calc < 0) {
            return FreezoneTile.TYPE_WALKABLE;
        }
        return this.tiles[calc].type;
    }

    /**
     * Update the OtherPlayer entity destinations and last update timestamp according to the parsed json values for
     * the specified entity.
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
                if (entities.get(i) instanceof OtherPlayerEntity && ((OtherPlayerEntity) entities.get(i)).name.equals(
                        dataname)) {
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
     * Returns the additionnal informations not related to this instance about this freezone.
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
}
