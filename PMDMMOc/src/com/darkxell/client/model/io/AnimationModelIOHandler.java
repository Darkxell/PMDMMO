package com.darkxell.client.model.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.model.animation.AnimationListModel;
import com.darkxell.client.model.animation.AnimationModel;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.common.model.io.ModelIOHandler;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.StringUtil;

public class AnimationModelIOHandler extends ModelIOHandler<AnimationListModel> {

    public AnimationModelIOHandler() {
        super(AnimationListModel.class);
    }

    @Override
    protected AnimationListModel handleAfterImport(AnimationListModel object) {

        for (AnimationModel m : object.abilities)
            m.setSpritesPrefix("abilities/");
        for (AnimationModel m : object.custom)
            m.setSpritesPrefix("");
        for (AnimationModel m : object.items)
            m.setSpritesPrefix("items/");
        for (AnimationModel m : object.moves)
            m.setSpritesPrefix("moves/");
        for (AnimationModel m : object.movetargets)
            m.setSpritesPrefix("targets/");
        for (AnimationModel m : object.projectiles)
            m.setSpritesPrefix("projectiles/");
        for (AnimationModel m : object.statuses)
            m.setSpritesPrefix("status/");

        ArrayList<AnimationModel> allAnimations = new ArrayList<>();
        allAnimations.addAll(object.abilities);
        allAnimations.addAll(object.custom);
        allAnimations.addAll(object.items);
        allAnimations.addAll(object.moves);
        allAnimations.addAll(object.movetargets);
        allAnimations.addAll(object.projectiles);
        allAnimations.addAll(object.statuses);

        for (AnimationModel anim : allAnimations)
            this.handleAfterImport(anim);

        return super.handleAfterImport(object);
    }

    private void handleAfterImport(AnimationModel animation) {
        if (animation.getClones() == null && animation.getDefaultModel() == null)
            throw new RuntimeException("Animation has no default data: " + animation.getID());

        this.handleDefaultVariantAfterImport(animation, animation.getDefaultModel());
        for (Direction d : Direction.values())
            this.handleVariantAfterImport(animation, animation.getVariant(d), animation.getDefaultModel());
    }

    @Override
    protected AnimationListModel handleBeforeExport(AnimationListModel model) {
        model = model.copy();

        model.abilities.sort(Comparator.naturalOrder());
        model.custom.sort(Comparator.naturalOrder());
        model.items.sort(Comparator.naturalOrder());
        model.moves.sort(Comparator.naturalOrder());
        model.movetargets.sort(Comparator.naturalOrder());
        model.projectiles.sort(Comparator.naturalOrder());
        model.statuses.sort(Comparator.naturalOrder());

        ArrayList<AnimationModel> allAnimations = new ArrayList<>();
        allAnimations.addAll(model.abilities);
        allAnimations.addAll(model.custom);
        allAnimations.addAll(model.items);
        allAnimations.addAll(model.moves);
        allAnimations.addAll(model.movetargets);
        allAnimations.addAll(model.projectiles);
        allAnimations.addAll(model.statuses);

        for (AnimationModel anim : allAnimations)
            this.handleBeforeExport(anim);

        return super.handleBeforeExport(model);
    }

    private void handleBeforeExport(AnimationModel animation) {
        for (Direction d : Direction.values())
            this.handleVariantBeforeExport(animation, d, animation.getVariant(d), animation.getDefaultModel());
        this.handleDefaultVariantBeforeExport(animation, animation.getDefaultModel());
    }

    private void handleDefaultVariantAfterImport(AnimationModel animation, AnimationVariantModel model) {
        if (model.getAlsoPlay() == null)
            model.setAlsoPlay(new String[0]);
        if (model.getAlsoPlayDelay() == null) {
            model.setAlsoPlayDelay(new Integer[model.getAlsoPlay().length]);
            for (int i = 0; i < model.getAlsoPlay().length; ++i)
                model.getAlsoPlayDelay()[i] = 0;
        }
        if (model.getBackSpriteUsage() == null)
            model.setBackSpriteUsage(BackSpriteUsage.no);
        if (model.getDelayTime() == null)
            model.setDelayTime(0);
        if (model.getHeight() == null)
            model.setHeight(0);
        if (model.getLoopsFrom() == null)
            model.setLoopsFrom(0);
        if (model.getPokemonStateDelay() == null)
            model.setPokemonStateDelay(0);
        if (model.getSoundDelay() == null)
            model.setSoundDelay(0);
        if (model.getSpriteDuration() == null)
            model.setSpriteDuration(2);
        if (model.getSpriteOrder() == null)
            model.setSpriteOrder(new Integer[0]);
        if (model.getSprites() == null)
            model.setSprites(String.valueOf(animation.getID()));
        if (model.getSprites().equals("none"))
            model.setSprites(null);
        if (model.getWidth() == null)
            model.setWidth(0);
        if (model.isPlaysForEachTarget() == null)
            model.setPlaysForEachTarget(true);

        if (model.getGravityX() == null)
            model.setGravityX(model.getWidth() / 2);
        if (model.getGravityY() == null)
            model.setGravityY(model.getHeight() / 2);
    }

