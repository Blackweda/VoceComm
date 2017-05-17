package com.holdings.siloaman.talktoiea;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
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
        http://stackoverflow.com/questions/14940657/android-speech-recognition-as-a-service-on-android-4-1-4-2/14950616#14950616
        http://stackoverflow.com/questions/11726023/split-string-into-individual-words-java
		
		We should design way for the app to use timer and shut off for a few seconds if the user has accidently pocket dialed the app
		and is not actually saying anything useful for the app.
    */


    public TextView speechScreener;
    public TextView speechTextBack;
    public boolean somethingtoSay = false;

    TextToSpeech textToSpeech;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get rid of the app title in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, I'm IEA. How can I help you?");
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
            calculator();

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

    public void calculator(){

        int finalAnswer = 0;

        String checkTVForMath;
        int wordCount;
        String[] wordDefinitions = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
                                    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
                                    "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety", "hundred", "thousand",
                                    "million", "billion", "trillion", "quadrillion", "quintillion", "sextillion", "septillion", "octillion",
                                    "nonillion", "decillion", "a hundred",
                                    "a thousand", "a million", "a billion", "a trillion", "a quadrillion", "a quintillion", "a sextillion",
                                    "a septillion", "a octillion", "a nonillion", "a decillion"
        };
        String[] wordsNumbered =   {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                                            "19", "20", "30", "40", "50", "60", "70", "80", "90", "100",
                                            "000", "000000", "000000000", "000000000000", "000000000000000", "000000000000000000",
                                            "000000000000000000000", "000000000000000000000000", "000000000000000000000000000",
                                            "000000000000000000000000000000", "000000000000000000000000000000000",
                                            "100", "1000", "1000000", "1000000000", "1000000000000", "1000000000000000",
                                            "1000000000000000000", "1000000000000000000000", "1000000000000000000000000",
                                            "1000000000000000000000000000", "1000000000000000000000000000000",
                                            "1000000000000000000000000000000000"

        };

        checkTVForMath = speechTextBack.getText().toString();

        String[] arrayofWords = checkTVForMath.split("\\W+");       // delimit by Words

        Toast.makeText(MainActivity.this, "Number of words: " + arrayofWords.length, Toast.LENGTH_LONG).show();

        wordCount = arrayofWords.length;
        int[] wordCountArray = new int[wordCount];

        for(int i = 0; i < arrayofWords.length; i++){

            if(arrayofWords[i].equals("+") || arrayofWords[i].equals("plus")){
                wordCountArray[i] = 1;
            }
            else if(arrayofWords[i].equals("-") || arrayofWords[i].equals("minus")){
                wordCountArray[i] = 1;
            }
            else if(arrayofWords[i].equals("*") || arrayofWords[i].equals("times")){
                wordCountArray[i] = 1;
            }
            else if(arrayofWords[i].equals("/") || arrayofWords[i].equals("divided")){
                wordCountArray[i] = 1;
            }
            else
                wordCountArray[i] = 0;
        }

        for(int i = 0; i < arrayofWords.length; i++){

            
        }

    }

}

/*

You can go backwards by either:

Running restore_installed_build in the same tools folder to return it to the currently installed
version Navigating to an older commit with git checkout [revision] .. The revision is a commit hash,
for example for 1.9 it's 189f6d39abf1651454feabd8ed01d371eadf2628 so
git checkout 189f6d39abf1651454feabd8ed01d371eadf2628. should work (notice the dot in the end, it's
important). You can see the commit hashes for releases with
https://github.com/adobe/brackets/releases.
 */
