package com.example.david.yodown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    public static final String USERNAME_SAVE = "save username";
    public static final String PASSWORD_SAVE = "save password";

    private Button loginBtn;
    private EditText loginUsername;
    private EditText loginPassword;
    private CheckBox logCheckBox;
    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

    public SharedPreferences mPref;
    public Editor mEditor;
    public boolean autoLog = false;

    public static final String USERNAME_EXTRA = "Username Extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText)findViewById(R.id.loginUsername);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        logCheckBox = (CheckBox)findViewById(R.id.logCheckBox);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidInput();
            }
        });
    }

    private void checkValidInput(){
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        RequestParams parameters = new RequestParams();
        parameters.add("user_id", username);
        parameters.add("password", password);
        String URL = BASE_URL+"/users/login";
        Log.v("URL:", URL);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("response:", response.toString());
                toGamePage();
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

    private void toGamePage(){
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        Boolean saveLog = logCheckBox.isChecked();
        if(saveLog){
            mEditor.putString(USERNAME_SAVE, username);
            mEditor.putString(PASSWORD_SAVE, password);
        }
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra(USERNAME_EXTRA, username);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
