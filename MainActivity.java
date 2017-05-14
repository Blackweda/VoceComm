package com.holdings.siloaman.voicecomm;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    /*
    SOURCES:
        https://developer.android.com/reference/android/speech/SpeechRecognizer.html
        https://www.youtube.com/watch?v=VazSEtXHDcI
        https://youtu.be/nzkrRQgCEmE
        https://youtu.be/5tWWEAeuZTs
        
        http://stackoverflow.com/questions/16219601/how-to-make-google-voice-run-continually-in-the-background-on-android
        protosmartiea?
    */

    // SPEECH TO TEXT VARIABLES
    public TextView speechScreener;
    public TextView speechTextBack;
    public boolean somethingtoSay = false;
    
    // TEXT TO SPEECH VARIABLES
    TextToSpeech textToSpeech;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechTextBack = (TextView)findViewById(R.id.speechTextBack);
        Button startListeningButton = (Button)findViewById(R.id.startListeningButton);
        textToSpeech = new TextToSpeech(MainActivity.this, MainActivity.this);
        final Button repeatBackButton = (Button)findViewById(R.id.repeatBackButton);

        // SPEECH TO TEXT HANDLER BUTTON
        startListeningButton.setOnClickListener(new View.OnClickListener() {        

            public void onClick(View v){

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // Specify free form input
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, I'm Annie. How can I help you?");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 2);
            }
        });

        // TEXT TO SPEECH HANDLER BUTTON
        repeatBackButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(!textToSpeech.isSpeaking()){
                    HashMap<String, String> stringStringHashMap = new HashMap<String, String>();
                    stringStringHashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "Hello, how are you?");
                    textToSpeech.speak(speechTextBack.getText().toString(), textToSpeech.QUEUE_ADD, stringStringHashMap);
                    repeatBackButton.setVisibility(Button.GONE);
                }
                else{
                    textToSpeech.stop();
                }
            }
        });

    }


    // SPEECH TO TEXT ASSOCIATED FUNCTION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            // for other listeners or buttons
        }

        if(requestCode == 2){

            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechTextBack.setText(results.get(0));

        }

    }

    // TEXT TO SPEECH ASSOCIATED FUNCTION
    @Override
    public void onInit(int i) {

        textToSpeech.setOnUtteranceCompletedListener(this);
    }

    // TEXT TO SPEECH ASSOCIATED FUNCTION
    @Override
    public void onUtteranceCompleted(String s) {

        runOnUiThread(new Runnable(){

            @Override
            public void run(){

                Toast.makeText(MainActivity.this, "Utterance Completed", Toast.LENGTH_LONG).show();
                Button button = (Button)findViewById(R.id.repeatBackButton);
                button.setVisibility(Button.VISIBLE);
            }

        });{

        };
    }

    // TEXT TO SPEECH ASSOCIATED FUNCTION
    protected void onDestroy(){

        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }

        super.onDestroy();

    }
}


