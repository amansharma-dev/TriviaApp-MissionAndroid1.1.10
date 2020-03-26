package com.example.triviaapp_missionandroid1110;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp_missionandroid1110.data.AnswerListAsync;
import com.example.triviaapp_missionandroid1110.data.QuestionBank;
import com.example.triviaapp_missionandroid1110.model.Question;
import com.example.triviaapp_missionandroid1110.model.Score;
import com.example.triviaapp_missionandroid1110.util.Prefs;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private TextView counterQuestion;
    private TextView showQuestions;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private List<Question> questionList;
    private int currentQuestionIndex =0;
    private TextView currentScore;
    private int currentScoreCounter = 0;
    private Score score;
    private TextView highScore;
//    public static final String SCORE_ID ="score_id";

    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionList = new QuestionBank().getQuestions(new AnswerListAsync() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {



                showQuestions.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                counterQuestion.setText(currentQuestionIndex +" / " +questionArrayList.size());
                Log.d("processFinished", "processFinished: "+questionArrayList);

            }
        });

        counterQuestion = findViewById(R.id.counterQuestions_textView);
        showQuestions = findViewById(R.id.showQuestion_textView);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        currentScore = findViewById(R.id.currentScoreCounter_textView);
        highScore = findViewById(R.id.highScoreCounter_textView);


        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        score = new Score();

        prefs = new Prefs(MainActivity.this);
        Log.d("Save", "onCreate: "+prefs.getHighScore());
        highScore.setText(String.valueOf(prefs.getHighScore()));

        currentQuestionIndex=prefs.getState();
        Log.d("STATE_SAVE", "onCreate: "+prefs.getState());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;

            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.next_button:
                nextQuestion();
//                updateQuestion();
                break;
            case R.id.previous_button:
                if(currentQuestionIndex>0){

                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }


                break;
        }
    }

    private void updateQuestion(){
        String question = questionList.get(currentQuestionIndex).getAnswer();
        counterQuestion.setText(currentQuestionIndex +" / " + questionList.size());
        showQuestions.setText(question);
    }

    private void checkAnswer(boolean userAnswer){
        boolean isAnswerTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toast = 0;
        if(userAnswer == isAnswerTrue){
            addScore();
            Log.d("CurrentScore", "checkAnswer: "+currentScoreCounter);
            fadeAnimation();
            toast = R.string.correctAnswer;
        }
        else{
            subScore();
            shakeAnimation();
            toast = R.string.wrongAnswer; }
            Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_SHORT)
                .show();
    }

    private void shakeAnimation(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        final CardView cardView = findViewById(R.id.questions_cardView);
        cardView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation(){
        final CardView cardView = findViewById(R.id.questions_cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);


        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void nextQuestion(){
        currentQuestionIndex = (currentQuestionIndex +1) % questionList.size();
        updateQuestion();
    }
    private void addScore(){
        currentScoreCounter = currentScoreCounter + 10;
        score.setScore(currentScoreCounter);
        currentScore.setText(String.valueOf(score.getScore()));
        Log.d("Score", "addScore: "+score.getScore());
    }

    private void subScore(){
        currentScoreCounter =  currentScoreCounter - 5;
        score.setScore(currentScoreCounter);
        currentScore.setText(String.valueOf(score.getScore()));
        Log.d("Score", "subScore: "+score.getScore());
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        Log.d("GetScore", "onPause: "+score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}