    private void handleDefaultVariantBeforeExport(AnimationModel animation, AnimationVariantModel model) {

        if (model.getGravityX().equals(model.getWidth() / 2))
            model.setGravityX(null);
        if (model.getGravityY().equals(model.getHeight() / 2))
            model.setGravityY(null);
        if (model.getAlsoPlayDelay().length == 0) {
            model.setAlsoPlayDelay(null);
        } else if (model.getAlsoPlayDelay().length == model.getAlsoPlay().length) {
            boolean stop = false;
            for (int d : model.getAlsoPlayDelay())
                if (d != 0) {
                    stop = true;
                    break;
                }
            if (!stop)
                model.setAlsoPlayDelay(null);
        }

        if (model.getAlsoPlay().length == 0)
            model.setAlsoPlay(null);
        if (model.getBackSpriteUsage() == BackSpriteUsage.no)
            model.setBackSpriteUsage(null);
        if (model.getDelayTime().equals(0))
            model.setDelayTime(null);
        if (model.getHeight().equals(0))
            model.setHeight(null);
        if (model.getLoopsFrom().equals(0))
            model.setLoopsFrom(null);
        if (model.getPokemonStateDelay().equals(0))
            model.setPokemonStateDelay(null);
        if (model.getSoundDelay().equals(0))
            model.setSoundDelay(null);
        if (model.getSpriteDuration().equals(2))
            model.setSpriteDuration(null);
        if (model.getSprites() == null)
            model.setSprites("none");
        if (model.getSprites().equals(String.valueOf(animation.getID())))
            model.setSprites(null);
        if (model.getSpriteOrder() == null || (model.getSpriteOrder().length == 1 && model.getSpriteOrder()[0] == null))
            model.setSpriteOrder(new Integer[] { 0 });
        if (model.getWidth().equals(0))
            model.setHeight(null);
        if (model.isPlaysForEachTarget().equals(true))
            model.setPlaysForEachTarget(null);
    }

    private void handleVariantAfterImport(AnimationModel parent, AnimationVariantModel variant,
            AnimationVariantModel def) {
        if (variant.getAlsoPlay() == null)
            variant.setAlsoPlay(def.getAlsoPlay());
        if (variant.getAlsoPlayDelay() == null)
            variant.setAlsoPlayDelay(def.getAlsoPlayDelay());
        if (variant.getAnimationMovement() == null)
            variant.setAnimationMovement(def.getAnimationMovement());
        if (variant.getBackSpriteUsage() == null)
            variant.setBackSpriteUsage(def.getBackSpriteUsage());
        if (variant.getCustomAnimation() == null)
            variant.setCustomAnimation(def.getCustomAnimation());
        if (variant.getDelayTime() == null)
            variant.setDelayTime(def.getDelayTime());
        if (variant.getGravityX() == null)
            variant.setGravityX(def.getGravityX());
        if (variant.getGravityY() == null)
            variant.setGravityY(def.getGravityY());
        if (variant.getHeight() == null)
            variant.setHeight(def.getHeight());
        if (variant.getLoopsFrom() == null)
            variant.setLoopsFrom(def.getLoopsFrom());
        if (variant.getOverlay() == null)
            variant.setOverlay(def.getOverlay());
        if (variant.getPokemonMovement() == null)
            variant.setPokemonMovement(def.getPokemonMovement());
        if (variant.getPokemonState() == null)
            variant.setPokemonState(def.getPokemonState());
        if (variant.getPokemonStateDelay() == null)
            variant.setPokemonStateDelay(def.getPokemonStateDelay());
        if (variant.getSound() == null)
            variant.setSound(def.getSound());
        if (variant.getSoundDelay() == null)
            variant.setSoundDelay(def.getSoundDelay());
        if (variant.getSprites() == null)
            variant.setSprites(def.getSprites());
        if (variant.getSpriteDuration() == null)
            variant.setSpriteDuration(def.getSpriteDuration());
        if (variant.getSpriteOrder() == null)
            variant.setSpriteOrder(def.getSpriteOrder());
        if (variant.getSpriteOrder().length == 1 && variant.getSpriteOrder()[0] == null)
            variant.setSpriteOrder(def.getSpriteOrder());
        if (variant.getWidth() == null)
            variant.setWidth(def.getWidth());
        if (variant.isPlaysForEachTarget() == null)
            variant.setPlaysForEachTarget(def.isPlaysForEachTarget());
        if (variant.getGravityX() == null)
            variant.setGravityX(def.getGravityX());
        if (variant.getGravityY() == null)
            variant.setGravityY(def.getGravityY());
    }

