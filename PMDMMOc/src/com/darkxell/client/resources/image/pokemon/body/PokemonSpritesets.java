package com.darkxell.client.resources.image.pokemon.body;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;

import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.client.model.pokemonspriteset.PokemonAnimationModel;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;

public final class PokemonSpritesets {

    /** Stores the loaded Spriteset data. */
    private static final HashMap<Integer, PokemonSpritesetData> data = new HashMap<>();
    /** Stores the loaded Spritesheets. */
    private static final HashMap<Integer, PokemonSpriteset> spritesets = new HashMap<>();

    public static boolean dataExists(Integer id) {
        return data.containsKey(id) && data.get(id).id == id;
    }

    /** Disposes of the Spritesheet for the Pokemon with the input ID. */
    public static void disposeSpriteset(int id) {
        spritesets.remove(id);
    }

    public static PokemonSpritesetData getData(int id) {
        return data.get(id);
    }

    /** Returns the Spritesheet for the Pokemon with the input ID. Loads it if not loaded. */
    public static PokemonSpriteset getSpriteset(int id) {
        if (!spritesets.containsKey(id))
            loadSpriteset(id);
        return spritesets.get(id);
    }

    public static PokemonSpriteset getSpriteset(Pokemon pokemon) {
        return getSpriteset(pokemon.species(), pokemon.isShiny());
    }

    public static PokemonSpriteset getSpriteset(PokemonSpecies species, boolean shiny) {
        return getSpriteset(getSpritesetID(species, shiny));
    }

    private static int getSpritesetID(PokemonSpecies species, boolean shiny) {
        return (shiny ? -1 : 1) * species.getID();
    }

    public static Collection<PokemonSpritesetData> listSpritesetData() {
        return data.values();
    }

    /** Reads the sprites data file. */
    public static void loadData() {
        loadSpriteset(0);
    }

    public static void loadData(String externalPath) {
        loadData();
        File folder = new File(externalPath);
        if (!folder.exists())
            try {
                folder.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }

        for (File data : folder.listFiles())
            try {
                int id = Integer.parseInt(data.getName().replaceAll("\\.xml", ""));
                loadSpritesetData(id, id, externalPath + "/" + id);
            } catch (Exception e) {
                Logger.w("Unconventional sprite data file, skipped: " + data.getName());
            }
    }

    /** Loads the Spritesheet for the Pokemon with the input ID. */
    public static void loadSpriteset(int id) {
        String filename = Math.abs(id) + "";
        int effectiveID = Math.abs(id);
        if (id < 0 && Res.exists("/pokemons/" + effectiveID + "s.png"))
            filename = effectiveID + "s";
        else if (id < 0) {
            spritesets.put(id, spritesets.get(0));
            return;
        }

        if (!data.containsKey(effectiveID))
            loadSpritesetData(effectiveID);

        PokemonSpritesetData d = data.get(effectiveID);
        if (id != 0 && d == data.get(0)) {
            spritesets.put(id, spritesets.get(0));
            return;
        }

        spritesets.put(id, new PokemonSpriteset("/pokemons/" + filename + ".png", data.get(effectiveID)));
    }

    public static void loadSpriteset(PokemonSpecies species, boolean shiny) {
        loadSpriteset(getSpritesetID(species, shiny));
    }

    /** Loads the animations data for the Pokemon with the input ID. */
    public static void loadSpritesetData(int id) {
        int effectiveID = Math.abs(id);
        loadSpritesetData(id, effectiveID, "/pokemons/data/" + effectiveID);
    }

    @SuppressWarnings("deprecation")
    private static void loadSpritesetData(int id, int dataID, String path) {
        PokemonAnimationModel model;
        if (path.startsWith("/"))
            model = ClientModelIOHandlers.pokemonAnimation.read(PokemonSpritesets.class.getResource(path + ".xml"));
        else
            try {
                model = ClientModelIOHandlers.pokemonAnimation.read(new File(path + ".xml").toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                model = null;
            }
        if (model == null) {
            setSpritesetData(id, data.get(0));
            return;
        }

        setSpritesetData(id, new PokemonSpritesetData(dataID, model));
    }

    public static PMDRegularSpriteset loadTestSpriteset(int id, int spriteWidth, int spriteHeight) {
        return new PMDRegularSpriteset("/pokemons/" + id + ".png", spriteWidth, spriteHeight, -1, -1);
    }

    public static void remove(PokemonSpritesetData item) {
        data.entrySet().removeIf(entry -> entry.getValue() == item);
        spritesets.entrySet().removeIf(entry -> entry.getValue().data == item);
    }

    public static void setSpritesetData(int id, PokemonSpritesetData pokemonSpritesetData) {
        data.put(id, pokemonSpritesetData);
    }

    private PokemonSpritesets() {
    }

}
