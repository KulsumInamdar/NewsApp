package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class NewsList extends AppCompatActivity implements NewsRecyclerViewAdapter.ItemClickListener {

    NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    ArrayList<String> titlesList;
    ArrayList<String> linksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        titlesList = (ArrayList<String>) getIntent().getSerializableExtra("title");
        linksList = (ArrayList<String>) getIntent().getSerializableExtra("link");

        RecyclerView recyclerView = findViewById(R.id.news_rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, titlesList, this);

        recyclerView.setAdapter(mNewsRecyclerViewAdapter);


    }

    @Override
    public void onItemClick(View view, int position) {

        //Call web-view activity
        Intent intent = new Intent(NewsList.this, NewsWebView.class);
        intent.putExtra("url",linksList.get(position));
        startActivity(intent);
    }
}