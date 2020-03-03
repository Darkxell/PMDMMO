package com.darkxell.client.model.animation;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.CompoundAnimation;
import com.darkxell.client.mechanics.animation.CustomAnimationFactory;
import com.darkxell.client.mechanics.animation.OverlayAnimation;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.mechanics.animation.spritemovement.SpritesetAnimationMovement;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.animation.AnimationVariantModels.DefaultVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.EastVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.NorthEastVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.NorthVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.NorthWestVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.SouthEastVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.SouthVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.SouthWestVariant;
import com.darkxell.client.model.animation.AnimationVariantModels.WestVariant;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

@XmlRootElement(name = "anim")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimationModel implements Comparable<AnimationModel> {

    /** The ID of this Animation. */
    @XmlAttribute
    private int id;

    /** If not <code>null</code>, this Animation is an exact copy of the Animation with this ID. */
    @XmlAttribute(name = "clone")
    private String clones = null;

    @XmlElement(name = "default")
    private DefaultVariant defaultModel = new DefaultVariant();

    @XmlElement
    private NorthVariant north = new NorthVariant();

    @XmlElement
    private NorthEastVariant northeast = new NorthEastVariant();

    @XmlElement
    private EastVariant east = new EastVariant();

    @XmlElement
    private SouthEastVariant southeast = new SouthEastVariant();

    @XmlElement
    private SouthVariant south = new SouthVariant();

    @XmlElement
    private SouthWestVariant southwest = new SouthWestVariant();

    @XmlElement
    private WestVariant west = new WestVariant();

    @XmlElement
    private NorthWestVariant northwest = new NorthWestVariant();

    /** A prefix to apply to the sprites path. Does not apply if the sprites ID contains '/' */
    @XmlTransient
    private String spritesPrefix;

    public AnimationModel() {
    }

    public AnimationModel(AnimationModel anim) {
        this.id = anim.id;
        this.clones = anim.clones;
        this.spritesPrefix = anim.spritesPrefix;
    }

    public AnimationModel(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(AnimationModel o) {
        return Integer.compare(this.id, o.id);
    }

    public PokemonAnimation createAnimation(DungeonPokemon dungeon, CutscenePokemon cutscene,
            AbstractPokemonRenderer renderer, AnimationEndListener listener) {
        if (this.clones != null)
            if (dungeon != null)
                return Animations.getAnimation(dungeon, this.clones, listener);
            else
                return Animations.getCutsceneAnimation(Integer.parseInt(this.clones), cutscene, listener);

        AnimationVariantModel model = this.getDefaultModel();

        if (renderer != null) {
            Direction d = renderer.sprite().getFacingDirection();
            if (this.getVariant(d) != null)
                model = this.getVariant(d);
        }
        
        model = model.copy();

        PokemonAnimation a = null;
        if (model.getCustomAnimation() != null) {
            a = CustomAnimationFactory.load(model, renderer, listener);
        }

        if (model.getCustomAnimation() == null || (model.getCustomAnimation() != null && a == null)) {
            if (model.getSprites() == null) {
                a = new PokemonAnimation(model, renderer, 0, listener);
                a.delayTime = model.getDelayTime();
            } else {
                String actualSprites = model.getSprites();
                if (!model.getSprites().contains("/"))
                    actualSprites = "/" + this.spritesPrefix + model.getSprites();
                actualSprites = "/animations" + actualSprites;

                PMDRegularSpriteset spriteset = new PMDRegularSpriteset(actualSprites + ".png", model.getWidth(),
                        model.getHeight(), -1, -1);
                Integer[] order = model.getSpriteOrder().clone();
                if (order.length == 0 && spriteset.isLoaded()) {
                    order = new Integer[model.getBackSpriteUsage() == BackSpriteUsage.yes ? spriteset.spriteCount() / 2
                            : spriteset.spriteCount()];
                    for (int i = 0; i < order.length; ++i)
                        order[i] = i;
                }
                a = new SpritesetAnimation(model, renderer, spriteset, order, listener);
                ((SpritesetAnimation) a).spritesetMovement = SpritesetAnimationMovement
                        .create(model.getAnimationMovement(), (SpritesetAnimation) a);
                if (model.getDelayTime() <= 0)
                    a.delayTime = model.getSpriteDuration() * order.length;
                else
                    a.delayTime = model.getDelayTime();
            }
        }

        if (a.delayTime <= 0)
            a.delayTime = 15;

        if (model.getPokemonMovement() != null) {
            String movement = model.getPokemonMovement();
            a.movement = PokemonAnimationMovement.create(a, movement);
            if (a.movement != null)
                a.duration = Math.max(a.duration, a.movement.duration);
        }

        if (model.getAlsoPlay().length > 0 && dungeon != null) {
            ArrayList<AbstractAnimation> anims = new ArrayList<>();
            ArrayList<Integer> delays = new ArrayList<>();
            AbstractAnimation tmp;

            for (int i = 0; i < model.getAlsoPlay().length; ++i) {
                if (model.getAlsoPlay()[i].equals(""))
                    continue;
                tmp = Animations.getAnimation(dungeon, model.getAlsoPlay()[i], null);
                if (tmp != null && !anims.contains(tmp)) {
                    anims.add(tmp);
                    delays.add(model.getAlsoPlayDelay()[i]);
                }
            }

            if (!anims.isEmpty()) {
                CompoundAnimation anim = new CompoundAnimation(model, renderer, null);
                anim.add(a, 0);
                for (int i = 0; i < anims.size(); ++i)
                    anim.add(anims.get(i), delays.get(i));
                a = anim;
            }
        }

        if (model.getOverlay() != null) {
            OverlayAnimation overlay = new OverlayAnimation(model, model.getOverlay(), a, listener);
            Persistence.dungeonState.staticAnimationsRenderer.add(overlay);
            overlay.start();
        }

        return a;
    }

    public String getClones() {
        return clones;
    }

    public AnimationVariantModel getDefaultModel() {
        return this.defaultModel;
    }

    public int getID() {
        return id;
    }

    public String getSpritesPrefix() {
        return spritesPrefix;
    }

    public AnimationVariantModel getVariant(Direction direction) {
        switch (direction) {
        case NORTH:
            return this.north;
        case NORTHEAST:
            return this.northeast;
        case NORTHWEST:
            return this.northwest;
        case SOUTH:
            return this.south;
        case SOUTHEAST:
            return this.southeast;
        case SOUTHWEST:
            return this.southwest;
        case EAST:
            return this.east;
        case WEST:
            return this.west;
        default:
            return this.getDefaultModel();
        }
    }

    public void setVariant(Direction direction, AnimationVariantModel variant) {
        if (direction == null) {
            this.defaultModel = null;
            return;
        }
        switch (direction) {
        case NORTH:
            this.north = (NorthVariant) variant;
            break;
        case NORTHEAST:
            this.northeast = (NorthEastVariant) variant;
            break;
        case NORTHWEST:
            this.northwest = (NorthWestVariant) variant;
            break;
        case SOUTH:
            this.south = (SouthVariant) variant;
            break;
        case SOUTHEAST:
            this.southeast = (SouthEastVariant) variant;
            break;
        case SOUTHWEST:
            this.southwest = (SouthWestVariant) variant;
            break;
        case EAST:
            this.east = (EastVariant) variant;
            break;
        case WEST:
            this.west = (WestVariant) variant;
            break;
        }
    }

    public void setClones(String clones) {
        this.clones = clones;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setSpritesPrefix(String spritesPrefix) {
        this.spritesPrefix = spritesPrefix;
    }

    public AnimationModel copy() {
        AnimationModel clone = new AnimationModel(this);
        clone.defaultModel = (DefaultVariant) this.defaultModel.copy();
        clone.north = (NorthVariant) this.north.copy();
        clone.northeast = (NorthEastVariant) this.northeast.copy();
        clone.northwest = (NorthWestVariant) this.northwest.copy();
        clone.south = (SouthVariant) this.south.copy();
        clone.southeast = (SouthEastVariant) this.southeast.copy();
        clone.southwest = (SouthWestVariant) this.southwest.copy();
        clone.east = (EastVariant) this.east.copy();
        clone.west = (WestVariant) this.west.copy();
        return clone;
    }

}
