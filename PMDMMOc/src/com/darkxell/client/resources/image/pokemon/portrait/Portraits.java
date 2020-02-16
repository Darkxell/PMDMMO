package com.darkxell.client.resources.image.pokemon.portrait;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;

public final class Portraits {

    private static final PortraitSpriteset alternates = new PortraitSpriteset("/pokemons/portraits/forms.png", 10001,
            20);
    private static final PortraitSpriteset alternateShinies = new PortraitSpriteset(
            "/pokemons/portraits/forms-shiny.png", 10001, 20);
    static final ArrayList<Integer> emotionPokemons = new ArrayList<>();
    private static final EmotionPortraitsSpriteset emotions = new EmotionPortraitsSpriteset(
            "/pokemons/portraits/normal-emotions.png");
    private static final PortraitSpriteset normal = new PortraitSpriteset("/pokemons/portraits/normal.png", 1, 100);
    private static final PortraitSpriteset shinies = new PortraitSpriteset("/pokemons/portraits/shiny.png", 1, 100);
    private static final EmotionPortraitsSpriteset shinyEmotions = new EmotionPortraitsSpriteset(
            "/pokemons/portraits/shiny-emotions.png");

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
        BufferedImage portrait = portrait(pokemon, emotion, shiny);
        if (flip)
            g.drawImage(portrait, x + 4 + portrait.getWidth(), y + 4, -portrait.getWidth(), portrait.getHeight(), null);
        else
            g.drawImage(portrait, x + 4, y + 4, null);
        g.drawImage(HudSprites.portrait.image(), x, y, null);
    }

    private static AbstractPortraitSpriteset getEmotionSheet(AbstractPortraitSpriteset sheet, PortraitEmotion emotion) {
        if (sheet == normal || sheet == alternates)
            return emotions;
        if (sheet == shinies || sheet == alternateShinies)
            return shinyEmotions;
        return sheet;
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
        AbstractPortraitSpriteset sheet = normal;
        boolean alternative = pokemon.getID() >= 10000;
        if (shiny && alternative)
            sheet = alternateShinies;
        else if (alternative)
            sheet = alternates;
        else if (shiny)
            sheet = shinies;

        if (emotion != null && emotion != PortraitEmotion.Normal && emotionPokemons.contains(pokemon.getID()))
            sheet = getEmotionSheet(sheet, emotion);

        return sheet.getPortrait(pokemon, emotion);
    }

    private Portraits() {
    }
}
