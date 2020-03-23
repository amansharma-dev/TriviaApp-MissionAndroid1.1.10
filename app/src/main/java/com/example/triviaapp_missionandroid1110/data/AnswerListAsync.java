package com.example.triviaapp_missionandroid1110.data;

import com.example.triviaapp_missionandroid1110.model.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface AnswerListAsync {
    void processFinished(ArrayList<Question> questionArrayList);
}
