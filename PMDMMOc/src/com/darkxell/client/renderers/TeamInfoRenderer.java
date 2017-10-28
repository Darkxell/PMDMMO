package com.darkxell.client.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

/** Draws Team info. */
public final class TeamInfoRenderer
{

	private static final BufferedImage canvas;
	private static final Graphics2D g;
	public static final int height;
	public static final int MEMBER_HEIGHT = 50, MEMBER_WIDTH = 125;
	public static final int TITLE_HEIGHT = 30;
	public static final int width;

	static
	{
		height = MEMBER_HEIGHT * 2 + TITLE_HEIGHT;
		width = MEMBER_WIDTH * 2;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = canvas.createGraphics();
	}

	private static void drawMember(Pokemon pokemon, int x, int y, int width, int height)
	{
		y += 5;
		g.drawImage(PokemonPortrait.portrait(pokemon), x + 5, y, null);
		TextRenderer.render(g, pokemon.getNickname(), x + 10 + PokemonPortrait.PORTRAIT_SIZE, y);

		if (width == MEMBER_WIDTH)
		{
			int symbol = TextRenderer.width(PMDChar.type_0);
			if (pokemon.species.type2 != null) TextRenderer.render(g, pokemon.species.type2.symbol(), x + MEMBER_WIDTH - 1 - symbol, y);
			TextRenderer.render(g, pokemon.species.type1.symbol(), x + MEMBER_WIDTH - 1 - symbol - (pokemon.species.type2 == null ? 0 : symbol + 1), y);
		} else
		{
			Message types = pokemon.species.type1.getName().addSuffix(
					pokemon.species.type2 == null ? new Message("", false) : pokemon.species.type2.getName().addPrefix(" "));
			TextRenderer.render(g, types, x + MEMBER_WIDTH * 2 - 5 - TextRenderer.width(types), y);
		}
	}

	public static void render(Graphics2D parentGraphics, int canvasWidth, int canvasHeight)
	{
		Player p = Persistance.player;
		Pokemon[] team = p.getTeam();

		g.clearRect(0, 0, width, height);
		g.setColor(new Color(0, 120, 180));
		g.fillRect(0, 0, width, height);
		g.setColor(Palette.TRANSPARENT_GRAY);
		g.fillRect(0, 0, width, TITLE_HEIGHT);

		Message title = new Message("team.title").addReplacement("<player>", p.name);
		TextRenderer.render(g, title, width / 2 - TextRenderer.width(title) / 2, TITLE_HEIGHT / 2 - TextRenderer.height() / 2);

		int y = TITLE_HEIGHT;
		g.setColor(Palette.TRANSPARENT_GRAY);

		if (team.length == 1) drawMember(team[0], 0, y, MEMBER_WIDTH * 2, MEMBER_HEIGHT * 2);
		else g.drawLine(0, y + MEMBER_HEIGHT, width, y + MEMBER_HEIGHT);

		if (team.length == 4)
		{
			drawMember(team[0], 0, y, MEMBER_WIDTH, MEMBER_HEIGHT);
			drawMember(team[1], MEMBER_WIDTH, y, MEMBER_WIDTH, MEMBER_HEIGHT);
		}

		if (team.length == 2 || team.length == 3) drawMember(team[0], 0, y, MEMBER_WIDTH * 2, MEMBER_HEIGHT);
		if (team.length == 2) drawMember(team[1], 0, y + MEMBER_HEIGHT, MEMBER_WIDTH * 2, MEMBER_HEIGHT);
		else if (team.length == 3 || team.length == 4)
		{
			g.drawLine(MEMBER_WIDTH, team.length == 3 ? y + MEMBER_HEIGHT : y, MEMBER_WIDTH, height);
			drawMember(team[team.length == 3 ? 1 : 2], 0, y + MEMBER_HEIGHT, MEMBER_WIDTH, MEMBER_HEIGHT);
			drawMember(team[team.length == 3 ? 2 : 3], MEMBER_WIDTH, y + MEMBER_HEIGHT, MEMBER_WIDTH, MEMBER_HEIGHT);
		}

		parentGraphics.drawImage(canvas, 0, 0, canvasWidth, canvasHeight, null);
	}

	private TeamInfoRenderer()
	{}

}
