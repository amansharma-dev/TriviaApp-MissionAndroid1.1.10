package com.example.triviaapp_missionandroid1110;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.triviaapp_missionandroid1110.data.AnswerListAsync;
import com.example.triviaapp_missionandroid1110.data.QuestionBank;
import com.example.triviaapp_missionandroid1110.model.Question;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Question> questionList = new QuestionBank().getQuestions(new AnswerListAsync() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                Log.d("processFinished", "processFinished: "+questionArrayList);

            }
        });
        //Log.d("questionList", "onCreate: ."+questionList);

    }
}
