package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.bank.BankDialog;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class BankActionConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistence.currentDialog != null && Persistence.currentDialog instanceof BankDialog)
		{
			try
			{
				long bag = message.getLong("moneyinbag", 0);
				long bank = message.getLong("moneyinbank", 0);
				((BankDialog) Persistence.currentDialog).onConfirmReceived(bag, bank);
			} catch (Exception e)
			{
				Logger.e("Error reading bankactionresult message: " + e.getMessage());
			}
		}
	}

}
