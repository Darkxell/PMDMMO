package com.darkxell.client.state.dialog.bank;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class BankDialogScreen extends OptionDialogScreen
{

	public final long bag, bank;
	private BankMenuWindow summaryWindow;

	public BankDialogScreen(PokemonSpecies species, long bagmoney, long bankmoney, Message message, Message... options)
	{
		super(species, message, DialogPortraitLocation.BOTTOM_LEFT, options);
		this.bag = bagmoney;
		this.bank = bankmoney;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.showingOptions)
		{
			if (this.summaryWindow == null)
			{
				Rectangle dialog = this.parentState.dialogBox();
				this.summaryWindow = new BankMenuWindow(new Rectangle(dialog.x, 20, dialog.width, TextRenderer.height() + OptionSelectionWindow.MARGIN_Y * 2),
						this.bag, this.bank);
			}
			this.summaryWindow.render(g, null, width, height);
		}
	}

}
