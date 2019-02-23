package com.darkxell.client.resources.images.pokemon;

import java.util.Collection;
import java.util.HashMap;
import javafx.util.Pair;

import org.jdom2.Element;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.xml.XMLUtils;

public class PokemonSpritesetData {

    public final boolean hasBigShadow;
    public final int id;
    final HashMap<Integer, PokemonSpriteSequence> sequences;
    public final int spriteWidth, spriteHeight;
    final HashMap<Pair<PokemonSpriteState, Direction>, Integer> states;

    public PokemonSpritesetData(int id, boolean hasBigShadow, int spriteWidth, int spriteHeight,
            HashMap<Pair<PokemonSpriteState, Direction>, Integer> states,
            HashMap<Integer, PokemonSpriteSequence> sequences) {
        this.id = id;
        this.hasBigShadow = hasBigShadow;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        this.states = new HashMap<>(states);
        this.sequences = new HashMap<>(sequences);
    }

    public PokemonSpritesetData(int id, Element xml) {
        this.id = id;

        this.spriteWidth = Integer.parseInt(xml.getChildText("FrameWidth"));
        this.spriteHeight = Integer.parseInt(xml.getChildText("FrameHeight"));
        this.hasBigShadow = XMLUtils.getAttribute(xml, "bigshadow", false);

        this.states = new HashMap<>();
        this.sequences = new HashMap<>();

        for (Element e : xml.getChild("AnimGroupTable").getChildren()) {
            PokemonSpriteState state = PokemonSpriteState.valueOf(e.getAttributeValue("state").toUpperCase());
            for (Element s : e.getChildren())
                this.states.put(new Pair<>(state, Direction.valueOf(s.getAttributeValue("direction").toUpperCase())),
                        Integer.parseInt(s.getAttributeValue("sequence")));
        }

        for (Element e : xml.getChild("AnimSequenceTable").getChildren())
            this.sequences.put(Integer.parseInt(e.getAttributeValue("id")), new PokemonSpriteSequence(this, e));
    }

    public PokemonSpritesetData(Integer id) {
        this.id = id;
        this.spriteWidth = this.spriteHeight = 16;
        this.hasBigShadow = false;
        this.states = new HashMap<>();
        this.sequences = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public HashMap<Pair<PokemonSpriteState, Direction>, Integer> sequenceMapper() {
        return (HashMap<Pair<PokemonSpriteState, Direction>, Integer>) this.states.clone();
    }

    public Collection<PokemonSpriteSequence> sequences() {
        return this.sequences.values();
    }

    @Override
    public String toString() {
        String value = this.id + "- ";
        PokemonSpecies species = Registries.species().find(this.id);
        if (species != null)
            value += species;
        return value;
    }

    public Element toXML() {
        Element root = new Element("AnimData");
        if (this.spriteWidth != 0)
            root.addContent(new Element("FrameWidth").setText(String.valueOf(this.spriteWidth)));
        if (this.spriteHeight != 0)
            root.addContent(new Element("FrameHeight").setText(String.valueOf(this.spriteHeight)));
        XMLUtils.setAttribute(root, "bigshadow", this.hasBigShadow, false);

        Element grouptable = new Element("AnimGroupTable");
        for (PokemonSpriteState state : PokemonSpriteState.values()) {
            Element s = new Element("AnimGroup").setAttribute("state", state.name().toLowerCase());
            for (Direction d : Direction.DIRECTIONS) {
                Integer sequence = this.states.getOrDefault(new Pair<>(state, d), -1);
                if (sequence != -1)
                    s.addContent(new Element("AnimSequenceIndex").setAttribute("sequence", String.valueOf(sequence))
                            .setAttribute("direction", d.lowercaseName()));
            }
            grouptable.addContent(s);
        }
        root.addContent(grouptable);

        Element sequencetable = new Element("AnimSequenceTable");
        for (PokemonSpriteSequence s : this.sequences.values())
            sequencetable.addContent(s.toXML());
        root.addContent(sequencetable);

        return root;
    }

}
