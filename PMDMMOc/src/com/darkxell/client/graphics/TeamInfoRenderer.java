package com.darkxell.client.graphics;

import static com.darkxell.client.resources.images.pokemon.PokemonPortrait.PORTRAIT_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.graphics.TextRenderer.PMDChar;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

/** Draws Team info. */
public final class TeamInfoRenderer {

    public static final Message loading = new Message("general.loading");
    public static final int PREFERRED_HEIGHT, MIN_HEIGHT;
    public static final int TITLE_HEIGHT;

    static {
        TITLE_HEIGHT = TextRenderer.height() + TextRenderer.lineSpacing() * 2;
        PREFERRED_HEIGHT = Math.max(TextRenderer.height() * 3 + TextRenderer.lineSpacing() * 4,
                PORTRAIT_SIZE + TextRenderer.lineSpacing() * 2);
        MIN_HEIGHT = (int) (TextRenderer.height() * 1.5 + TextRenderer.lineSpacing() * 3);
    }

    /*
     * private static void drawMember(Graphics2D g, Pokemon pokemon, int x, int y, int width, int height) { y += 5;
     * g.drawImage(PokemonPortrait.portrait(pokemon), x + 5, y, null); if (width == MEMBER_WIDTH) { int symbol =
     * TextRenderer.width(PMDChar.type_0); if (pokemon.species().type2 != null) TextRenderer.render(g,
     * pokemon.species().type2.symbol(), x + MEMBER_WIDTH - 2 - symbol, y); TextRenderer.render(g,
     * pokemon.species().type1.symbol(), x + MEMBER_WIDTH - 2 - symbol - (pokemon.species().type2 == null ? 0 : symbol +
     * 1), y); } else { Message m = pokemon.species().type1.getName() .addSuffix(pokemon.species().type2 == null ? new
     * Message("", false) : pokemon.species().type2.getName().addPrefix(" ")); TextRenderer.render(g, m, x +
     * MEMBER_WIDTH * 2 - 5 - TextRenderer.width(m), y); m = new Message("team.hp").addReplacement("<max>",
     * TextRenderer.alignNumber(pokemon.getBaseStats().getHealth(), 3)).addReplacement("<current>",
     * TextRenderer.alignNumber(pokemon.getDungeonPokemon() == null ? pokemon.getBaseStats().getHealth() :
     * pokemon.getDungeonPokemon().getHp(), 3)); TextRenderer.render(g, m, x + MEMBER_WIDTH * 2 - 5 -
     * TextRenderer.width(m), y + 5 + TextRenderer.height()); } int barsize = width - 5 - PORTRAIT_SIZE; int hp = (int)
     * (barsize * (pokemon.getDungeonPokemon() == null ? 1 : pokemon.getDungeonPokemon().getHp() * 1d /
     * pokemon.getBaseStats().getHealth())); int xp = (int) (barsize * (pokemon.experience() * 1d /
     * pokemon.experienceToNextLevel())); x += 5 + PORTRAIT_SIZE; y += PORTRAIT_SIZE - 1;
     * g.setColor(Palette.TRANSPARENT_GRAY); g.drawLine(x, y - 3, x + barsize - 1, y - 3); g.drawLine(x, y - 3 - 5, x +
     * barsize - 1, y - 3 - 5); g.setColor(Color.WHITE); g.drawLine(x, y - 1, x + barsize - 1, y - 1); g.drawLine(x, y -
     * 1 - 3, x + barsize - 1, y - 1 - 3); g.drawLine(x, y - 1 - 3 - 5, x + barsize - 1, y - 1 - 3 - 5);
     * g.setColor(Palette.TEAM_HP_GREEN); g.fillRect(x, y - 3 - 4, hp, 3); g.setColor(Palette.TEAM_HP_RED); g.fillRect(x
     * + hp, y - 3 - 4, barsize - hp, 3); g.setColor(Palette.TEAM_XP_BLUE); g.fillRect(x, y - 2, xp, 3);
     * g.setColor(Palette.TEAM_XP_PURPLE); g.fillRect(x + xp, y - 2, barsize - xp, 3); x += 5; y -= PORTRAIT_SIZE - 1;
     * TextRenderer.render(g, pokemon.getNickname(), x, y); String gender = pokemon.gender() == Pokemon.MALE ?
     * PMDChar.male.value : pokemon.gender() == Pokemon.FEMALE ? PMDChar.female.value : PMDChar.minus.value;
     * TextRenderer.render(g, new Message("team.level").addReplacement("<lvl>",
     * Integer.toString(pokemon.level())).addReplacement("<gender>", gender), x, y + 5 + TextRenderer.height()); }
     */

