package com.darkxell.client.mechanics.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.animation.AnimationListModel;
import com.darkxell.client.model.animation.AnimationModel;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.model.animation.AnimationVariantModels.DefaultVariant;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.image.Sprites.DungeonSprites;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;

public final class Animations {
    public enum AnimationGroup {
        Abilities,
        Custom,
        Items,
        Moves,
        MoveTargets,
        Projectiles,
        Statuses
    }

    private static final HashMap<Integer, AnimationModel> abilities, custom, items, moves, moveTargets, projectiles,
            statuses;

    public static final int ATTACK_DOWN, DEFENSE_DOWN, SP_ATTACK_DOWN, SP_DEFENSE_DOWN, SPEED_DOWN, EVASION_DOWN,
            ACCURACY_DOWN;
    public static final int ATTACK_UP = 10, DEFENSE_UP = 11, SP_ATTACK_UP = 12, SP_DEFENSE_UP = 13, SPEED_UP = 14,
            EVASION_UP = 15, ACCURACY_UP = 16;
    public static final int HURT = 1, HEAL = 3, THROW = 6, TELEPORT = 50, REVIVE = 51;
    public static final int STAT_UP_TO_DOWN = 10;

    static {
        ATTACK_DOWN = ATTACK_UP + STAT_UP_TO_DOWN;
        DEFENSE_DOWN = DEFENSE_UP + STAT_UP_TO_DOWN;
        SP_ATTACK_DOWN = SP_ATTACK_UP + STAT_UP_TO_DOWN;
        SP_DEFENSE_DOWN = SP_DEFENSE_UP + STAT_UP_TO_DOWN;
        SPEED_DOWN = SPEED_UP + STAT_UP_TO_DOWN;
        EVASION_DOWN = EVASION_UP + STAT_UP_TO_DOWN;
        ACCURACY_DOWN = ACCURACY_UP + STAT_UP_TO_DOWN;

        abilities = new HashMap<>();
        custom = new HashMap<>();
        items = new HashMap<>();
        moves = new HashMap<>();
        moveTargets = new HashMap<>();
        projectiles = new HashMap<>();
        statuses = new HashMap<>();
    }

    private static boolean existsAnimation(int id, HashMap<Integer, AnimationModel> registry) {
        return registry.containsKey(id);
    }

    public static boolean existsAnimation(Pair<Integer, AnimationGroup> id) {
        if (id == null)
            return false;
        return registry(id.second).containsKey(id.first);
    }

    public static boolean existsAnimation(String id) {
        Pair<Integer, AnimationGroup> split = splitID(id);
        return existsAnimation(split);
    }

    public static boolean existsItemAnimation(Item item) {
        return existsAnimation(item.getID(), items);
    }

    public static boolean existsMoveAnimation(Move move) {
        return existsAnimation(move.getID(), moves);
    }

    public static boolean existsProjectileAnimation(int projectileID) {
        return existsAnimation(projectileID, projectiles);
    }

    public static boolean existsTargetAnimation(Move move) {
        return existsAnimation(move.getID(), moveTargets);
    }

    public static PokemonAnimation getAbilityAnimation(DungeonPokemon pokemon, Ability ability,
            AnimationEndListener listener) {
        return getAnimation(ability.id, abilities, pokemon, listener);
    }

    public static PokemonAnimation getAnimation(DungeonPokemon target, String id, AnimationEndListener listener) {
        if (!id.contains("/"))
            return getCustomAnimation(target, Integer.parseInt(id), listener);
        String group = id.substring(0, id.indexOf("/"));
        int anim = Integer.parseInt(id.substring(id.indexOf("/") + 1));

        MoveRegistry moves = Registries.moves();

        switch (group.toLowerCase()) {
        case "custom":
            return getCustomAnimation(target, anim, listener);

        case "abilities":
            return getAbilityAnimation(target, Ability.find(anim), listener);

        case "items":
            return getItemAnimation(target, Registries.items().find(anim), listener);

        case "moves":
            return getMoveAnimation(target, moves.find(anim), listener);

        case "projectiles":
            return getProjectileAnimation(target, anim, listener);

        case "statuses":
            return getStatusAnimation(target, StatusConditions.find(anim), listener);

        case "movetargets":
        case "targets":
            return getMoveTargetAnimation(target, moves.find(anim), listener);

        default:
            return null;
        }
    }

