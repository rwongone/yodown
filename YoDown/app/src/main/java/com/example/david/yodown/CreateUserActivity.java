package com.example.david.yodown;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.PendingIntent.getActivity;


public class CreateUserActivity extends ActionBarActivity {

    private User user;
    private Button formCreateBtn;
    private EditText createUsername;
    private TextView errorId;
    private final String BASE_URL = "http://104.236.61.102:3000";
    private AsyncHttpClient client = new AsyncHttpClient();

    public static final String USERNAME_EXTRA="create username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        formCreateBtn = (Button)findViewById(R.id.formCreateBtn);
        createUsername = (EditText)findViewById(R.id.createUsername);
        errorId = (TextView)findViewById(R.id.errorId);

        formCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidInput()){
                    sendServer(v);
                }
            }
        });
    }

    private boolean checkValidInput(){
        String username = createUsername.getText().toString();
        if(username.equals("")){
            errorId.setText(getResources().getString(R.string.usernameCreateError));
            errorId.setTextColor(getResources().getColor(R.color.errorRed));
            return false;
        }
        errorId.setText(getResources().getString(R.string.createNotify));
        errorId.setTextColor(getResources().getColor(R.color.goodGreen));
        //TODO: check if username is already being used
        return true;
    }

    private void sendServer(View v){
        String username = createUsername.getText().toString();
        String password = createPassword.getText().toString();
/*        RequestParams parameters = new RequestParams();
        parameters.add("user_id", username);
        parameters.add("password", password);
        String URL = BASE_URL+"/users";
        Log.v("URL:", URL);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                createNewUser(response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.v("RESPONSE:", response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.v("RESPONSE:", response);
            }

        };
        client.post(URL, parameters, responseHandler);*/
        //TODO: send username and password to server
    }

    private void createNewUser(JSONObject response){
        try {
            user = new User(response.getString("_id"), createUsername.getText().toString(), createPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(user.getUniqueId() + ", " +  user.getPassword() + ", " + user.getUserName());
        AlertDialog dialog = builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
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
