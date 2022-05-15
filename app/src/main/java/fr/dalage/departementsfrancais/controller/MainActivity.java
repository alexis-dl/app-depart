package fr.dalage.departementsfrancais.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fr.dalage.departementsfrancais.R;

public class MainActivity extends AppCompatActivity {

    EditText mEditText;
    Button mPlayButton;
    ImageView mImageView;
    TextView mHomePageText;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("TAG", "onStart() called");

        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.edit_text);
        mPlayButton = findViewById(R.id.button);
        mImageView = findViewById(R.id.imageView);
        mHomePageText = findViewById(R.id.HomePageText);

        mPlayButton.setEnabled(false);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This is where we'll check the user input
                mPlayButton.setEnabled(!editable.toString().isEmpty());
            }
        });

        mPlayButton.setOnClickListener(view -> {
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putString(SHARED_PREF_USER_INFO_NAME, mEditText.getText().toString())
                    .apply();
            Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivityForResult(gameActivityIntent,GAME_ACTIVITY_REQUEST_CODE);
        });
        greetUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            //put the score into the Shared Preferences
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();
        }
        greetUser();

    }
    private void greetUser() {
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1);

        if (firstName != null) {
            if (score != -1) {
                mHomePageText.setText(getString(R.string.welcome_back_with_score, firstName, score));
            } else {
                mHomePageText.setText(getString(R.string.welcome_back, firstName));
            }

            mEditText.setText(firstName);
            mEditText.setSelection(firstName.length());
        }
    }
}