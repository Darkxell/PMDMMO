package com.darkxell.client.resources.image.pokemon.body;

import java.util.Arrays;
import java.util.Collection;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

/**
 * Pokemon Spriteset Data Sequence
 */
public class PSDSequence implements Comparable<PSDSequence> {

    public final int duration;
    final PSDFrame[] frames;
    public final int id;
    public final int rushPoint, hitPoint, returnPoint;

    public PSDSequence(Integer id) {
        this.id = id;
        this.duration = 0;
        this.frames = new PSDFrame[0];
        this.rushPoint = this.hitPoint = this.returnPoint = 0;
    }

    public PSDSequence(Integer id, int rush, int hit, int ret, Collection<PSDFrame> frames) {
        this.id = id;
        this.rushPoint = rush;
        this.hitPoint = hit;
        this.returnPoint = ret;
        this.frames = frames.toArray(new PSDFrame[0]);
        this.duration = this.totalDuration();
    }

    public PSDSequence(PokemonSpritesetData pokemonSpritesetData, Element xml) {
        this.id = XMLUtils.getAttribute(xml, "id", -1);
        this.rushPoint = XMLUtils.getAttribute(xml, "rush", 0);
        this.hitPoint = XMLUtils.getAttribute(xml, "hit", 0);
        this.returnPoint = XMLUtils.getAttribute(xml, "return", 0);

        this.frames = new PSDFrame[xml.getChildren().size()];
        int i = 0;
        for (Element e : xml.getChildren()) {
            this.frames[i] = new PSDFrame(pokemonSpritesetData, e);
            ++i;
        }
        this.duration = this.totalDuration();
    }

    @Override
    public int compareTo(PSDSequence o) {
        return Integer.compare(this.id, o.id);
    }

    public double dashOffset(int tick) {
        if (tick <= this.rushPoint || tick >= this.returnPoint)
            return 0;
        if (tick == this.hitPoint)
            return 1;
        if (tick <= this.hitPoint)
            return (tick - this.rushPoint) * 1. / (this.hitPoint - this.rushPoint);
        return 1 - ((tick - this.hitPoint) * 1. / (this.returnPoint - this.hitPoint));
    }

    public Collection<PSDFrame> frames() {
        return Arrays.asList(this.frames);
    }

    public PSDFrame getFrame(int tick) {
        return this.frames[this.getFrameIndex(tick)];
    }

    private int getFrameIndex(int tick) {
        int i = -1;
        while (tick >= 0) {
            ++i;
            tick -= this.frames[i].duration;
        }
        return i;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    private int totalDuration() {
        int d = 0;
        for (PSDFrame f : this.frames)
            d += f.duration;
        return d;
    }

    public Element toXML() {
        Element root = new Element("AnimSequence");
        XMLUtils.setAttribute(root, "id", this.id, -1);
        XMLUtils.setAttribute(root, "rush", this.rushPoint, 0);
        XMLUtils.setAttribute(root, "hit", this.hitPoint, 0);
        XMLUtils.setAttribute(root, "return", this.returnPoint, 0);

        for (PSDFrame frame : this.frames)
            root.addContent(frame.toXML());

        return root;
    }

}