    private static void drawBox(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(Palette.TRANSPARENT_GRAY);
        g.drawRect(x, y, width, height);
    }

    private static void drawInfo(Graphics2D g, Pokemon pokemon, int x, int y, int width, int height, int portraitSize,
            int statBarsHeight) {
        int availableWidth = width - portraitSize - TextRenderer.lineSpacing() * 2;
        int xmax = x + width - 1;

        Message name = pokemon.getNickname();
        x += TextRenderer.lineSpacing() * 2 + portraitSize;
        y += TextRenderer.lineSpacing();
        TextRenderer.render(g, name, x, y);

        Message fulltypes = pokemon.species().type1.getName();
        Message smalltypes = new Message(pokemon.species().type1.symbol(), false);
        if (pokemon.species().type2 != null) {
            fulltypes.addSuffix(" " + pokemon.species().type2.getName());
            smalltypes.addSuffix(pokemon.species().type2.symbol());
        }

        boolean drewTypesBelow = false;
        int availableHeight = portraitSize - statBarsHeight;

        Message level = new Message("team.level").addReplacement("<lvl>", String.valueOf(pokemon.level()));
        Message levelshort = new Message("team.level.short").addReplacement("<lvl>", String.valueOf(pokemon.level()));
        Message gender = new Message(pokemon.gender() == Pokemon.MALE ? PMDChar.male.value
                : pokemon.gender() == Pokemon.FEMALE ? PMDChar.female.value : PMDChar.minus.value, false);
        Message hp = new Message("team.hp")
                .addReplacement("<max>", TextRenderer.alignNumber(pokemon.getBaseStats().getHealth(), 3))
                .addReplacement("<current>",
                        TextRenderer
                                .alignNumber(pokemon.getDungeonPokemon() == null ? pokemon.getBaseStats().getHealth()
                                        : pokemon.getDungeonPokemon().getHp(), 3));
        Message hpshort = new Message("team.hp.short")
                .addReplacement("<max>", TextRenderer.alignNumber(pokemon.getBaseStats().getHealth(), 3))
                .addReplacement("<current>",
                        TextRenderer
                                .alignNumber(pokemon.getDungeonPokemon() == null ? pokemon.getBaseStats().getHealth()
                                        : pokemon.getDungeonPokemon().getHp(), 3));

        Message levelToDraw = level, genderToDraw = gender, hpToDraw = hp;

        if (availableHeight < TextRenderer.height() * 2 + TextRenderer.lineSpacing()) // 1 line info
        {
            Message complete = new Message("").addSuffix(name).addSuffix("  ").addSuffix(levelshort).addSuffix("  ")
                    .addSuffix(gender).addSuffix("  ").addSuffix(hpshort).addSuffix("  ").addSuffix(smalltypes);
            if (TextRenderer.width(complete) <= availableWidth)
                TextRenderer.render(g, complete, x, y);
            else {
                Message secondtry = new Message("").addSuffix(name).addSuffix(" ").addSuffix(levelshort).addSuffix(" ")
                        .addSuffix(hpshort).addSuffix(" ").addSuffix(smalltypes);
                if (TextRenderer.width(secondtry) <= availableWidth)
                    TextRenderer.render(g, secondtry, x, y);
                else {
                    Message thirdtry = new Message("").addSuffix(name).addSuffix(" ").addSuffix(levelshort)
                            .addSuffix(" ").addSuffix(smalltypes);
                    if (TextRenderer.width(thirdtry) <= availableWidth)
                        TextRenderer.render(g, thirdtry, x, y);
                }
            }
        } else // 2 lines info
        {
            Message nameWithFullTypes = pokemon.getNickname().addSuffix("  " + fulltypes);
            if (TextRenderer.width(nameWithFullTypes) < availableWidth)
                TextRenderer.render(g, fulltypes, xmax - TextRenderer.width(fulltypes), y);
            else {
                Message nameWithSmallTypes = pokemon.getNickname().addSuffix(smalltypes);
                if (TextRenderer.width(nameWithSmallTypes) < availableWidth)
                    TextRenderer.render(g, smalltypes, xmax - TextRenderer.width(smalltypes), y);
                else {
                    drewTypesBelow = true;
                    y += TextRenderer.height() + TextRenderer.lineSpacing();
                    TextRenderer.render(g, smalltypes, x, y);
                }
            }

            if (drewTypesBelow)
                TextRenderer.render(g, levelshort, xmax - TextRenderer.width(levelshort), y);
            else {
                Message complete = new Message("").addSuffix(level).addSuffix("  ").addSuffix(gender).addSuffix("  ")
                        .addSuffix(hp);

                y += TextRenderer.height() + TextRenderer.lineSpacing();
                if (TextRenderer.width(complete) > availableWidth) {
                    Message secondTry = new Message("").addSuffix(levelshort).addSuffix(" ").addSuffix(gender)
                            .addSuffix(" ").addSuffix(hp);
                    if (TextRenderer.width(secondTry) <= availableWidth)
                        levelToDraw = levelshort;
                    else {
                        Message thirdTry = new Message("").addSuffix(level).addSuffix(" ").addSuffix(gender)
                                .addSuffix(" ").addSuffix(hpshort);
                        if (TextRenderer.width(thirdTry) <= availableWidth)
                            hpToDraw = hpshort;
                        else {
                            Message fourthTry = new Message("").addSuffix(levelshort).addSuffix(" ").addSuffix(gender)
                                    .addSuffix(" ").addSuffix(hpshort);
                            if (TextRenderer.width(fourthTry) <= availableWidth) {
                                levelToDraw = levelshort;
                                hpToDraw = hpshort;
                            } else {
                                Message fifthTry = new Message("").addSuffix(levelshort).addSuffix(" ")
                                        .addSuffix(hpshort);
                                if (TextRenderer.width(fifthTry) <= availableWidth) {
                                    levelToDraw = levelshort;
                                    genderToDraw = null;
                                    hpToDraw = hpshort;
                                } else {
                                    Message sixthTry = new Message("").addSuffix(levelshort);
                                    if (TextRenderer.width(sixthTry) <= availableWidth) {
                                        levelToDraw = levelshort;
                                        genderToDraw = null;
                                        hpToDraw = null;
                                    }
                                }
                            }
                        }
                    }
                }

                if (levelToDraw != null)
                    TextRenderer.render(g, levelToDraw, x, y);
                if (genderToDraw != null)
                    if (levelToDraw != null)
                        TextRenderer.render(g, genderToDraw, x + TextRenderer.width("  " + levelToDraw), y);
                    else
                        TextRenderer.render(g, genderToDraw, x, y);
                if (hpToDraw != null)
                    TextRenderer.render(g, hpToDraw, xmax - TextRenderer.width(hpToDraw), y);
            }
        }

    }

