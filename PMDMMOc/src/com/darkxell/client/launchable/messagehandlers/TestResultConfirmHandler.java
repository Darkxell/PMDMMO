package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.quiz.PersonalityQuizDialog;
import com.eclipsesource.json.JsonObject;

public class TestResultConfirmHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        ComplexDialog dialog = Persistence.currentDialog;
        if (dialog instanceof PersonalityQuizDialog)
            ((PersonalityQuizDialog) dialog).onResultConfirmed();
    }

}
