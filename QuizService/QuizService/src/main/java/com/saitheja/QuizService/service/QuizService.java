package com.saitheja.QuizService.service;


import com.saitheja.QuizService.dao.QuizDao;
import com.saitheja.QuizService.feign.QuizInterface;
import com.saitheja.QuizService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String categoryName, Integer numOfQuestions, String title) {

        List<Integer> questions= quizInterface.getQuestionsForQuiz(categoryName, numOfQuestions).getBody();
        //call the generate url - Rest Template
//
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz=quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        quizInterface.getQuestionsFromId(questionIds);
        ResponseEntity<List<QuestionWrapper>> questions=quizInterface.getQuestionsFromId(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score= quizInterface.getScore(responses);
        return score;
    }

}
