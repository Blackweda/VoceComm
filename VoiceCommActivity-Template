package com.holdings.siloaman.talktoiea;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class VoiceCommActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    /*
    SOURCES:
        https://developer.android.com/reference/android/speech/SpeechRecognizer.html
        https://www.youtube.com/watch?v=VazSEtXHDcI
        https://youtu.be/nzkrRQgCEmE
        https://youtu.be/5tWWEAeuZTs

        http://stackoverflow.com/questions/16219601/how-to-make-google-voice-run-continually-in-the-background-on-android
        http://stackoverflow.com/questions/14940657/android-speech-recognition-as-a-service-on-android-4-1-4-2/14950616#14950616
        http://stackoverflow.com/questions/11726023/split-string-into-individual-words-java
		
		http://stackoverflow.com/questions/14371092/how-to-make-a-specific-text-on-textview-bold
		http://stackoverflow.com/questions/1987673/substring-search-in-java
		
		We should design way for the app to use timer and shut off for a few seconds if the user has accidently pocket dialed the app
		and is not actually saying anything useful for the app.
    */


    public TextView speechScreener;
    public TextView speechTextBack;
    public boolean somethingtoSay = false;

    public enum ablautReduplication { Opinion, Size, Age, Shape, Colour, Origin, Material, Purpose, Noun };

    TextToSpeech textToSpeech;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicecomm);

        // Get rid of the app title in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


        speechTextBack = (TextView)findViewById(R.id.speechTextBack);
        Button startListeningButton = (Button)findViewById(R.id.startListeningButton);
        textToSpeech = new TextToSpeech(VoiceCommActivity.this, VoiceCommActivity.this);
        final Button repeatBackButton = (Button)findViewById(R.id.repeatBackButton);

        // SPEECH TO TEXT HANDLER BUTTON
        startListeningButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // Specify free form input
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, I'm IEA. How can I help you?");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
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
            //calculator();
            catchKeyWordsSingles();
            //testWord();
			
			/* HIGHLIGHT(BOLD) KEY WORDS IN TEXT
			final SpannableStringBuilder str = new SpannableStringBuilder("Your awesome text");
			str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			TextView tv=new TextView(context);
			tv.setText(str);
			*/

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

                //Toast.makeText(VoiceCommActivity.this, "Utterance Completed", Toast.LENGTH_LONG).show();
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

    // Logic for understanding numbers in both numeral and alphabetic formats
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

        Toast.makeText(VoiceCommActivity.this, "Number of words: " + arrayofWords.length, Toast.LENGTH_LONG).show();

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

    public void catchKeyWordsSingles(){

        String parsibleString;

        parsibleString = speechTextBack.getText().toString();

        String[] arrayofWords = parsibleString.split("\\W+");       // delimit by Words

        //Toast.makeText(VoiceCommActivity.this, "Number of words: " + arrayofWords.length, Toast.LENGTH_LONG).show();


        String voiceCommand = "voicecomm"; String helpCommand = "help";
        String callCommand = "call"; String contactCommand = "contact";
        String explainCommand = "explain"; String routeCommand = "route";
        String cardinalCommand = "cardinal"; String directionCommand = "direction";
        String whereCommand = "where"; String amCommand = "am"; String iCommand = "I";
        String giveCommand = "give"; String meCommand = "me"; String directionsCommand = "directions";
        String stepCommand = "step"; String byCommand = "by"; String stepByStepCommand = "step-by-step";
        String whatCommand = "what"; String isCommand = "is"; String myCommand = "my"; String locationCommand = "location";
        String howCommand = "how"; String farCommand = "far"; String fromCommand = "from"; String checkPointCommand = "checkpoint";

        for (int i = 0; i < arrayofWords.length; i++){

            int WordPlace = i+1;        // Show the Human understandable word placement rather than the array element placement

            // HELP COMMAND
            if(parsibleString.contains("voicecomm help") || parsibleString.contains("Voicecomm help"))
            if(arrayofWords[i].equalsIgnoreCase(voiceCommand)){

                Toast.makeText(VoiceCommActivity.this, "Voicecomm Help Command Heard At Word #: " + WordPlace + " and " + (WordPlace+1), Toast.LENGTH_LONG).show();
            }

            // CARDINAL DIRECTION COMMAND
            if(parsibleString.contains("cardinal direction") || parsibleString.contains("Cardinal direction")){

                if(arrayofWords[i].equalsIgnoreCase(cardinalCommand)){

                    Toast.makeText(VoiceCommActivity.this, "Cardinal Direction Command Heard At Word #: " + WordPlace + " and " + (WordPlace+1), Toast.LENGTH_LONG).show();
                }
            }

            // STEP-BY-STEP COMMAND   [ this function gives issues the most!]
            if(parsibleString.contains("step by step") || parsibleString.contains("Step by step") ||
                    parsibleString.contains("step-by-step") || parsibleString.contains("Step-by-step")){

                int wordLength = arrayofWords.length;

                if(i < (wordLength-1)) {     // prevent the 2nd 'step' from accessing garbage memory access with arrayofWords[i+n]

                    if (arrayofWords[i].equalsIgnoreCase(stepCommand) && arrayofWords[i+1].equalsIgnoreCase(byCommand) && arrayofWords[i+2].equalsIgnoreCase(stepCommand)){

                        Toast.makeText(VoiceCommActivity.this, "Step By Step Command Heard At Word #: " + WordPlace + " to " + (WordPlace+2), Toast.LENGTH_LONG).show();
                    }
                }
            }

            // WHERE AM I? COMMAND
            if(parsibleString.contains("where am I") || parsibleString.contains("Where am I")
	      || parsibleString.contains("where am i") || parsibleString.contains("Where am i")){

                if(arrayofWords[i].equalsIgnoreCase(whereCommand) && arrayofWords[i+1].equalsIgnoreCase(amCommand) && arrayofWords[i+2].equalsIgnoreCase(iCommand)){

                    Toast.makeText(VoiceCommActivity.this, "Where Am I Command Heard At Word #: " + WordPlace + " to " + (WordPlace+2), Toast.LENGTH_LONG).show();
                }
            }

            // GIVE ME DIRECTIONS COMMAND
            if(parsibleString.contains("give me directions") || parsibleString.contains("Give me directions")){

                if(arrayofWords[i].equalsIgnoreCase(giveCommand) && arrayofWords[i+1].equalsIgnoreCase(meCommand) && arrayofWords[i+2].equalsIgnoreCase(directionsCommand)){

                    Toast.makeText(VoiceCommActivity.this, "Give Me Directions Command Heard At Word #: " + WordPlace + " to " + (WordPlace+2), Toast.LENGTH_LONG).show();
                }
            }

            // CONTACT COMMAND
            if(parsibleString.contains("call contact") || parsibleString.contains("Call contact")){

                if(arrayofWords[i].equalsIgnoreCase(callCommand) && arrayofWords[i+1].equalsIgnoreCase(contactCommand)){

                    Toast.makeText(VoiceCommActivity.this, "Call Contact Command Heard At Word #: " + WordPlace + " to " + (WordPlace+1), Toast.LENGTH_LONG).show();
                }
            }

            // EXPLAIN ROUTE COMMAND
            if(parsibleString.contains("explain route") || parsibleString.contains("Explain route")){

                if(arrayofWords[i].equalsIgnoreCase(explainCommand) && arrayofWords[i+1].equalsIgnoreCase(routeCommand)){

                    Toast.makeText(VoiceCommActivity.this, "Explain Route Command Heard At Word #: " + WordPlace + " to " + (WordPlace+1), Toast.LENGTH_LONG).show();
                }
            }

            // HOW FAR AM I FROM CHECKPOINT COMMAND
            if(parsibleString.contains("how far am I from checkpoint") || parsibleString.contains("How far am I from checkpoint")
	      		|| parsibleString.contains("how far am i from checkpoint" || parsibleString.contains("How far am i from checkpoint")){

                if(arrayofWords[i].equalsIgnoreCase(howCommand)
                        && arrayofWords[i+1].equalsIgnoreCase(farCommand)
                        && arrayofWords[i+2].equalsIgnoreCase(amCommand)
                        && arrayofWords[i+3].equalsIgnoreCase(iCommand)
                        && arrayofWords[i+4].equalsIgnoreCase(fromCommand)
                        && arrayofWords[i+5].equalsIgnoreCase(checkPointCommand)){

                    Toast.makeText(VoiceCommActivity.this, "How Far Am I From Checkpoint Command Heard At Word #: " + WordPlace + " to " + (WordPlace+5), Toast.LENGTH_LONG).show();
                }
            }

            // WHAT IS MY LOCATION COMMAND
            if(parsibleString.contains("what is my location") || parsibleString.contains("What is my location")){

                if(arrayofWords[i].equalsIgnoreCase(whatCommand) && arrayofWords[i+1].equalsIgnoreCase(isCommand)
                        && arrayofWords[i+2].equalsIgnoreCase(myCommand) && arrayofWords[i+3].equalsIgnoreCase(locationCommand)){

                    Toast.makeText(VoiceCommActivity.this, "What Is My Location Command Heard At Word #: " + WordPlace + " to " + (WordPlace+3), Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    public void testWord(){

        String testWord = "step-by-step";
        String[] wordSplit = testWord.split("\\W+");
        Toast.makeText(VoiceCommActivity.this, "Testing Word: " + testWord, Toast.LENGTH_LONG).show();
        for(int j = 0; j < wordSplit.length; j++){
            Toast.makeText(VoiceCommActivity.this, "Word #" + j + ": " + wordSplit[j], Toast.LENGTH_LONG).show();
        }
    }

    // Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.voice_comm_menu:
                Intent menuIntent = new Intent(VoiceCommActivity.this, VoiceCommActivity.class);
                startActivity(menuIntent);
                return true;
            case R.id.contact_menu:
                menuIntent = new Intent(VoiceCommActivity.this, ContactActivity.class);
                startActivity(menuIntent);
                return true;
            case R.id.learning_menu:
                menuIntent = new Intent(VoiceCommActivity.this, LearningActivity.class);
                startActivity(menuIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
