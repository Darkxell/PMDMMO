package com.darkxell.client.state.menu.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.dungeon.MovesMenuState;
import com.darkxell.client.state.menu.dungeon.MovesMenuState.MoveMenuOption;
import com.darkxell.common.util.language.Message;

public class MoveSelectionWindow extends OptionSelectionWindow
{

	public static final int ppLength = TextRenderer.width("00/00");

	public MoveSelectionWindow(MovesMenuState menu, Rectangle dimensions)
	{
		super(menu, dimensions);
	}

	@Override
	protected void drawOption(Graphics2D g, MenuOption option, int x, int y)
	{
		if (((MoveMenuOption) option).move.pp() == 0) TextRenderer.setColor(Palette.FONT_RED);
		else TextRenderer.setColor(Palette.FONT_GREEN);
		TextRenderer.render(g, option.name, x, y);
		TextRenderer.render(g, ((MoveMenuOption) option).move.pp() + "/" + ((MoveMenuOption) option).move.maxPP(),
				(int) (this.dimensions.getMaxX() - ppLength - MARGIN_X), y);
		TextRenderer.setColor(null);

		if ((this.cursor > 9 || !this.menu.isMain()) && this.menu.currentOption() == option)
			g.drawImage(this.menu.isMain() ? MenuHudSpriteset.SELECTION_ARROW : MenuHudSpriteset.SELECTED_ARROW,
					x - MenuHudSpriteset.SELECTION_ARROW.getWidth() - 4, y + TextRenderer.height() / 2 - MenuHudSpriteset.SELECTION_ARROW.getHeight() / 2,
					null);

		y += TextRenderer.height() + TextRenderer.lineSpacing();
	}

	@Override
	public void render(Graphics2D g, Message name, int width, int height)
	{
		super.render(g, name, width, height);

		int x = MARGIN_X + this.dimensions.x - 1;
		int y = MARGIN_Y + this.dimensions.y + TextRenderer.lineSpacing() / 2;
		MenuOption[] options = this.menu.currentTab().options();
		for (MenuOption option : options)
			if (option != options[options.length - 1])
			{
				y += TextRenderer.height();
				g.setColor(Color.WHITE);
				g.drawLine(x, y, (int) this.dimensions.getMaxX() - MARGIN_X, y);
				g.setColor(Color.BLACK);
				g.drawLine(x, y + 1, (int) this.dimensions.getMaxX() - MARGIN_X, y + 1);
				y += TextRenderer.lineSpacing();
			}
	}

}