    private static void drawMember(Graphics2D g, int totalwidth, int totalheight, Pokemon pokemon,
            Rectangle dimensions) {
        final int x = dimensions.x, y = dimensions.y, width = dimensions.width, height = dimensions.height;
        drawBox(g, x, y, width, height);
        int portraitSize = drawPortrait(g, pokemon, x, y, width, height);
        int statBarsHeight = drawStatBars(g, pokemon, x, y, width, height, portraitSize);
        drawInfo(g, pokemon, x, y, width, height, portraitSize, statBarsHeight);
    }

    private static int drawPortrait(Graphics2D g, Pokemon pokemon, int x, int y, int width, int height) {
        BufferedImage portrait = PokemonPortrait.portrait(pokemon);
        int size = height - TextRenderer.lineSpacing() * 2;
        if (size > PORTRAIT_SIZE)
            size = PORTRAIT_SIZE;
        g.drawImage(portrait, x + TextRenderer.lineSpacing(), y + TextRenderer.lineSpacing(), size, size, null);
        return size;
    }

    private static int drawStatBars(Graphics2D g, Pokemon pokemon, int x, int y, int width, int height,
            int portraitSize) {
        int barsize = width - TextRenderer.lineSpacing() - portraitSize;
        int hp = (int) (barsize * (pokemon.getDungeonPokemon() == null ? 1
                : pokemon.getDungeonPokemon().getHp() * 1d / pokemon.getBaseStats().getHealth()));
        int xp = (int) (barsize * (pokemon.experience() * 1d / pokemon.experienceToNextLevel()));
        int barheight = (int) Math.round(height * 1. / 15);
        if (barheight == 0)
            barheight = 1;
        boolean drawBorders = barheight >= 3;

        x += TextRenderer.lineSpacing() + portraitSize;
        y += TextRenderer.lineSpacing() + portraitSize;

        y -= barheight;

        // Draw XP
        g.setColor(Palette.TEAM_XP_BLUE);
        g.fillRect(x, y, xp, barheight);
        g.setColor(Palette.TEAM_XP_PURPLE);
        g.fillRect(x + xp, y, barsize - xp, barheight);

        y -= barheight;
        if (drawBorders)
            y -= 1;

        // Draw HP
        g.setColor(Palette.TEAM_HP_GREEN);
        g.fillRect(x, y, hp, barheight);
        g.setColor(Palette.TEAM_HP_RED);
        g.fillRect(x + hp, y, barsize - hp, barheight);

        if (drawBorders) {
            y += barheight * 2;
            g.setColor(Color.WHITE);
            g.drawLine(x, y - barheight, x + barsize - 1, y - barheight);
            g.drawLine(x, y - barheight * 2 - 1, x + barsize - 1, y - barheight * 2 - 1);
        }

        return barheight * 2 + (drawBorders ? 2 : 0);
    }

