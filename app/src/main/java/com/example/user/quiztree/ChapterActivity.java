package com.example.user.quiztree;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ChapterActivity extends AppCompatActivity {
    private int subject;
    private ListView list;
    private Resources resources;
    private String[] topics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        if(getIntent().hasExtra("Subject")){
            subject=getIntent().getIntExtra("Subject",0);
        }
        resources = getResources();
        topics = resources.getStringArray(R.array.itemName);
        if(this.getSupportActionBar()!=null)
            this.getSupportActionBar().setTitle(topics[subject]);
        list = (ListView) findViewById(R.id.chapters_list);
        list.setEmptyView(findViewById(R.id.emptly));
        if(subject==0){
            CustomChapterAdapter adapter = new CustomChapterAdapter (this,resources.getStringArray(R.array.maths_chapters),subject);
            list.setAdapter(adapter);

        }
        else if(subject==1){
            CustomChapterAdapter adapter = new CustomChapterAdapter (this,resources.getStringArray(R.array.science_chapters),subject);
            list.setAdapter(adapter);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isNetworkAvailable()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connection_unavailable), Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("Subject", subject);
                    intent.putExtra("Chapter", position);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
