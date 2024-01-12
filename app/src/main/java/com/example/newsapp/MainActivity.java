package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> titlesList;
    ArrayList<String> linksList;
    DBHelper dbHelper;

    public void onGetLatestNewClick(View view)
    {
        DownloadNewsTask downloadNewsTask =  new DownloadNewsTask();
       // downloadNewsTask.execute("https://newsdata.io/api/1/news?apikey=pub_360892213562fd4e7409855f581bdd6af994b&language=en");
       updateListWithData();
       callListActivity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titlesList =  new ArrayList<>();
        linksList = new ArrayList<>();
        dbHelper = new DBHelper(this);
    }

    public class DownloadNewsTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
           String result = " ";
           URL url;
            HttpsURLConnection httpsURLConnection;

            try {
                url =  new URL(strings[0]);
                try {
                    httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = httpsURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    int data = inputStreamReader.read();

                    while(data != -1)
                    {
                        char ch  = (char) data;
                        result +=ch;
                        data = inputStreamReader.read();
                    }
                    return result;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //JOSn
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String totalResults = jsonObject.getString("totalResults");
                String results = jsonObject.getString("results");

                Log.i("Results status", status);
                Log.i("Results total", totalResults);


                JSONArray resultsArray = new JSONArray(results);
                for(int i =0 ; i<resultsArray.length(); i++)
                {
                    JSONObject jsonPart = resultsArray.getJSONObject(i);
                    String title =  jsonPart.getString("title");
                    String link =  jsonPart.getString("link");
                    //titlesList.add(jsonPart.getString("title"));
                    //INSERT title and link IN DB

                    long id = dbHelper.insertData(title,link);
                    if(id <=0)
                    {
                        Log.i("db", "Insertion Unsuccessful");
                    }
                    else
                    {
                        Log.i("db", "Insertion Successful");
                    }
                    Log.i("result Title",jsonPart.getString("title"));
                    Log.i("result Image",jsonPart.getString("image_url"));
                }
                updateListWithData();
                callListActivity();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void updateListWithData() {
        Cursor cursor = dbHelper.getCursorForData();
        while(cursor.moveToNext())
        {
            @SuppressLint("Range") int id =  cursor.getInt(cursor.getColumnIndex(dbHelper.ID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(dbHelper.TITLE));
            titlesList.add(title);
            @SuppressLint("Range") String link = cursor.getString(cursor.getColumnIndex(dbHelper.LINK));
            linksList.add(link);
        }
    }

    private void callListActivity()
    {
        Intent intent = new Intent(MainActivity.this,NewsList.class);
        intent.putExtra("title",titlesList);
        intent.putExtra("link", linksList);
        startActivity(intent);
    }

}