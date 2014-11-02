package com.example.david.yodown;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class GameActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                                                                GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private ListView enemiesList;

    private String username = "";
    private ArrayList<String> enemies;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try{
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }catch(IntentSender.SendIntentException e){
                e.printStackTrace();
            }
        }else{
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    public void showErrorDialog(int errorCode){
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if(errorDialog != null){
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME_EXTRA);
        username = intent.getStringExtra(CreateUserActivity.USERNAME_EXTRA);

        enemiesList = (ListView)findViewById(R.id.enemyList);

        //TODO: get closest locations
        enemies = new ArrayList<String>();
        //Testing
        enemies.add("one");
        enemies.add("Two");
        enemiesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enemies));
        Log.v("g","g");
        enemiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: send server request
                Log.v("Item", enemies.get(position));
            }
        });

        mLocationClient = new LocationClient(this, this, this);

        //mCurrentLocation = mLocationClient.getLastLocation();
        //Toast.makeText(this, Double.toString(mCurrentLocation.getLatitude())+Double.toString(mCurrentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(){
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    public void onStop(){
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ErrorDialogFragment extends DialogFragment{
        private Dialog mDialog;
        public ErrorDialogFragment(){
            super();
            mDialog = null;
        }
        public void setDialog(Dialog dialog){
            mDialog = dialog;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            return mDialog;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case CONNECTION_FAILURE_RESOLUTION_REQUEST  :
                switch (resultCode){
                    case Activity.RESULT_OK:
                        //TODO:try request again
                    break;
                }
        }
    }

    private boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(ConnectionResult.SUCCESS == resultCode){
            Log.d("Location updates", "Google play service is available");
            return true;
        }else{
            showErrorDialog(resultCode);
        }
        return false;
    }

}
