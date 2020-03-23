package com.example.triviaapp_missionandroid1110.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp_missionandroid1110.controller.AppController;
import com.example.triviaapp_missionandroid1110.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    private ArrayList<Question> questionArrayList = new ArrayList<>();

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";


    //function
    public List<Question> getQuestions(final AnswerListAsync callback){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Json", "onResponse: "+response);

                        for(int i = 0; i<response.length(); i++){
                            try {

                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).getString(0));
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                questionArrayList.add(question);
                             //   Log.d("getQuestion", "onResponse: "+question);

                                //Log.d("jsonindex", "onResponse: "+response.getJSONArray(i).get(0));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(callback !=null){
                            callback.processFinished(questionArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: "+error.toString());


            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }


}
