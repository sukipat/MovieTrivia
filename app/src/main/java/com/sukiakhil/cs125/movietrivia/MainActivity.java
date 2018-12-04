package com.sukiakhil.cs125.movietrivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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

    //Declares new int to keep track of correct tries
    private static int countCorrect;

    /*Creates string to store the answer in, initializes as blank to prevent NullExceptions in the
    checkAnswers function
     */
    private static String answer = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Links variable to object on activity_main by the id
        scoreView = findViewById(R.id.scoreField);
        quoteView = findViewById(R.id.quoteField);
        inputField = findViewById(R.id.guessInput);

        submitButton = findViewById(R.id.submitButton);

        //Sets the submit button to call the checkAnswer function when clicked
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(inputField.getText().toString(), answer);
            }
        });

        newQuote = findViewById(R.id.refreshButton);
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
        }

    }


}
