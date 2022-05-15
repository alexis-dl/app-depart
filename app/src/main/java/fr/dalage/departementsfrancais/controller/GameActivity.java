package fr.dalage.departementsfrancais.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import fr.dalage.departementsfrancais.R;
import fr.dalage.departementsfrancais.model.Question;
import fr.dalage.departementsfrancais.model.QuestionBank;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mTextView;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    QuestionBank mQuestionBank = generateQuestions();
    Question mCurrentQuestion;
    private int mRemainingQuestionCount;
    private int mScore;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        mTextView=findViewById(R.id.game_activity_textview_question);
        b1=findViewById(R.id.game_activity_button_1);
        b2=findViewById(R.id.game_activity_button_2);
        b3=findViewById(R.id.game_activity_button_3);
        b4=findViewById(R.id.game_activity_button_4);

        // Use the same listener for the four buttons.
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

        mCurrentQuestion= mQuestionBank.getCurrentQuestion();
        displayQuestion(mCurrentQuestion);
        mRemainingQuestionCount=3;
        mScore=0;
    }
    private void displayQuestion(final Question question) {
        mTextView.setText(question.getQuestion());
        b1.setText(question.getChoiceList().get(0));
        b2.setText(question.getChoiceList().get(1));
        b3.setText(question.getChoiceList().get(2));
        b4.setText(question.getChoiceList().get(3));
    }
    private QuestionBank generateQuestions(){
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3));

    }


    @Override
    public void onClick(View v) {
        int index;

        if (v == b1) {
            index = 0;
        } else if (v == b2) {
            index = 1;
        } else if (v == b3) {
            index = 2;
        } else if (v == b4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

        if(index==mQuestionBank.getCurrentQuestion().getAnswerIndex()){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(this, "Faux!", Toast.LENGTH_SHORT).show();
        }
        mRemainingQuestionCount--;

        if (mRemainingQuestionCount > 0) {
            mCurrentQuestion = mQuestionBank.getNextQuestion();
            displayQuestion(mCurrentQuestion);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Well done!")
                    .setMessage("Your score is " + mScore)
                    .setPositiveButton("OK", (dialog, which) -> {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE,mScore);
                        setResult(RESULT_OK,intent);
                        finish();
                    })
                    .create()
                    .show();
        }

    }
}