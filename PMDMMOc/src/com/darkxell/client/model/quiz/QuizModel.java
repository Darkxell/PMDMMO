package com.darkxell.client.model.quiz;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

@XmlRootElement(name = "quiz")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizModel {

    public enum QuizGender {
        Boy(0),
        Girl(1);

        public final int id;

        QuizGender(int id) {
            this.id = id;
        }

        public Message getName() {
            return new Message("quiz.gender." + this.name().toLowerCase());
        }
    }

    /** Lists the available question groups. */
    @XmlElement(name = "questiongroup")
    @XmlElementWrapper(name = "questions")
    public ArrayList<QuizQuestionGroupModel> questionGroups;

    @XmlElement(name = "partner")
    @XmlElementWrapper(name = "partners")
    private ArrayList<QuizPartnerChoiceModel> partners;

    /** Associates the pair (natureID, genderID) to the starter ID. */
    @XmlElement(name = "starter")
    @XmlElementWrapper(name = "starters")
    public ArrayList<QuizStarterModel> starters;

    @XmlTransient
    private QuizQuestionModel[] askedQuestions;

    public QuizModel() {
    }

    public QuizQuestionModel[] askedQuestions() {
        if (this.askedQuestions == null)
            return null;
        return this.askedQuestions.clone();
    }

    /** Chooses random questions to be asked. */
    public void chooseQuestions(int questionCount) {
        Random r = new Random();
        if (questionCount > this.questionGroups.size()) {
            Logger.e("Question count (" + questionCount + ") > question group count (" + this.questionGroups.size()
                    + ")! Defaults back to group count.");
            questionCount = this.questionGroups.size();
        }

        ArrayList<Integer> remainingGroups = new ArrayList<>();
        for (int i = 0; i < this.questionGroups.size(); ++i)
            remainingGroups.add(i);
        this.askedQuestions = new QuizQuestionModel[questionCount];

        for (int question = 0; question < questionCount; ++question) {
            int chosen = r.nextInt(remainingGroups.size());
            QuizQuestionGroupModel group = this.questionGroups.get(remainingGroups.get(chosen));
            remainingGroups.remove(chosen);
            this.askedQuestions[question] = group.questions.get(r.nextInt(group.questions.size()));
        }
    }

    public PokemonSpecies[] starters() {
        ArrayList<PokemonSpecies> starters = new ArrayList<>();

        for (QuizStarterModel s : this.starters) {
            PokemonSpecies p = Registries.species().find(s.getPokemonID());
            if (!starters.contains(p))
                starters.add(p);
        }

        starters.sort(Comparator.naturalOrder());

        return starters.toArray(new PokemonSpecies[0]);
    }

    public PokemonSpecies[] validPartners(int choice) {
        ArrayList<PokemonSpecies> partners = new ArrayList<>();
        for (QuizPartnerChoiceModel partner : this.partners)
            if (partner.isValid(choice))
                partners.add(Registries.species().find(partner.getID()));
        return partners.toArray(new PokemonSpecies[0]);
    }

    public int getStarter(Nature nature, QuizGender gender) {
        for (QuizStarterModel starter : this.starters)
            if (starter.matches(nature, gender))
                return starter.getPokemonID();
        Logger.e("No starter found for " + nature + " " + gender);
        return 0;
    }

}
