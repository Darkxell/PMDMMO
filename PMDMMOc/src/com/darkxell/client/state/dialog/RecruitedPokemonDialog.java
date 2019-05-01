package com.darkxell.client.state.dialog;

import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class RecruitedPokemonDialog extends ComplexDialog {

	private enum RecruitedPokemonDialogIndex {
		ASK_NICKNAME, NICKNAME, RECRUITED;
	}

	public final boolean askForConfirm;
	private RecruitedPokemonDialogIndex index;
	public DialogEndListener onDialogEnd;
	public final DungeonPokemon recruit;

	public RecruitedPokemonDialog(AbstractGraphicsLayer background, DungeonPokemon recruit, boolean askForConfirm,
			DialogEndListener onDialogEnd) {
		super(background);
		this.recruit = recruit;
		this.askForConfirm = askForConfirm;
		this.onDialogEnd = onDialogEnd;
	}

	@Override
	public DialogState firstState() {
		return this.newDialog(new DialogScreen(
				new Message("recruit.recruited").addReplacement("<pokemon>", this.recruit.getNickname())));
	}

	@Override
	public ComplexDialogAction nextAction(DialogState previous) {
		// TODO Auto-generated method stub
		return ComplexDialogAction.TERMINATE;
	}

	@Override
	public DialogState nextState(DialogState previous) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onDialogFinished(DialogState dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public AbstractState onFinish(DialogState lastState) {
		this.onDialogEnd.onDialogEnd(lastState);
		return null;
	}

}
