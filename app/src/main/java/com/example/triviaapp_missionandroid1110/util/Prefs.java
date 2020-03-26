package com.example.triviaapp_missionandroid1110.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score){
        int currentScore = score;
        int lastScore = sharedPreferences.getInt("high_score",0);
        if(score > lastScore) {
            sharedPreferences.edit().putInt("high_score", currentScore).apply();
        }
    }

    public int getHighScore(){
        return sharedPreferences.getInt("high_score",0);
    }
}
