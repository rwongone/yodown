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

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends ActionBarActivity {

    private ListView enemiesList;

    private String username = "";
    private ArrayList<String> enemies;

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