    public static PokemonAnimation getAnimation(int id, HashMap<Integer, AnimationModel> registry,
            DungeonPokemon target, AnimationEndListener listener) {
        if (!registry.containsKey(id)) {
            Logger.w("Animation not found: " + id);
            return null;
        }
        AbstractPokemonRenderer renderer = target == null ? null
                : Persistence.dungeonState.pokemonRenderer.getRenderer(target);
        return registry.get(id).createAnimation(target, null, renderer, listener);
    }

    public static PokemonAnimation getCustomAnimation(DungeonPokemon target, int id, AnimationEndListener listener) {
        return getAnimation(id, custom, target, listener);
    }

    public static PokemonAnimation getCutsceneAnimation(int id, CutscenePokemon target, AnimationEndListener listener) {
        if (!custom.containsKey(id)) {
            Logger.w("Animation not found: " + id);
            return null;
        }
        AbstractPokemonRenderer renderer = target == null ? null
                : (AbstractPokemonRenderer) Persistence.currentmap.cutsceneEntityRenderers.getRenderer(target);
        return custom.get(id).createAnimation(null, target, renderer, listener);
    }

    public static AnimationModel getData(int id, AnimationGroup group) {
        return registry(group).get(id);
    }

    public static PokemonAnimation getItemAnimation(DungeonPokemon target, Item i, AnimationEndListener listener) {
        return getAnimation(i.getID(), items, target, listener);
    }

    public static PokemonAnimation getMoveAnimation(DungeonPokemon user, Move m, AnimationEndListener listener) {
        return getAnimation(m.getID(), moves, user, listener);
    }

    public static PokemonAnimation getMoveTargetAnimation(DungeonPokemon target, Move m,
            AnimationEndListener listener) {
        return getAnimation(m.getID(), moveTargets, target, listener);
    }

    public static PokemonAnimation getProjectileAnimation(DungeonPokemon pokemon, int projectileID,
            AnimationEndListener listener) {
        PokemonAnimation a = getAnimation(projectileID, projectiles, pokemon, listener);
        if (a != null)
            a.plays = -1;
        return a;
    }

    public static AbstractAnimation getProjectileAnimationFromItem(DungeonPokemon pokemon, Item item,
            AnimationEndListener listener) {
        BufferedImage sprite = DungeonSprites.items.getSprite(item);
        AnimationVariantModel data = new DefaultVariant();
        data.setBackSpriteUsage(BackSpriteUsage.no);
        data.setSpriteOrder(new Integer[] { item.getSpriteID() });
        data.setGravityX(sprite.getWidth() / 2);
        data.setGravityY(sprite.getHeight() / 2);
        data.setSpriteDuration(10);
        SpritesetAnimation a = new SpritesetAnimation(data, null, DungeonSprites.items, data.getSpriteOrder(),
                listener);
        a.plays = -1;
        return a;
    }

    public static PokemonAnimation getStatChangeAnimation(StatChangedEvent event, AnimationEndListener listener) {
        int statID;

        switch (event.stat) {
        case Defense:
            statID = DEFENSE_UP;
            break;
        case SpecialAttack:
            statID = SP_ATTACK_UP;
            break;
        case SpecialDefense:
            statID = SP_DEFENSE_UP;
            break;
        case Speed:
            statID = SPEED_UP;
            break;
        case Evasiveness:
            statID = EVASION_UP;
            break;
        case Accuracy:
            statID = ACCURACY_UP;
            break;

        default:
            statID = ATTACK_UP;
            break;
        }

        if (event.stage < 0)
            statID += STAT_UP_TO_DOWN;
        return getCustomAnimation(event.target, statID, listener);
    }

    public static PokemonAnimation getStatusAnimation(DungeonPokemon target, StatusCondition s,
            AnimationEndListener listener) {
        PokemonAnimation a = getAnimation(s.id, statuses, target, listener);
        if (a != null)
            a.plays = -1;
        return a;
    }

    public static String[] listAnimations() {
        ArrayList<String> anims = new ArrayList<>();

        for (Integer key : abilities.keySet())
            anims.add("abilities/" + key);
        for (Integer key : custom.keySet())
            anims.add("custom/" + key);
        for (Integer key : items.keySet())
            anims.add("items/" + key);
        for (Integer key : moves.keySet())
            anims.add("moves/" + key);
        for (Integer key : moveTargets.keySet())
            anims.add("targets/" + key);
        for (Integer key : projectiles.keySet())
            anims.add("projectiles/" + key);
        for (Integer key : statuses.keySet())
            anims.add("statuses/" + key);

        anims.sort(Comparator.naturalOrder());

        return anims.toArray(new String[0]);
    }