    private static Rectangle[] findBestColumnSplit(ArrayList<Pokemon> team, Rectangle[] preferredDimensions, int width,
            int height) {
        int eachHeight = (height - TITLE_HEIGHT) / team.size();
        if (eachHeight < MIN_HEIGHT)
            preferredDimensions = findBestSquareSplit(team, preferredDimensions, width, height);
        else {
            if (eachHeight < PREFERRED_HEIGHT)
                for (Rectangle dim : preferredDimensions)
                    dim.height = eachHeight;

            int y = TITLE_HEIGHT;
            for (Rectangle dim : preferredDimensions) {
                dim.width = width - 1;
                dim.y = y;
                y += dim.height;
            }
        }

        return preferredDimensions;
    }

    private static Rectangle[] findBestSquareSplit(ArrayList<Pokemon> team, Rectangle[] preferredDimensions, int width,
            int height) {
        boolean odd = team.size() % 2 == 1;
        int eachHeight = (height - TITLE_HEIGHT) * 2 / (team.size() + (odd ? 1 : 0));
        int startI = odd ? 1 : 0;
        int y = TITLE_HEIGHT;
        if (odd) {
            preferredDimensions[0].width = width;
            preferredDimensions[0].height = eachHeight;
            y += preferredDimensions[0].height;
        }
        for (int i = startI; i < team.size(); ++i) {
            preferredDimensions[i].y = y;
            preferredDimensions[i].width = width / 2;
            preferredDimensions[i].height = eachHeight;
            if (i % 2 == (odd ? 0 : 1)) {
                preferredDimensions[i].x = width / 2;
                y += eachHeight;
            }
        }
        return preferredDimensions;
    }

