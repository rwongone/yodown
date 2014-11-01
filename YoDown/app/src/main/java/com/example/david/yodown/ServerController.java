package com.example.david.yodown;
import com.loopj.android.http.*;
import org.apache.http.*;
import org.json.*;


/**
 * Created by Kevin on 11/1/14.
 */
public class ServerController {
    private static ServerController ourInstance = new ServerController();

    public static ServerController getInstance() {
        return ourInstance;
    }

    private static final String BASE_URL = "http://172.26.12.55:3000";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private ServerController() {
    }

    public void loginUser(String userName, String password){
        RequestParams parameters = new RequestParams();
        parameters.add("user_id", userName);
        parameters.add("password", password);
        String URL = BASE_URL+"/users/login";
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loginNewUser();
            }
        };
        client.post(URL, parameters, responseHandler);
    }

    public void createUser(String userName, String password){
        RequestParams parameters = new RequestParams();
        parameters.add("user_id", userName);
        parameters.add("password", password);
        String URL = BASE_URL+"/users";
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loginNewUser();
            }
        };
        client.post(URL, parameters, responseHandler);
    }

    public void loginNewUser(){

    }

}