    public static void loadData() {
        abilities.clear();
        custom.clear();
        items.clear();
        moves.clear();
        moveTargets.clear();
        projectiles.clear();
        statuses.clear();

        AnimationListModel model = ClientModelIOHandlers.animation
                .read(Animations.class.getResource("/data/animations.xml"));

        model.abilities.forEach(m -> abilities.put(m.getID(), m));
        model.custom.forEach(m -> custom.put(m.getID(), m));
        model.items.forEach(m -> items.put(m.getID(), m));
        model.moves.forEach(m -> moves.put(m.getID(), m));
        model.movetargets.forEach(m -> moveTargets.put(m.getID(), m));
        model.projectiles.forEach(m -> projectiles.put(m.getID(), m));
        model.statuses.forEach(m -> statuses.put(m.getID(), m));
    }

    public static boolean movePlaysForEachTarget(Move move) {
        if (move == null)
            return false;
        AnimationModel data = moves.get(move.getID());
        if (data == null)
            return false;
        if (data.getClones() != null) {
            if (data.getClones().startsWith("moves/"))
                return movePlaysForEachTarget(
                        Registries.moves().find(Integer.parseInt(data.getClones().substring("moves/".length()))));
            return false;
        }
        return data.getDefaultModel().isPlaysForEachTarget();
    }

    public static ProjectileMovement projectileMovement(int id) {
        if (!existsProjectileAnimation(id))
            return ProjectileMovement.STRAIGHT;
        AnimationModel data = projectiles.get(id);
        if (data == null)
            return ProjectileMovement.STRAIGHT;
        String movement = null;
        if (data.getClones() != null) {
            if (data.getClones().startsWith("projectiles/"))
                return projectileMovement(Integer.parseInt(data.getClones().substring("projectiles/".length())));
            else
                return ProjectileMovement.STRAIGHT;
        } else
            movement = data.getDefaultModel().getAnimationMovement();
        if (movement != null)
            try {
                return ProjectileMovement.valueOf(movement.toUpperCase());
            } catch (Exception ignored) {
            }
        return ProjectileMovement.STRAIGHT;
    }

    public static void register(AnimationModel animation, AnimationGroup group) {
        if (!existsAnimation(animation.getID(), registry(group)))
            registry(group).put(animation.getID(), animation);
    }

    private static HashMap<Integer, AnimationModel> registry(AnimationGroup group) {
        switch (group) {
        case Abilities:
            return abilities;

        case Custom:
            return custom;

        case Items:
            return items;

        case Moves:
            return moves;

        case MoveTargets:
            return moveTargets;

        case Projectiles:
            return projectiles;

        case Statuses:
            return statuses;
        }
        return null;
    }

    public static void save(File file) {
        AnimationListModel model = new AnimationListModel();
        abilities.values().forEach(m -> model.abilities.add(m));
        custom.values().forEach(m -> model.custom.add(m));
        items.values().forEach(m -> model.items.add(m));
        moves.values().forEach(m -> model.moves.add(m));
        moveTargets.values().forEach(m -> model.movetargets.add(m));
        projectiles.values().forEach(m -> model.projectiles.add(m));
        statuses.values().forEach(m -> model.statuses.add(m));
        ClientModelIOHandlers.animation.export(model, file);
    }

    public static Pair<Integer, AnimationGroup> splitID(String id) {
        if (!id.contains("/")) {
            if (!id.matches("\\d+"))
                return null;
            return new Pair<>(Integer.parseInt(id), AnimationGroup.Custom);
        }
        String group = id.substring(0, id.indexOf("/"));
        int anim = Integer.parseInt(id.substring(id.indexOf("/") + 1));
        switch (group) {
        case "custom":
            return new Pair<>(anim, AnimationGroup.Custom);

        case "ability":
        case "abilities":
            return new Pair<>(anim, AnimationGroup.Abilities);

        case "item":
        case "items":
            return new Pair<>(anim, AnimationGroup.Items);

        case "move":
        case "moves":
            return new Pair<>(anim, AnimationGroup.Moves);

        case "projectile":
        case "projectiles":
            return new Pair<>(anim, AnimationGroup.Projectiles);

        case "status":
        case "statuses":
        case "statuscondition":
        case "statusconditions":
            return new Pair<>(anim, AnimationGroup.Statuses);

        case "target":
        case "targets":
        case "movetarget":
        case "movetargets":
            return new Pair<>(anim, AnimationGroup.MoveTargets);

        default:
            break;
        }
        return null;
    }

    public static void unregister(int id, AnimationGroup group) {
        if (existsAnimation(id, registry(group)))
            registry(group).remove(id);
    }

    private Animations() {
    }

}
