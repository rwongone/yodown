package com.example.david.yodown;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HighScoreActivity extends ActionBarActivity {

    ArrayList<String> scores;
    ArrayAdapter<String> adapter;
    ListView highScoreList;

    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scores = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.custom_arrayadapter_layout_center_text, scores);
        highScoreList = (ListView)findViewById(R.id.highScoreList);
        getScore();
    }

    private void getScore(){
        RequestParams parameters = new RequestParams();
        String URL = BASE_URL+"/places";
        Log.v("URL:", URL);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
                //enemies.add("Three");
                adapter.clear();
                ArrayList<String> temp = new ArrayList<String>();
                scores.clear();
                if(response!=null){
                    for(int i=0;i<response.length();i++){
                        try {
                            temp.add(response.getJSONObject(i).getString("user_id"));
                            //enemies.add(response.getJSONObject(i).getString("user_id"));
                            Log.v("User:", response.getJSONObject(i).getString("user_id")+response.getJSONObject(i).getString("score"));
                        }catch(JSONException e){
                            Log.v("JSON exception:",e.toString());
                        }
                    }
                }
                adapter.addAll(temp);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getApplication(), "Success2", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Toast.makeText(getApplication(), "Fail1", Toast.LENGTH_SHORT).show();
                if(response!=null){
                    Log.v("FAIL1:", response.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Toast.makeText(getApplication(), "Fail2", Toast.LENGTH_SHORT).show();
                if(response != null){
                    Log.v("FAIL2:", response);
                }
            }
        };
        client.post(URL, parameters, responseHandler);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
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