    private void handleVariantBeforeExport(AnimationModel parent, Direction d, AnimationVariantModel variant,
            AnimationVariantModel def) {

        if (variant.equals(def))
            parent.setVariant(d, null);

        if (variant.getGravityX().equals(def.getGravityX()))
            variant.setGravityX(null);
        if (variant.getGravityY().equals(def.getGravityY()))
            variant.setGravityY(null);

        if (Arrays.equals(variant.getAlsoPlay(), def.getAlsoPlay()))
            variant.setAlsoPlay(null);
        if (Arrays.equals(variant.getAlsoPlayDelay(), def.getAlsoPlayDelay()))
            variant.setAlsoPlayDelay(null);
        if (StringUtil.equals(variant.getAnimationMovement(), def.getAnimationMovement()))
            variant.setAnimationMovement(null);
        if (variant.getBackSpriteUsage() == def.getBackSpriteUsage())
            variant.setBackSpriteUsage(null);
        if (StringUtil.equals(variant.getCustomAnimation(), def.getCustomAnimation()))
            variant.setCustomAnimation(null);
        if (variant.getDelayTime() != null && variant.getDelayTime().equals(def.getDelayTime()))
            variant.setDelayTime(null);
        if (variant.getGravityX() != null && variant.getGravityX().equals(def.getGravityX()))
            variant.setGravityX(null);
        if (variant.getGravityY() != null && variant.getGravityY().equals(def.getGravityY()))
            variant.setGravityY(null);
        if (variant.getHeight() != null && variant.getHeight().equals(def.getHeight()))
            variant.setHeight(null);
        if (variant.getLoopsFrom() != null && variant.getLoopsFrom().equals(def.getLoopsFrom()))
            variant.setLoopsFrom(null);
        if (variant.getOverlay() != null && variant.getOverlay().equals(def.getOverlay()))
            variant.setOverlay(null);
        if (variant.getPokemonMovement() != null && variant.getPokemonMovement().equals(def.getPokemonMovement()))
            variant.setPokemonMovement(null);
        if (variant.getPokemonState() == def.getPokemonState())
            variant.setPokemonState(null);
        if (variant.getPokemonStateDelay() != null && variant.getPokemonStateDelay().equals(def.getPokemonStateDelay()))
            variant.setPokemonStateDelay(null);
        if (variant.getSound() != null && variant.getSound().equals(def.getSound()))
            variant.setSound(null);
        if (variant.getSoundDelay() != null && variant.getSoundDelay().equals(def.getSoundDelay()))
            variant.setSoundDelay(null);
        if (variant.getSprites() != null && variant.getSprites().equals(def.getSprites()))
            variant.setSprites(null);
        if (variant.getSpriteDuration() != null && variant.getSpriteDuration().equals(def.getSpriteDuration()))
            variant.setSpriteDuration(null);
        if (Arrays.equals(variant.getSpriteOrder(), def.getSpriteOrder()))
            variant.setSpriteOrder(null);
        if (variant.getWidth() != null && variant.getWidth().equals(def.getWidth()))
            variant.setWidth(null);
        if (variant.isPlaysForEachTarget() != null && variant.isPlaysForEachTarget().equals(def.isPlaysForEachTarget()))
            variant.setPlaysForEachTarget(null);
    }

}
