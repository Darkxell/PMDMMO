package com.darkxell.client.model.io;

import com.darkxell.client.model.quiz.QuizAnswerModel;
import com.darkxell.client.model.quiz.QuizModel;
import com.darkxell.client.model.quiz.QuizQuestionGroupModel;
import com.darkxell.client.model.quiz.QuizQuestionModel;
import com.darkxell.common.model.io.ModelIOHandler;

public class QuizModelIOHandler extends ModelIOHandler<QuizModel> {

    public QuizModelIOHandler() {
        super(QuizModel.class);
    }

    @Override
    protected QuizModel handleAfterImport(QuizModel object) {

        for (QuizQuestionGroupModel group : object.questionGroups)
            for (QuizQuestionModel question : group.questions) {
                question.questionGroupID = group.getID();
                for (QuizAnswerModel answer : question.answers) {
                    answer.questionGroupID = group.getID();
                    answer.questionID = question.getID();
                }
            }

        return super.handleAfterImport(object);
    }

}
