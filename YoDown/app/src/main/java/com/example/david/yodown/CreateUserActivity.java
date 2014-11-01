package com.example.david.yodown;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CreateUserActivity extends ActionBarActivity {

    private Button formCreateBtn;
    private EditText createUsername;
    private EditText createPassword;
    private EditText createRePassword;
    private TextView errorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        formCreateBtn = (Button)findViewById(R.id.formCreateBtn);
        createUsername = (EditText)findViewById(R.id.createUsername);
        createPassword = (EditText)findViewById(R.id.createPassword);
        createRePassword = (EditText)findViewById(R.id.createRePassword);
        errorId = (TextView)findViewById(R.id.errorId);

        formCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidInput()){
                    sendServer();
                }
            }
        });
    }

    private boolean checkValidInput(){
        String username = createUsername.getText().toString();
        if(username.equals("")){
            errorId.setText(getResources().getString(R.string.usernameCreateError));
            return false;
        }
        String password = createPassword.getText().toString();
        if(password.equals("")){
            errorId.setText(getResources().getString(R.string.passwordCreateError1));
            return false;
        }
        String rePassword = createRePassword.getText().toString();
        if(!(password.equals(rePassword))){
            errorId.setText(getResources().getString(R.string.passwordCreateError2));
            return false;
        }
        //TODO: check if username is already being used
        return true;
    }

    private void sendServer(){
        String username = createUsername.getText().toString();
        String password = createPassword.getText().toString();
        //TODO: send username and password to server
        //TODO: go to game page
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
