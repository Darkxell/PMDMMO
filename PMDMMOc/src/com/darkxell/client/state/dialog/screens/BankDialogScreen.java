package com.darkxell.client.state.dialog.screens;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class BankDialogScreen extends OptionDialogScreen
{

	public final Message bag, bank;
	private MenuWindow summaryWindow;

	public BankDialogScreen(PokemonSpecies species, long bagmoney, long bankmoney, Message message, Message... options)
	{
		super(species, message, options);
		this.bag = new Message("dialog.bag.summary").addReplacement("<bag>", Long.toString(bagmoney));
		this.bank = new Message("dialog.bank.summary").addReplacement("<bank>", Long.toString(bankmoney));
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.showingOptions)
		{
			if (this.summaryWindow == null)
			{
				Rectangle dialogBox = this.parentState.dialogBox();
				this.summaryWindow = new MenuWindow(
						new Rectangle(dialogBox.x, 20, dialogBox.width, TextRenderer.height() + OptionSelectionWindow.MARGIN_Y * 2));
			}
			this.summaryWindow.render(g, null, width, height);

			Rectangle inside = this.summaryWindow.inside();
			TextRenderer.render(g, this.bag, inside.x + OptionSelectionWindow.MARGIN_X, inside.y + inside.height / 2 - TextRenderer.height() / 2);
			TextRenderer.render(g, this.bank, inside.x + inside.width / 2, inside.y + inside.height / 2 - TextRenderer.height() / 2);
		}
	}

}
