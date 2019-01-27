package com.darkxell.client.mechanics.freezone;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezone.entity.FreezoneEntity;
import com.darkxell.client.mechanics.freezone.entity.FreezoneEntityFactory;
import com.darkxell.client.mechanics.freezone.entity.OtherPlayerEntity;
import com.darkxell.client.mechanics.freezone.trigger.TriggerZone;
import com.darkxell.client.mechanics.freezone.trigger.TriggerZoneFactory;
import com.darkxell.client.graphics.EntityRendererHolder;
import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.graphics.layers.BackgroundLayerFactory;
import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Areas where you can move freely and don't have to fight.
 *
 * <h3>XML Format</h3>
 *
 * <ul>
 * <li>Player only ({@code playeronly} attribute on root)</li>
 * <li>Default spawn position ({@code <default x="int" y="int" />})</li>
 * <li>Terrain ({@code <terrain />}, source either with tag as root or sourced, see {@link FreezoneTerrain})</li>
 * <li>Background music ({@code <bgm source="resource abspath" />})</li>
 * <li>Background, if any ({@code <background type="" />})</li>
 * <li>Triggers ({@code <triggers />}, see {@link TriggerZoneFactory})</li>
 * <li>Entities ({@code <entities />}, see {@link FreezoneEntityFactory})</li>
 * <li>Extends ({@code <extends source="resource abspath" />}</li>
 * </ul>
 *
 * <h3>Extends behavior</h3>
 *
 * <p>When there is an {@code <extends />} element in a file, the freezone will load that file first, and then
 * continue to load the current file, overwriting it.</p>
 *
 * <p>Here is how each of the properties are extended:</p>
 *
 * <ul>
 * <li>Attributes on the root are always overwritten.</li>
 * <li>Only the root terrain and background is loaded.</li>
 * <li>Default position, background music, and other such properties that may be added in the future are overwritten
 * if the elements are present.</li>
 * </ul>
 * <p>
 * TODO: Pre-loading neighbors, reducing load times, etc.
 */
public class FreezoneMap {
    protected FreezoneTerrain terrain;
    private AbstractGraphicsLayer background;

    /**
     * Should any players present be hidden?
     */
    public boolean playerOnly = false;

    public String freezonebgm = "";

    public final FreezoneInfo info;
    private int defaultX, defaultY;

    /**
     * List the entities in this map. Note that the player isn't actually an entity.
     */
    private ArrayList<FreezoneEntity> entities = new ArrayList<>();
    public ArrayList<TriggerZone> triggerzones = new ArrayList<>();

    public final EntityRendererHolder<FreezoneEntity> entityRenderers = new EntityRendererHolder<>();
    public final EntityRendererHolder<CutsceneEntity> cutsceneEntityRenderers = new EntityRendererHolder<>();

    public FreezoneMap(String xmlPath, FreezoneInfo info) {
        this.info = info;

        try {
            this.load(xmlPath, true);
        } catch (Exception e) {
            Logger.e("Could not build freezone map from XML: " + e + " (path: " + xmlPath + ")");
            e.printStackTrace();
        }
    }

    private String loadExtensionPath(Element root) {
        Element extendEl = root.getChild("extends");

        if (extendEl == null) {
            return null;
        }

        return root.getAttributeValue("source");
    }

    private void loadProperties(Element root) {
        // flag attributes (similar to html5 boolean attributes)
        // these attributes are always overwritten in child freezones, for clarity.
        this.playerOnly = root.getAttribute("playeronly") != null;

        // default positions
        Element defaultPosEl = root.getChild("default");
        if (defaultPosEl != null) {
            this.defaultX = XMLUtils.getAttribute(defaultPosEl, "x", 0);
            this.defaultY = XMLUtils.getAttribute(defaultPosEl, "y", 0);
        }

        // bgm
        Element bgmEl = root.getChild("bgm");
        if (bgmEl != null) {
            this.freezonebgm = XMLUtils.getAttribute(bgmEl, "source", "town.mp3");
        }

        // add to triggers
        Element triggers = root.getChild("triggers");
        if (triggers != null) {
            for (Element triggerEl : triggers.getChildren("trigger")) {
                this.triggerzones.add(TriggerZoneFactory.getZone(triggerEl));
            }
        }

        // add to entities
        Element entities = root.getChild("entities");
        if (entities != null) {
            for (Element entityEl : entities.getChildren("entity")) {
                this.addEntity(FreezoneEntityFactory.getEntity(entityEl));
            }
        }
    }

    private void loadTerrain(Element root) throws IOException, JDOMException {
        Element bgLayerEl = root.getChild("background");
        if (bgLayerEl != null) {
            this.background = BackgroundLayerFactory.getLayer(bgLayerEl);
        }

        Element terrainEl = root.getChild("terrain");
        if (terrainEl != null) {
            Attribute terrainSource = terrainEl.getAttribute("source");
            if (terrainSource == null) {
                this.terrain = new FreezoneTerrain(terrainEl);
            } else {
                this.terrain = new FreezoneTerrain(terrainSource.getValue());
            }
        }
    }

    /**
     * Load freezone file from resource path.
     *
     * @param xmlPath Resource path to XML file.
     * @param isRoot  Is this call the root terrain file?
     */
    private void load(String xmlPath, boolean isRoot) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Element root = builder.build(Res.get(xmlPath)).getRootElement();

        String extendsPath = this.loadExtensionPath(root);
        if (extendsPath != null) {
            this.load(extendsPath, false);
        }

        this.loadProperties(root);
        if (isRoot) {
            this.loadTerrain(root);
        }
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
                if (entities.get(i) instanceof OtherPlayerEntity &&
                        ((OtherPlayerEntity) entities.get(i)).lastupdate < ct - FLUSHTIMEOUT) {
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

    public AbstractGraphicsLayer getBackground() {
        return this.background;
    }
}
