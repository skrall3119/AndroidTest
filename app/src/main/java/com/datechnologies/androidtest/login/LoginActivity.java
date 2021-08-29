package com.datechnologies.androidtest.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.LoginInfoModel;
import com.datechnologies.androidtest.api.UserClient;
import com.squareup.picasso.OkHttp3Downloader;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 *
 */
public class LoginActivity extends AppCompatActivity {

    String API_BASE_URL = "https://dev.rapptrlabs.com/Tests/scripts/";

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        ActionBar actionBar = getSupportActionBar();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button loginButton = (Button) findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().isEmpty() && editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                postData(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        // Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // Add a ripple effect when the buttons are clicked
        // Save screen state on screen rotation, inputted username and password should not disappear on screen rotation

        // Send 'email' and 'password' to http://dev.rapptrlabs.com/Tests/scripts/login.php
        // as FormUrlEncoded parameters.

        // When you receive a response from the login endpoint, display an AlertDialog.
        // The AlertDialog should display the 'code' and 'message' that was returned by the endpoint.
        // The AlertDialog should also display how long the API call took in milliseconds.
        // When a login is successful, tapping 'OK' on the AlertDialog should bring us back to the MainActivity

        // The only valid login credentials are:
        // email: info@rapptrlabs.com
        // password: Test123
        // so please use those to test the login.
    }

    private void postData(String name, String password){

        Log.i("Response", "Button Clicked");
        //create retrofit builder and pass base url and make data convert to JSON format.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        //create retrofit api class
        UserClient userClient = retrofit.create(UserClient.class);

        Call<ResponseBody> call = userClient.checkLoginInfo(name, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                long requestTime = response.raw().sentRequestAtMillis();
                long responseTime = response.raw().receivedResponseAtMillis();

                long apiTime = responseTime - requestTime;

                if (response.code() == 200){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Response from server")
                            .setMessage(response.code() + ": " + response.message() + "\n" + "Response time: " + apiTime + "ms")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Response from server")
                            .setMessage(response.code() + ": " + response.message() + "\n" + "Response time: " + apiTime + "ms")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                return;
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error found : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
