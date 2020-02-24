package com.darkxell.client.model.animation;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.util.XMLUtils.IntegerArrayAdapter;
import com.darkxell.common.util.XMLUtils.StringArrayAdapter;
import com.darkxell.common.util.language.StringUtil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AnimationVariantModel {

    /** The ID of the sprites. <code>null</code> if no sprites. */
    @XmlAttribute
    private String sprites;

    /** Dimensions of a single sprite on the spritesheet, if any. */
    @XmlAttribute
    private Integer width;

    @XmlAttribute
    private Integer height;

    /** The x offset to apply to the sprites. -1 defaults to the center of the sprite. */
    @XmlAttribute(name = "x")
    private Integer gravityX;

    /** The y offset to apply to the sprites. -1 defaults to the center of the sprite. */
    @XmlAttribute(name = "y")
    private Integer gravityY;

    /** The duration of each sprite. */
    @XmlAttribute(name = "spriteduration")
    private Integer spriteDuration;

    /** Whether to display sprites in front of or behind the Pokemon if any. */
    @XmlAttribute(name = "backsprites")
    private BackSpriteUsage backSpriteUsage;

    /** The time to wait for this Animation to play. */
    @XmlAttribute(name = "delaytime")
    private Integer delayTime;

    /** The ID of the sound to play when this Animation is played. */
    @XmlAttribute
    private String sound;

    /** The number of ticks to wait before playing the sound. */
    @XmlAttribute(name = "sounddelay")
    private Integer soundDelay;

    /** The name of the movement of the animation. <code>null</code> if no movement. */
    @XmlAttribute(name = "movement")
    private String animationMovement;

    /** If true and this is a Move Animation, this Animation plays for each target hit by the Move. */
    @XmlAttribute(name = "playsforeachtarget")
    private Boolean playsForEachTarget;

    /** The state applied to the Pokemon, if any. */
    @XmlAttribute(name = "state")
    private PokemonSpriteState pokemonState;

    /** The movement applied to the Pokemon, if any. */
    @XmlAttribute(name = "pkmnmovement")
    private String pokemonMovement;

    /** List of animation IDs to play with this Animation. */
    @XmlAttribute(name = "alsoplay")
    @XmlJavaTypeAdapter(StringArrayAdapter.class)
    private String[] alsoPlay;

    /** For each other animation to play, the number of ticks to wait before starting it. */
    @XmlAttribute(name = "alsoplaydelay")
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] alsoPlayDelay;

    /** if not null, this Animation should instantiate a specific class when loaded. */
    @XmlAttribute(name = "customanimation")
    private String customAnimation;

    /** If this Animation plays several times, the index in the order of the sprites to start at in the restarts. */
    @XmlAttribute(name = "loopsfrom")
    private Integer loopsFrom;

    /** If not -1, the ID of the animation to play as an overlay. */
    @XmlAttribute
    private Integer overlay;

    /** The delay before applying the Pokemon State. */
    @XmlAttribute(name = "statedelay")
    private Integer pokemonStateDelay;

    /** The order of the sprites in the sprite set. */
    @XmlValue
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] spriteOrder;

    public AnimationVariantModel() {
    }

    public String[] getAlsoPlay() {
        return alsoPlay;
    }

    public Integer[] getAlsoPlayDelay() {
        return alsoPlayDelay;
    }

    public String getAnimationMovement() {
        return animationMovement;
    }

    public BackSpriteUsage getBackSpriteUsage() {
        return backSpriteUsage;
    }

    public String getCustomAnimation() {
        return customAnimation;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public Integer getGravityX() {
        return gravityX;
    }

    public Integer getGravityY() {
        return gravityY;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getLoopsFrom() {
        return loopsFrom;
    }

    public Integer getOverlay() {
        return overlay;
    }

    public String getPokemonMovement() {
        return pokemonMovement;
    }

    public PokemonSpriteState getPokemonState() {
        return pokemonState;
    }

    public Integer getPokemonStateDelay() {
        return pokemonStateDelay;
    }

    public String getSound() {
        return sound;
    }

    public Integer getSoundDelay() {
        return soundDelay;
    }

    public Integer getSpriteDuration() {
        return spriteDuration;
    }

    public Integer[] getSpriteOrder() {
        return spriteOrder;
    }

    public String getSprites() {
        return sprites;
    }

    public Integer getWidth() {
        return width;
    }

    public Boolean isPlaysForEachTarget() {
        return playsForEachTarget;
    }

    public void setAlsoPlay(String[] alsoPlay) {
        this.alsoPlay = alsoPlay;
    }

    public void setAlsoPlayDelay(Integer[] alsoPlayDelay) {
        this.alsoPlayDelay = alsoPlayDelay;
    }

    public void setAnimationMovement(String animationMovement) {
        this.animationMovement = animationMovement;
    }

    public void setBackSpriteUsage(BackSpriteUsage backSpriteUsage) {
        this.backSpriteUsage = backSpriteUsage;
    }

    public void setCustomAnimation(String customAnimation) {
        this.customAnimation = customAnimation;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public void setGravityX(Integer gravityX) {
        this.gravityX = gravityX;
    }

    public void setGravityY(Integer gravityY) {
        this.gravityY = gravityY;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setLoopsFrom(Integer loopsFrom) {
        this.loopsFrom = loopsFrom;
    }

    public void setOverlay(Integer overlay) {
        this.overlay = overlay;
    }

    public void setPlaysForEachTarget(Boolean playsForEachTarget) {
        this.playsForEachTarget = playsForEachTarget;
    }

    public void setPokemonMovement(String pokemonMovement) {
        this.pokemonMovement = pokemonMovement;
    }

    public void setPokemonState(PokemonSpriteState pokemonState) {
        this.pokemonState = pokemonState;
    }

    public void setPokemonStateDelay(Integer pokemonStateDelay) {
        this.pokemonStateDelay = pokemonStateDelay;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setSoundDelay(Integer soundDelay) {
        this.soundDelay = soundDelay;
    }

    public void setSpriteDuration(Integer spriteDuration) {
        this.spriteDuration = spriteDuration;
    }

    public void setSpriteOrder(Integer[] spriteOrder) {
        this.spriteOrder = spriteOrder;
    }

    public void setSprites(String sprites) {
        this.sprites = sprites;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public AnimationVariantModel copy() {
        AnimationVariantModel clone = this.createCopy();
        clone.setAlsoPlay(this.alsoPlay == null ? null : this.alsoPlay.clone());
        clone.setAlsoPlayDelay(this.alsoPlayDelay == null ? null : this.alsoPlayDelay.clone());
        clone.setAnimationMovement(this.animationMovement);
        clone.setBackSpriteUsage(this.backSpriteUsage);
        clone.setCustomAnimation(this.customAnimation);
        clone.setDelayTime(this.delayTime);
        clone.setGravityX(this.gravityX);
        clone.setGravityY(this.gravityY);
        clone.setHeight(this.height);
        clone.setLoopsFrom(this.loopsFrom);
        clone.setOverlay(this.overlay);
        clone.setPlaysForEachTarget(this.playsForEachTarget);
        clone.setPokemonMovement(this.pokemonMovement);
        clone.setPokemonState(this.pokemonState);
        clone.setPokemonStateDelay(this.pokemonStateDelay);
        clone.setSound(this.sound);
        clone.setSoundDelay(this.soundDelay);
        clone.setSpriteDuration(this.spriteDuration);
        clone.setSpriteOrder(this.spriteOrder == null ? null : this.spriteOrder.clone());
        clone.setSprites(this.sprites);
        clone.setWidth(this.width);
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnimationVariantModel))
            return false;
        AnimationVariantModel o = (AnimationVariantModel) obj;
        if (!Arrays.equals(this.getAlsoPlay(), o.getAlsoPlay()))
            return false;
        if (!Arrays.equals(this.getAlsoPlayDelay(), o.getAlsoPlayDelay()))
            return false;
        if (!StringUtil.equals(this.getAnimationMovement(), o.getAnimationMovement()))
            return false;
        if (this.getBackSpriteUsage() != o.getBackSpriteUsage())
            return false;
        if (!StringUtil.equals(this.getCustomAnimation(), o.getCustomAnimation()))
            return false;
        if (!this.delayTime.equals(o.delayTime))
            return false;
        if (!this.gravityX.equals(o.gravityX))
            return false;
        if (!this.gravityY.equals(o.gravityY))
            return false;
        if (!this.height.equals(o.height))
            return false;
        if (!this.loopsFrom.equals(o.loopsFrom))
            return false;
        if ((this.overlay == null) != (o.overlay == null))
            return false;
        if (this.overlay != null && !this.overlay.equals(o.overlay))
            return false;
        if (!StringUtil.equals(this.pokemonMovement, o.pokemonMovement))
            return false;
        if (this.pokemonState != o.pokemonState)
            return false;
        if (!this.pokemonStateDelay.equals(o.pokemonStateDelay))
            return false;
        if (!StringUtil.equals(this.sound, o.sound))
            return false;
        if (!this.spriteDuration.equals(o.spriteDuration))
            return false;
        if (!Arrays.equals(this.spriteOrder, o.spriteOrder))
            return false;
        if (!StringUtil.equals(this.sprites, o.sprites))
            return false;
        if (!this.width.equals(o.width))
            return false;
        if (!this.isPlaysForEachTarget().equals(o.isPlaysForEachTarget()))
            return false;
        return true;
    }

    protected abstract AnimationVariantModel createCopy();

    public boolean isEmpty() {
        return this.alsoPlay == null && this.alsoPlayDelay == null && this.animationMovement == null
                && this.backSpriteUsage == null && this.customAnimation == null && this.delayTime == null
                && this.gravityX == null && this.gravityY == null && this.height == null && this.loopsFrom == null
                && this.overlay == null && this.pokemonMovement == null && this.pokemonState == null
                && this.pokemonStateDelay == null && this.sound == null && this.spriteDuration == null
                && this.spriteOrder == null && this.sprites == null && this.width == null
                && this.playsForEachTarget == null;
    }

    @Override
    public String toString() {
        return this.createCopy().getClass().getSimpleName() + "[alsoPlay=" + Arrays.toString(this.alsoPlay)
                + ", alsoPlayDelay=" + Arrays.toString(this.alsoPlayDelay) + ", animationMovement="
                + this.animationMovement + ", backSpriteUsage=" + this.backSpriteUsage + ", customAnimation="
                + this.customAnimation + ", delayTime=" + this.delayTime + ", gravityX=" + this.gravityX + ", gravityY="
                + this.gravityY + ", height=" + this.height + ", loopsFrom=" + this.loopsFrom + ", overlay="
                + this.overlay + ", pokemonMovement=" + this.pokemonMovement + ", pokemonState=" + this.pokemonState
                + ", pokemonStateDelay=" + this.pokemonStateDelay + ", sound=" + this.sound + ", spriteDuration="
                + this.spriteDuration + ", spriteOrder=" + Arrays.toString(this.spriteOrder) + ", sprites="
                + this.sprites + ", width=" + this.width + ", playsForEachTarget=" + this.playsForEachTarget + "]";
    }

}
