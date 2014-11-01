package com.example.david.yodown;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class GameActivity extends ActionBarActivity {

    private ListView enemiesList;

    private String username = "";
    private ArrayList<String> enemies;
    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

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

    private void fetchNearbyUsers(User user){
        RequestParams parameters = new RequestParams();
        parameters.add("user_id", user.getUserName());
        Map<String, String> location = new HashMap<String, String>();
        location.put("latitude", user.getLatitude()+"");
        location.put("longitude", user.getLongitude()+"");
        parameters.put("location", location);
        String URL = BASE_URL+"/location";
        Log.v("URL:", URL);
        Log.v("parameters:", parameters.toString());
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
}
