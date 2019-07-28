package com.darkxell.client.resources.images.pokemon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;

public class PokemonPortrait extends RegularSpriteSet {
    public enum PortraitEmotion {
        Amazed(4),
        Angry(12),
        Awkward(0),
        Confused(2),
        Crying(9),
        Determined(6),
        Happy(14),
        Joy(7),
        Laughing(5),
        Normal(-1),
        Relieved(1),
        Sad(10),
        Shocked(3),
        Sneezing(8),
        Struggle(13),
        Thoughtful(11);

        public final int index;

        PortraitEmotion(int index) {
            this.index = index;
        }
    }

    private static final PokemonPortrait alternates = new PokemonPortrait("/pokemons/portraits/forms.png", 20),
            alternateShinies = new PokemonPortrait("/pokemons/portraits/portraits/forms-shiny.png", 20);
    private static final ArrayList<Integer> emotionPokemons = new ArrayList<>();
    private static final RegularSpriteSet emotions = new RegularSpriteSet("/pokemons/portraits/normal-emotions.png", 40,
            600, 1800), shinyEmotions = new RegularSpriteSet("/pokemons/portraits/shiny-emotions.png", 40, 600, 1800);
    private static final PokemonPortrait normal = new PokemonPortrait("/pokemons/portraits/normal.png", 100),
            shinies = new PokemonPortrait("/pokemons/portraits/shiny.png", 100);
    public static final int PORTRAIT_SIZE = 40;

    /**
     * Draws the portrait of the input Pokemon at the input topright location.
     */
    public static void drawPortrait(Graphics2D g, Pokemon pokemon, int x, int y) {
        drawPortrait(g, pokemon, PortraitEmotion.Normal, x, y);
    }

    public static void drawPortrait(Graphics2D g, Pokemon pokemon, PortraitEmotion emotion, int x, int y) {
        drawPortrait(g, pokemon.species(), emotion, pokemon.isShiny(), x, y, false);
    }

    /**
     * Draws the portrait of the input Pokemon at the input topright location.
     */
    public static void drawPortrait(Graphics2D g, PokemonSpecies pokemon, PortraitEmotion emotion, boolean shiny, int x,
            int y, boolean flip) {
        BufferedImage portrait = PokemonPortrait.portrait(pokemon, emotion, shiny);
        if (flip)
            g.drawImage(portrait, x + 4 + portrait.getWidth(), y + 4, -portrait.getWidth(), portrait.getHeight(), null);
        else
            g.drawImage(portrait, x + 4, y + 4, null);
        g.drawImage(HudSprites.portrait.image(), x, y, null);
    }

    private static RegularSpriteSet getEmotionSheet(RegularSpriteSet sheet, PortraitEmotion emotion) {
        if (sheet == normal || sheet == alternates)
            return emotions;
        if (sheet == shinies || sheet == alternateShinies)
            return shinyEmotions;
        return sheet;
    }

    private static int getSheetIndex(PokemonSpecies pokemon, boolean shiny, PortraitEmotion emotion,
            boolean alternative, RegularSpriteSet sheet) {
        if (sheet == emotions || sheet == shinyEmotions)
            return emotionPokemons.indexOf(pokemon.id) * (PortraitEmotion.values().length - 1) + emotion.index;
        return pokemon.id - (alternative ? 10001 : 1);
    }

    public static void load() {
        String[] data = Res.readFile("/pokemons/portraits/have_emotions.txt");
        if (data != null)
            for (String line : data)
                try {
                    emotionPokemons.add(Integer.parseInt(line));
                } catch (NumberFormatException e) {
                    Logger.e("Loading portraits encountered error: " + e);
                    e.printStackTrace();
                }
    }

    /**
     * @return The portrait for the input Pokemon.
     */
    public static BufferedImage portrait(Pokemon pokemon) {
        return portrait(pokemon, PortraitEmotion.Normal);
    }

    /**
     * @return The portrait for the input Pokemon.
     */
    public static BufferedImage portrait(Pokemon pokemon, PortraitEmotion emotion) {
        return portrait(pokemon.species(), emotion, pokemon.isShiny());
    }

    /**
     * @return The portrait for the input Pokemon.
     */
    public static BufferedImage portrait(PokemonSpecies pokemon, PortraitEmotion emotion, boolean shiny) {
        RegularSpriteSet sheet = normal;
        boolean alternative = pokemon.id >= 10000;
        if (shiny && pokemon.id >= 10000)
            sheet = alternateShinies;
        else if (pokemon.id >= 10000)
            sheet = alternates;
        else if (shiny)
            sheet = shinies;

        if (emotion != null && emotion != PortraitEmotion.Normal && emotionPokemons.contains(pokemon.id))
            sheet = getEmotionSheet(sheet, emotion);

        return sheet.getImg(getSheetIndex(pokemon, shiny, emotion, alternative, sheet));
    }

    private PokemonPortrait(String path, int lines) {
        super(path, PORTRAIT_SIZE, 400, lines * PORTRAIT_SIZE);
    }
}
