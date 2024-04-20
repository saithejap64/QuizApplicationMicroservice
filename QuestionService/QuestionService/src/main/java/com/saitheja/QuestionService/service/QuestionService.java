package com.saitheja.QuestionService.service;


import com.saitheja.QuestionService.dao.QuestionsDao;
import com.saitheja.QuestionService.model.Question;
import com.saitheja.QuestionService.model.QuestionWrapper;
import com.saitheja.QuestionService.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionsDao questionsDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
            return new ResponseEntity<>(questionsDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionsDao.findByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionsDao.save(question);
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        }
        catch (Exception e){
            e.getMessage();
        }
        return new ResponseEntity<>("UNSUCCESSFUL", HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<String> updateQuestion(Question question) {
        questionsDao.save(question);
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    public String deleteQuestionById(int id) {
        questionsDao.deleteById(id);
        return "DELETED";
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions=questionsDao.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers= new ArrayList<>();
        List<Question> questions=new ArrayList<>();

        for (Integer id: questionIds){
            questions.add(questionsDao.findById(id).get());
        }

        for (Question question: questions){
            QuestionWrapper questionWrapper=new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestionTitle(question.getQuestionTitle());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(question.getOption4());
            wrappers.add(questionWrapper);
        }
        return new ResponseEntity<>(wrappers, HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int right=0;
        for (Response response: responses){
            Question question=questionsDao.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                right++;

        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
