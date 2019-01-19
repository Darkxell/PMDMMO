package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Position;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.HashMap;

import static com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset.TILE_SIZE;

public class FreezoneTerrain {
    private FreezoneTile[] tiles;
    private FreezoneTile defaultTile;

    /**
     * Width of the map, in tiles.
     */
    private int width;

    /**
     * Height of the map, in tiles.
     */
    private int height;

    /**
     * Cache of all tilesets requested so far.
     */
    private HashMap<String, AbstractFreezoneTileset> tilesets = new HashMap<>();

    public FreezoneTerrain(String xmlPath) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Element root = builder.build(Res.get(xmlPath)).getRootElement();

        this.load(root);
    }

    /**
     * Get an XML element's child text as an integer.
     */
    private int tagIntText(Element root, String name) {
        return Integer.parseInt(root.getChild(name).getText());
    }

    /**
     * Get an XML element's attribute value as an integer
     */
    private int tagIntAttr(Element el, String name) {
        return Integer.parseInt(el.getAttributeValue(name));
    }

    private void initTiles() {
        this.tiles = new FreezoneTile[this.width * this.height];
        for (int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = new FreezoneTile();
        }
        this.defaultTile = new FreezoneTile();
    }

    /**
     * Retrieves tile based on XML tag attributes.
     *
     * @param el {@code <tile />} element, or any element with an {@code x} or {@code y} attribute as integers.
     */
    private FreezoneTile getReferredTile(Element el) {
        int tileX = this.tagIntAttr(el, "x") / TILE_SIZE;
        int tileY = this.tagIntAttr(el, "y") / TILE_SIZE;
        return this.get(tileX, tileY);
    }

    /**
     * Get tileset based on key, lazy-loading when needed.
     *
     * @see AbstractFreezoneTileset#getTileSet(String, int, int)
     */
    private AbstractFreezoneTileset getTileset(String key) {
        if (this.tilesets.containsKey(key)) {
            return this.tilesets.get(key);
        }
        AbstractFreezoneTileset tileset = AbstractFreezoneTileset.getTileSet(key, this.width * TILE_SIZE,
                this.height * TILE_SIZE);
        this.tilesets.put(key, tileset);
        return tileset;
    }

    private void load(Element root) {
        this.width = this.tagIntText(root, "width") / TILE_SIZE;
        this.height = this.tagIntText(root, "height") / TILE_SIZE;
        this.initTiles();

        AbstractFreezoneTileset defaultTileset = null;
        for (Element el : root.getChild("tiles").getChildren()) {
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
                refTile.sprite = refTileset.get(xo, yo);
            }
        }

        for (FreezoneTile t : this.tiles) {
            if (t.sprite == null) {
                t.sprite = defaultTileset.getDefault();
            }
        }
    }

    /**
     * Get tile based on tile offset. See {@link #get(int, int)} for the conversion direction.
     *
     * @param offset Direct access index.
     * @return Tile, or default tile if tile index is out of bounds.
     */
    public FreezoneTile get(int offset) {
        if (offset >= this.tiles.length || offset < 0) {
            return this.defaultTile;
        }
        return this.tiles[offset];
    }

    /**
     * Shortcut for 2D access to a tile.
     *
     * @see #get(int)
     */
    public FreezoneTile get(int x, int y) {
        return this.get(y * this.width + x);
    }

    /**
     * Shortcut for double access to map - simply force-converts double to int, i.e. rounds down to the nearest integer.
     *
     * @see #get(int, int)
     */
    public FreezoneTile get(double x, double y) {
        return this.get((int) x, (int) y);
    }

    /**
     * Collision detection.
     *
     * @return Does the box overlap with any solid blocks in this terrain?
     */
    public boolean hasCollision(DoubleRectangle box) {
        for (Position pos : box.getCorners()) {
            if (this.get(pos.x, pos.y).type == FreezoneTile.TYPE_SOLID) {
                return true;
            }
        }
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Get total size of map. Should be equal to {@link #getWidth()} times {@link #getHeight()}.
     */
    public int size() {
        return this.tiles.length;
    }
}
