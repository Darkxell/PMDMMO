package com.darkxell.client.state.quiz;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class QuizData {

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

    private QuizQuestion[] askedQuestions;
    private PartnerChoice[] partners;
    /** Lists the available question groups. */
    public final QuizQuestionGroup[] questionGroups;
    /** Associates the pair (natureID, genderID) to the starter ID. */
    public final int[][] starters;

    public QuizData() {
        Element xml = XMLUtils.read(PokemonRegistry.class.getResourceAsStream("/data/quiz.xml"));
        List<Element> groups = xml.getChild("questions", xml.getNamespace()).getChildren("questiongroup",
                xml.getNamespace());
        this.questionGroups = new QuizQuestionGroup[groups.size()];
        for (int i = 0; i < this.questionGroups.length; ++i)
            this.questionGroups[i] = new QuizQuestionGroup(groups.get(i));

        List<Element> starters = xml.getChild("starters", xml.getNamespace()).getChildren("starter",
                xml.getNamespace());
        this.starters = new int[Nature.values().length][2];
        for (Element starter : starters) {
            int nature = Nature.valueOf(XMLUtils.getAttribute(starter, "nature", Nature.Brave.name())).id;
            int gender = QuizGender.valueOf(XMLUtils.getAttribute(starter, "gender", QuizGender.Boy.name())).id;
            this.starters[nature][gender] = XMLUtils.getAttribute(starter, "pokemon", 1);
        }

        List<Element> partners = xml.getChild("partners", xml.getNamespace()).getChildren("partner",
                xml.getNamespace());
        this.partners = new PartnerChoice[partners.size()];
        for (int i = 0; i < this.partners.length; ++i)
            this.partners[i] = new PartnerChoice(partners.get(i));
    }

    public QuizQuestion[] askedQuestions() {
        if (this.askedQuestions == null)
            return null;
        return this.askedQuestions.clone();
    }

    /** Chooses random questions to be asked. */
    public void chooseQuestions(int questionCount) {
        Random r = new Random();
        if (questionCount > this.questionGroups.length) {
            Logger.e("Question count (" + questionCount + ") > question group count (" + this.questionGroups.length
                    + ")! Defaults back to group count.");
            questionCount = this.questionGroups.length;
        }

        ArrayList<Integer> remainingGroups = new ArrayList<>();
        for (int i = 0; i < this.questionGroups.length; ++i)
            remainingGroups.add(i);
        this.askedQuestions = new QuizQuestion[questionCount];

        for (int question = 0; question < questionCount; ++question) {
            int chosen = r.nextInt(remainingGroups.size());
            QuizQuestionGroup group = this.questionGroups[remainingGroups.get(chosen)];
            remainingGroups.remove(chosen);
            this.askedQuestions[question] = group.questions[r.nextInt(group.questions.length)];
        }
    }

    public PokemonSpecies[] starters() {
        ArrayList<PokemonSpecies> starters = new ArrayList<>();

        for (int[] s : this.starters)
            for (int id : s) {
                PokemonSpecies p = Registries.species().find(id);
                if (!starters.contains(p))
                    starters.add(p);
            }

        starters.sort(Comparator.naturalOrder());

        return starters.toArray(new PokemonSpecies[0]);
    }

    public PokemonSpecies[] validPartners(int choice) {
        ArrayList<PokemonSpecies> partners = new ArrayList<>();
        for (PartnerChoice partner : this.partners)
            if (partner.isValid(choice))
                partners.add(Registries.species().find(partner.id));
        return partners.toArray(new PokemonSpecies[0]);
    }

}
