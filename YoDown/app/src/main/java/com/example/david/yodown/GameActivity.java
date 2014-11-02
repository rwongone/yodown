package com.example.david.yodown;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class GameActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                                                                GooglePlayServicesClient.OnConnectionFailedListener,
                                                                                                LocationListener{

    public SharedPreferences mPrefs;
    public Editor mEditor;

    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    public boolean mUpdateRequest = true;

    public static final long UPDATE_INTERVAL = 5 * 1000;
    public static final long FASTEST_INTERVAL = 1 * 1000;

    private ListView enemiesList;

    private String username = "";
    private ArrayList<String> enemies;
    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //mCurrentLocation = mLocationClient.getLastLocation();
        //Toast.makeText(this, Double.toString(mCurrentLocation.getLatitude())+Double.toString(mCurrentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        //mUpdateRequest = true;
        Log.v("Location Request", Boolean.toString(mUpdateRequest));
        if(mUpdateRequest){
            //Toast.makeText(this,"requestLocation",Toast.LENGTH_SHORT);
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
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

    @Override
    public void onLocationChanged(Location location){
        String msg = "Updated Location: " + username +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        //TODO: send username + location to server
        if(mPrefs.contains(LoginActivity.USERNAME_SAVE)) {
            username = mPrefs.getString(LoginActivity.USERNAME_SAVE, "DavidIsTheBest");
        }
        RequestParams parameters = new RequestParams();
        parameters.add("user_id", username);
        parameters.add("latitude", Double.toString(location.getLatitude()));
        parameters.add("longitude", Double.toString(location.getLongitude()));
        String URL = BASE_URL+"/places";
        Log.v("URL:", URL);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                enemies.add("Three");
                ((BaseAdapter) enemiesList.getAdapter()).notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            }
        };
        client.get(URL, parameters, responseHandler);

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
        //username = intent.getStringExtra(CreateUserActivity.USERNAME_EXTRA);

        mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

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

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mUpdateRequest = true;

        Intent mServiceIntent = new Intent(this, locationUpdateService.class);
        mServiceIntent.putExtra("USERNAME",username);
        this.startService(mServiceIntent);

        //mLocationClient.connect();
        //mCurrentLocation = mLocationClient.getLastLocation();
        //Toast.makeText(this, Double.toString(mCurrentLocation.getLatitude())+Double.toString(mCurrentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
    }
    public void sendYo(String senderId, String recipientId){
        RequestParams parameters = new RequestParams();
        parameters.add("sender_id", senderId);
        parameters.add("recipient_id", recipientId);
        String URL = BASE_URL+"/yo";
        Log.v("URL:", URL);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("response:", response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

            }
        };
        client.post(URL, parameters, responseHandler);
    }

    //shoot "/yo, params: sender_id, recipient_id"
    //fetch "/location, params: user_id, location{latitude, longitude}"
    @Override
    public void onStart(){
        super.onStart();
        mLocationClient.connect();
    }
    @Override
    public void onPause(){
        mEditor.putBoolean("KEY_UPDATES_ON", mUpdateRequest);
        mEditor.commit();
        super.onPause();
    }
    @Override
    public void onResume(){

    /*
     * Get any previous setting for location updates
     * Gets "false" if an error occurs
     */
        if (mPrefs.contains("KEY_UPDATES_ON")) {
            mUpdateRequest =
                    mPrefs.getBoolean("KEY_UPDATES_ON", true);

            // Otherwise, turn off location updates
        } else {
            mEditor.putBoolean("KEY_UPDATES_ON", true);
            mEditor.commit();
        }
        super.onResume();
    }
    @Override
    public void onStop(){
        if(mLocationClient.isConnected()){
            mLocationClient.removeLocationUpdates(this);
        }
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
