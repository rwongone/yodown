package com.example.david.yodown;

import android.content.Context;
import android.content.Intent;
import android.app.IntentService;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class locationUpdateService extends IntentService implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SharedPreferences.Editor mEditor;


    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    public boolean mUpdateRequest = true;

    public SharedPreferences mPrefs;

    String username;

    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    public locationUpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        username = workIntent.getStringExtra("USERNAME");
        //username = intent.getStringExtra(CreateUserActivity.USERNAME_EXTRA);

        mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        //...
        // Do work here, based on the contents of dataString
        //...
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected1", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Disconnected1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " + username +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this,"1 " + msg, Toast.LENGTH_SHORT).show();
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
                /*enemies.add("Three");
                ((BaseAdapter) enemiesList.getAdapter()).notifyDataSetChanged();*/
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}