    private static Rectangle[] getPreferredDimensions(ArrayList<Pokemon> team) {
        Rectangle[] dims = new Rectangle[team.size()];
        for (int i = 0; i < dims.length; ++i)
            dims[i] = new Rectangle(0, TITLE_HEIGHT + i * PREFERRED_HEIGHT, getPreferredWidth(team.get(i)),
                    PREFERRED_HEIGHT);
        return dims;
    }

    private static int getPreferredWidth(Pokemon pokemon) {
        String line1 = pokemon.getNickname() + "   " + pokemon.species().type1.getName();
        if (pokemon.species().type2 != null)
            line1 += "  " + pokemon.species().type2.getName();

        String line2 = new Message("team.level").addReplacement("<lvl>", Integer.toString(pokemon.level())).toString();
        line2 += "  " + (pokemon.gender() == Pokemon.MALE ? PMDChar.male.value
                : pokemon.gender() == Pokemon.FEMALE ? PMDChar.female.value : PMDChar.minus.value);
        line2 += new Message("team.hp")
                .addReplacement("<max>", TextRenderer.alignNumber(pokemon.getBaseStats().getHealth(), 3))
                .addReplacement("<current>",
                        TextRenderer
                                .alignNumber(pokemon.getDungeonPokemon() == null ? pokemon.getBaseStats().getHealth()
                                        : pokemon.getDungeonPokemon().getHp(), 3));

        return Math.max(TextRenderer.width(line1), TextRenderer.width(line2));
    }

    public static Pair<Integer, Integer> render(Graphics2D g, int width, int height) {

        Player p = Persistence.player;

        if (p == null || p.getTeamLeader() == null) {
            TextRenderer.render(g, loading, width / 2 - TextRenderer.width(loading) / 2,
                    height / 5 - TextRenderer.height() / 2);
            g.setColor(new Color(0, 120, 180));
            g.fillRect(0, 0, width, height / 4);
            return new Pair<>(width, height / 4);
        } else {
            g.setColor(new Color(0, 120, 180));
            g.fillRect(0, 0, width, TITLE_HEIGHT);
            g.setColor(Palette.TRANSPARENT_GRAY);
            g.fillRect(0, 0, width, TITLE_HEIGHT);
            Pokemon[] realTeam = p.getTeam();
            ArrayList<Pokemon> team = new ArrayList<Pokemon>();
            for (Pokemon pk : realTeam)
                if (!(pk.getDungeonPokemon() != null && pk.getDungeonPokemon().isFainted()))
                    team.add(pk);

            if (team.size() == 0)
                return new Pair<>(0, 0);
            Rectangle[] preferredDimensions = getPreferredDimensions(team);
            Rectangle[] split = findBestColumnSplit(team, preferredDimensions, width, height);

            Message title = new Message("team.title").addReplacement("<player>", p.name());
            TextRenderer.render(g, title, width / 2 - TextRenderer.width(title) / 2,
                    TITLE_HEIGHT / 2 - TextRenderer.height() / 2);

            int maxX = 0, maxY = 0;
            for (int i = 0; i < team.size(); ++i) {
                maxX = (int) Math.max(maxX, split[i].getMaxX());
                maxY = (int) Math.max(maxY, split[i].getMaxY());
            }
            g.setColor(new Color(0, 120, 180));
            g.fillRect(0, TITLE_HEIGHT, maxX, maxY - TITLE_HEIGHT);
            for (int i = 0; i < team.size(); ++i)
                drawMember(g, width, height, team.get(i), split[i]);
            return new Pair<>(maxX, maxY);
        }
    }

    private TeamInfoRenderer() {
    }

}
