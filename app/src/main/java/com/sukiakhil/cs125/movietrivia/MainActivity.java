package com.sukiakhil.cs125.movietrivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;



public class MainActivity extends AppCompatActivity {

    //Declares new variable of type TextView to be reference programmatically
    private TextView scoreView;

    //Declares new variable of type TextView to be reference programmatically
    private TextView quoteView;

    //Declares new variable of type EditText to be reference programmatically
    private EditText inputField;

    //Declares new variable of type Buttom to be reference programmatically
    private Button submitButton;

    //Declares new variable of type Button to be reference programmatically
    private Button newQuote;

    //Declares new variable of type TextView to be referenced programmatically
    private TextView answerView;

    //Declares new int to keep track of correct tries
    private static int countCorrect;

    //Declares new int to keep track of number of attempts left
    private static int attemptCount = 5;

    /*Creates string to store the answer in, initializes as blank to prevent NullExceptions in the
    checkAnswers function
     */
    private static String answer = "";

    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Creates new request queue and starts it
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();


        setContentView(R.layout.activity_main);

        //Links variable to object on activity_main by the id
        scoreView = findViewById(R.id.scoreField);
        quoteView = findViewById(R.id.quoteField);
        inputField = findViewById(R.id.guessInput);
        answerView = findViewById(R.id.answerView);

        submitButton = findViewById(R.id.submitButton);

        //Sets the submit button to call the checkAnswer function when clicked
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(inputField.getText().toString(), answer);
            }
        });

        //Links variable to object on activity_main by the id
        newQuote = findViewById(R.id.refreshButton);

        //Sets the new quote button to call the callAPI function when clicked
        newQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
            }
        });

    }

    /**
     * Function that checks answer and determines whether answer if correct or not
     * @param userInput the text entered in the inputField
     * @param answerString the answer that is stored as a static variable to be compared to
     */
    private void checkAnswer(String userInput, String answerString) {
        //Converts both the user input and correct answer to lowercase to avoid casing errors
        String input = userInput.toLowerCase();
        String toCompare = answerString.toLowerCase();

        //Checks if the strings match/the answer is correct
        if (input.equals(toCompare)) {
            //If correct, send the user to the congratulations screen
            Intent intent = new Intent(MainActivity.this, rightAnswer.class);
            startActivity(intent);

            //Increments number of correct and resets the view with the updated score
            countCorrect++;
            String toSet = "Number Correct: " + countCorrect;
            scoreView.setText(toSet);

        } else {
            //If incorrect, send the user to the sorry, try again screen
            Intent intent = new Intent(MainActivity.this, wrongAnswer.class);
            startActivity(intent);

            //If this is the last attempt failed, disable the submit button
            if (attemptCount == 1) {
                submitButton.setEnabled(false);

                //Sets the attempts left field to show the right answer
                String revealAnswer = "The correct answer is: " + answer;
                answerView.setText(revealAnswer);
            }
            else {
                //Decrements the amount of attempts left and resets the countView
                attemptCount--;
                String setNumLeft = "You have " + attemptCount + " attempts left";
                answerView.setText(setNumLeft);
            }

        }

    }


    /**
     * Calls the RapidAPI MoveQuotes API, resets the quote view, and modifies the answer String
     */
    private void callAPI() {

        //Resets attempts left and the attempts field;
        attemptCount = 5;
        String setNumLeft = "You have " + attemptCount + " attempts left";
        answerView.setText(setNumLeft);

        //Makes submit enabled again
        submitButton.setEnabled(true);

        //Resets the inputField to be blank for the next input;
        String emptySet = "";
        inputField.setText(emptySet);

        //JSONArray request to get the object from the API
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://andruxnet-random-famous-quotes.p.mashape.com/?cat=movies",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Attempts to parse the result, logs a statement on failure
                        try {
                            //Retrieves the first Object from the Array response
                            JSONObject quoteObject = response.getJSONObject(0);

                            //Retrieves the quote from the parse object and sets the quoteView
                            String toSetQuote = quoteObject.getString("quote");
                            quoteView.setText(toSetQuote);

                            //Retrieves the answer and sets the static answer string;
                            String answerResult = quoteObject.getString("author");
                            answer = answerResult;

                            //Prints answer for testing app
                            System.out.println(answerResult);

                        } catch (Exception e) {
                            System.out.println("Response succeeded, failed parsing");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Debug statement prints when there are errors in the response
                System.out.println("Error");
                System.out.println(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //Sets the headers required to make the API call to MashAPE
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Mashape-Key", "eCBxXTdlMCmshIFkGfvWrQjJhX3cp11RGmejsnonAlptzRkgEW");
                params.put("Accept", "application/json");
                return params;
            }
        };

        //Adds the request to the queue
        requestQueue.add(jsonArrayRequest);
    }

}

