package com.holdings.siloaman.talktoiea;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;


public class ContactActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener/*, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener*/ {

    /*
    https://developer.android.com/training/contacts-provider/retrieve-names.html
    https://developer.android.com/training/contacts-provider/retrieve-details.html
    https://github.com/keithweaver/Android-Samples/tree/master/Location/GetLocationAndroidM
    */

    long contactID;
    String contactName;
    ContactsContract.CommonDataKinds.Phone contactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final TextView ContactNameTV = (TextView)findViewById(R.id.nameDisplayTextView);
        final TextView ContactPhoneTV = (TextView)findViewById(R.id.phoneDisplayTextView);
        final TextView TextMessageTV = (TextView)findViewById(R.id.textMessageDisplayTextView);
        final TextView LocationTV = (TextView)findViewById(R.id.locationDisplayTextView);

        EditText GetContactET = (EditText)findViewById(R.id.findEditText);

        Button LoadContactButton = (Button)findViewById(R.id.loadDataButton);
        Button HelpButton = (Button)findViewById(R.id.helpButton);

        LoadContactButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                // Load Contact Info into TextViews
            }
        });

        HelpButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                // Send the Text Message
            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
