package com.holdings.siloaman.talktoiea;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import java.util.ArrayList;
import java.util.Locale;

public class ContactActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener,
        TextToSpeech.OnInitListener,
        TextToSpeech.OnUtteranceCompletedListener {

    /*
    https://developer.android.com/training/contacts-provider/retrieve-names.html
    https://developer.android.com/training/contacts-provider/retrieve-details.html
    https://github.com/keithweaver/Android-Samples/tree/master/Location/GetLocationAndroidM
    http://demonuts.com/2017/03/20/get-contact-list-details-android-studio-programmatically/
    https://youtu.be/F73tf7ySAZU
    https://youtu.be/g4_1UOFNLEY
    https://youtu.be/dGnEEvCo1lM
    */

    public Button ActionButton, StartListeningButton;
    public TextView ContactNameTV, ContactPhoneTV, speechTextBack;
    String helpMessage, helpContactName, helpPhoneNumber;
    TextToSpeech textToSpeech;
    IntentFilter intentFilter;

    // not sure if necessary
    private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){

            //TextView inTxt = (TextView)findViewById(R.id.textMsg);
            //inTxt.setText(intent.getExtras().getString("message"));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        speechTextBack = (TextView)findViewById(R.id.speechTextBack);
        StartListeningButton = (Button)findViewById(R.id.startListeningButton);
        ActionButton = (Button)findViewById(R.id.actionButton);
        textToSpeech = new TextToSpeech(ContactActivity.this, ContactActivity.this);


        ContactNameTV = (TextView)findViewById(R.id.contactNameTV);
        ContactPhoneTV = (TextView)findViewById(R.id.contactPhoneTV);



        // SPEECH TO TEXT HANDLER BUTTON
        StartListeningButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    // Doesn't work yet...
                //Intent intent = new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
                // Specify free form input
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, I'm IEA. How can I help you?");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30000);
                startActivityForResult(intent, 2);

            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");





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

            String parsibleString;

            parsibleString = speechTextBack.getText().toString();

            String[] arrayofWords = parsibleString.split("\\W+");       // delimit by Words

            String contactCommand = "contact";
            String forCommand = "for"; String helpCommand = "help";

            int numberofWords = arrayofWords.length;
            String nameofContact = "nobody";
            boolean foundContact = false;

            // QUERY FOR THE PROPER SENTENCE STRUCTURE
            if(parsibleString.contains("contact") /*|| parsibleString.contains("Contact")*/ &&
                    parsibleString.contains("help")) {

                if (arrayofWords[0].equalsIgnoreCase(contactCommand)){         // possibly a middle and last name or special case

                    //if(arrayofWords[2].equalsIgnoreCase(forCommand) || arrayofWords[3].equalsIgnoreCase(forCommand)) {

                        //if(arrayofWords[2].equalsIgnoreCase(forCommand))
                            nameofContact = arrayofWords[1];
                        //else if(arrayofWords[3].equalsIgnoreCase(forCommand))
                        //    nameofContact = arrayofWords[1] + " " + arrayofWords[2];

                        foundContact = getContactDetails(nameofContact);    //capitalizedName
                    //}
                }
            }
            //String capitalizedName = "";

            //if(numberofWords == 2)
            //    capitalizedName = nameofContact.substring(0, 1).toUpperCase() + nameofContact.substring(1);
            //else if(numberofWords > 2){
            //
            //}

            //boolean foundContact = getContactDetails(nameofContact);    //capitalizedName

            if(foundContact){

                Toast.makeText(ContactActivity.this, "Calling: " + ContactNameTV/*capitalizedName*/, Toast.LENGTH_LONG).show();

                // SEND THE SMS MESSAGE
                String tempContactName = ContactNameTV.getText().toString();

                //helpContactName is giving the first person in list, not the actual person I requested!!!
                helpMessage = "Hello " + tempContactName/*helpContactName*/ + "! I am testing my new COOL app! I am currently at: 70 The Pond Rd.";
                Toast.makeText(ContactActivity.this, helpMessage, Toast.LENGTH_LONG).show();

                sendMsg(helpPhoneNumber, helpMessage);
            }
        }
    }

    protected void sendMsg(String theNumber, String myMsg){

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);

    }

    @Override
    protected void onResume(){

        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause(){

        //unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    public boolean getContactDetails(String requestedName) {

        // TAKEN FROM : https://youtu.be/g4_1UOFNLEY

        boolean contactFound = false;

        ContentResolver myResolver = getContentResolver();   // class used to query / retrieve list of contacts
        Cursor cursor = myResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()){

            String contact_id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String contact_name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            helpContactName = contact_name;
            String contact_phone_number = "";
            //String contact_email = "";

            if(contact_name.contains(requestedName)) {

                // need phone cursor because of possibility of multiple phone numbers (home, cell, work, etc) for one ID user
                Cursor phoneCursor = myResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contact_id}, null);

                while (phoneCursor.moveToNext()) {

                    contact_phone_number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    helpPhoneNumber = contact_phone_number;
                    break;
                }

                Cursor emailCursor = myResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                /*
                while (emailCursor.moveToNext()) {

                    contact_email = emailCursor.getString(emailCursor.getColumnIndexOrThrow
                            (ContactsContract.CommonDataKinds.Email.DATA));
                }
                */
                contactFound = true;
                ContactNameTV.setText(contact_name);
                ContactPhoneTV.setText(contact_phone_number);


            }
        }
        return contactFound;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                Intent menuIntent = new Intent(ContactActivity.this, VoiceCommActivity.class);
                startActivity(menuIntent);
                return true;
            case R.id.contact_menu:
                menuIntent = new Intent(ContactActivity.this, ContactActivity.class);
                startActivity(menuIntent);
                return true;
            case R.id.learning_menu:
                menuIntent = new Intent(ContactActivity.this, LearningActivity.class);
                startActivity(menuIntent);
                return true;
            case R.id.direction_menu:
                menuIntent = new Intent(ContactActivity.this, CardinalDirectionActivity.class);
                startActivity(menuIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInit(int i) {

        textToSpeech.setOnUtteranceCompletedListener(this);
    }

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
}
