package com.holdings.siloaman.speechtotext;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /*
    SOURCES:
        https://developer.android.com/reference/android/speech/SpeechRecognizer.html
        https://www.youtube.com/watch?v=VazSEtXHDcI
        https://youtu.be/nzkrRQgCEmE
    */


    public TextView speechScreener;
    public TextView speechTextBack;
    public boolean somethingtoSay = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechTextBack = (TextView)findViewById(R.id.speechTextBack);
        Button startListeningButton = (Button)findViewById(R.id.startListeningButton);

        startListeningButton.setOnClickListener(new View.OnClickListener() {        

            public void onClick(View v){

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // Specify free form input
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, I'm IEA. How can I help you?");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 2);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            // for other listeners or buttons
        }

        if(requestCode == 2){

            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //Toast.makeText(this, results.get(0), Toast.LENGTH_SHORT.show();

            //String parseText;

            //speechScreener.setText(results.get(0));

            // ATTEMPT TO MAKE TEXTVIEW INVISIBLE WHILE 'DECIDING' WHAT TO SHOW
            //speechTextBack.setalpha(0.0f);

            speechTextBack.setText(results.get(0));
            //parseText = speechScreener.getText().toString();

            /*
            if(parseText.contentEquals("voicecomm") && somethingtoSay == false){
                somethingtoSay = true;
                speechTextBack.setText("Listening Now");
            }

            else if(somethingtoSay && !parseText.contains("voicecomm")) {
                speechTextBack.setText(results.get(0));
                somethingtoSay = false;
            }
            */

        }

    }
}


