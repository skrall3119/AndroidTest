package com.datechnologies.androidtest.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.JsPromptResult;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.ChatLogMessageModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Screen that displays a list of chats from a chat log.
 */
public class ChatActivity extends AppCompatActivity implements AsyncResponse {

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private ChatAdapter chatAdapter;
    private List<ChatLogMessageModel> tempList = new ArrayList<>();

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================


    public static void start(Context context)
    {
        Intent starter = new Intent(context, ChatActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Chat");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        chatAdapter = new ChatAdapter();

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        JsonTask task = new JsonTask();
        task.delegate = this;
        task.execute("https://dev.rapptrlabs.com/Tests/scripts/chat_log.php");



//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);
//        tempList.add(chatLogMessageModel);

        chatAdapter.setChatLogMessageModelList(tempList);

        // Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.

        // Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php
        // TODO: Parse this chat data from JSON into ChatLogMessageModel and display it.
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String result){
        Gson gson = new Gson();
        JsonObject object = new JsonParser().parse(result).getAsJsonObject();

        JsonArray arr = object.getAsJsonArray("data");
        for(int i = 0; i< arr.size(); i++){
            JsonObject obj = arr.get(i).getAsJsonObject();
            ChatLogMessageModel model = gson.fromJson(obj.toString(), ChatLogMessageModel.class);
            Log.i("Link", model.avatar_url);
            tempList.add(model);
        }
        chatAdapter.setChatLogMessageModelList(tempList);
    }

    // Retrive JSON data asynchronously
    private static class JsonTask extends AsyncTask<String, Void, String> {
        public AsyncResponse delegate = null;

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);

                }

                return buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            delegate.processFinish(result);
        }
    }
}
interface AsyncResponse {
    void processFinish(String result);
}

