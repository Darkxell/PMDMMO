package com.darkxell.client.state.quiz;

import java.util.Random;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.client.model.quiz.Nature;
import com.darkxell.client.model.quiz.QuizAnswerModel;
import com.darkxell.client.model.quiz.QuizAnswerRewardModel;
import com.darkxell.client.model.quiz.QuizModel;
import com.darkxell.client.model.quiz.QuizQuestionModel;
import com.darkxell.client.model.quiz.QuizModel.QuizGender;
import com.darkxell.client.renderers.layers.BackgroundLsdLayer;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.PlayerLoadingState.PlayerLoadingEndListener;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class PersonalityQuizDialog extends ComplexDialog {
    public static final int GENDER = 0, NATURE_DESC = 1, STARTER = 2, STARTER_CUSTOM = 3, PARTNER = 4, FINAL_DIALOG = 5,
            END = 6;
    private static final int startDialogScreens = 7, endDialogScreens = 3;

    private DialogState currentDialog;
    private int[] currentNature;
    private int currentQuestion;
    private Nature finalNature;
    private QuizGender gender;
    private Pokemon partner;
    private QuizQuestionModel[] questions;
    private QuizModel quizData;
    private Pokemon starter;

    public PersonalityQuizDialog() {
        super(new BackgroundLsdLayer());
        this.quizData = ClientModelIOHandlers.quiz.read(PersonalityQuizDialog.class.getResource("/data/quiz.xml"));
        this.quizData.chooseQuestions(8);
        this.questions = this.quizData.askedQuestions();
        this.currentNature = new int[Nature.values().length];
        this.currentQuestion = -1;
    }

    @Override
    public DialogState firstState() {
        Persistence.soundmanager.setBackgroundMusic(SoundsHolder.getSong("personality-test.mp3"));
        DialogScreen[] screens = new DialogScreen[startDialogScreens];
        for (int i = 0; i < startDialogScreens; ++i)
            screens[i] = new NarratorDialogScreen(new Message("quiz.start_dialog." + i));
        return this.currentDialog = this.newDialog(screens);
    }

    @Override
    public DialogLoadingState getLoadingState() {
        if (this.currentQuestion >= 0 && this.currentQuestion < this.questions.length + END)
            return super.getLoadingState();
        return new DialogLoadingState(this, null);
    }

    @Override
    public ComplexDialogAction nextAction(DialogState previous) {
        if (this.currentQuestion == this.questions.length + FINAL_DIALOG)
            return ComplexDialogAction.PAUSE;
        else
            return ComplexDialogAction.NEW_DIALOG;
    }

    @Override
    public DialogState nextState(DialogState previous) {
        this.currentDialog = null;
        if (this.currentQuestion == this.questions.length + FINAL_DIALOG) {
            DialogScreen[] screens = new DialogScreen[endDialogScreens];
            for (int i = 0; i < endDialogScreens; ++i)
                screens[i] = new DialogScreen(new Message("quiz.end." + i));
            this.currentDialog = this.newDialog(screens);
        } else if (this.currentQuestion == this.questions.length + PARTNER)
            this.currentDialog = this.newDialog(new StarterChoiceScreen(this, true));
        else if (this.currentQuestion == this.questions.length + STARTER_CUSTOM)
            this.currentDialog = this.newDialog(new StarterChoiceScreen(this, false));
        else if (this.currentQuestion == this.questions.length + STARTER)
            this.currentDialog = this.newDialog(new StarterScreen(
                    new Message("quiz.starter").addReplacement("<pokemon>", this.starter.getNickname()), this.starter));
        else if (this.currentQuestion == this.questions.length + NATURE_DESC)
            this.currentDialog = this.newDialog(this.finalNature.description());
        else if (this.currentQuestion == this.questions.length + GENDER)
            this.currentDialog = this.newDialog(new OptionDialogScreen(new Message("quiz.gender"),
                    new Message("quiz.gender.boy"), new Message("quiz.gender.girl")));
        else if (this.currentQuestion >= 0 && this.currentQuestion < this.questions.length)
            this.currentDialog = this.newDialog(new OptionDialogScreen(this.questions[this.currentQuestion].name(),
                    this.questions[this.currentQuestion].options()));

        return this.currentDialog.setOpaque(true);
    }

    @Override
    protected void onDialogFinished(DialogState dialog) {
        DialogScreen screen = dialog.currentScreen();

        if (this.currentQuestion >= 0 && this.currentQuestion < this.questions.length) {
            QuizAnswerModel answer = this.questions[this.currentQuestion].answers
                    .get(((OptionDialogScreen) screen).chosenIndex());
            for (QuizAnswerRewardModel reward : answer.rewards)
                this.currentNature[reward.nature.id] += reward.points;
        }

        ++this.currentQuestion;

        if (this.currentQuestion == this.questions.length) {
            this.finalNature = Nature.Brave;
            for (int i = 0; i < this.currentNature.length; ++i)
                if (this.currentNature[this.finalNature.id] < this.currentNature[i])
                    this.finalNature = Nature.get(i);
        } else if (this.currentQuestion == this.questions.length + NATURE_DESC) {
            this.gender = ((OptionDialogScreen) screen).chosenIndex() == 0 ? QuizGender.Boy : QuizGender.Girl;
            int starterID = this.quizData.getStarter(this.finalNature, this.gender);
            this.starter = Registries.species().find(starterID).generate(new Random(), 5);
        } else if (this.currentQuestion == this.questions.length + STARTER_CUSTOM) {
            int index = ((OptionDialogScreen) screen).chosenIndex();
            if (index == 0)
                ++this.currentQuestion;
        } else if (this.currentQuestion == this.questions.length + PARTNER) {
            int index = ((OptionDialogScreen) screen).chosenIndex();
            this.starter = this.partners(false)[index].generate(new Random(), 5);
        } else if (this.currentQuestion == this.questions.length + FINAL_DIALOG) {
            int index = ((OptionDialogScreen) screen).chosenIndex();
            this.partner = this.partners(true)[index].generate(new Random(), 5);
            this.sendTestResults(this.starter.species().getID(), this.partner.species().getID(), this.starter.gender(),
                    this.partner.gender());
        }
    }

    @Override
    public AbstractState onFinish(DialogState lastState) {
        return null;
    }

    public void onResultConfirmed() {
        Persistence.stateManager
                .setState(new PlayerLoadingState(Persistence.player.getData().id, new PlayerLoadingEndListener() {
                }));
    }

    public PokemonSpecies[] partners(boolean limitChoices) {
        if (!limitChoices)
            return this.quizData.starters();
        return this.quizData.validPartners(this.starter.species().getID());
    }

    public Message[] partnersAsOptions(boolean limitChoices) {
        PokemonSpecies[] partners = this.partners(limitChoices);
        Message[] options = new Message[partners.length];
        for (int i = 0; i < options.length; ++i)
            options[i] = partners[i].speciesName();
        return options;
    }

    private void sendTestResults(int id1, int id2, int gender1, int gender2) {
        JsonObject tosend = Json.object().add("action", "testresult").add("mainid", id1).add("offid", id2)
                .add("maingender", gender1).add("offgender", gender2);
        Persistence.socketendpoint.sendMessage(tosend.toString());
        Logger.i("Chosen pokemons " + id1 + " and " + id2 + ", sending to server.");
    }

}
