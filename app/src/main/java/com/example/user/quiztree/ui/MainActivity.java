package com.example.user.quiztree.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.user.quiztree.R;
import com.example.user.quiztree.data.ScoresContract;
import com.example.user.quiztree.utils.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    Integer[] imgid = {
            R.drawable.abacus,
            R.drawable.science
    };
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private ListView listView;
    private String[] topics;
    private Resources resources;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();


        resources = getResources();
        String[] chapters = resources.getStringArray(R.array.all_chapters);
        for (int i = 0; i < chapters.length; i++) {
            ContentValues values = new ContentValues();
            values.put(ScoresContract.Scores.CHAPTER, chapters[i]);
            values.put(ScoresContract.Scores.SCORE, 0);
            getContentResolver().insert(ScoresContract.Scores.CONTENT_URI, values);
        }

        topics = resources.getStringArray(R.array.itemName);
        listView = (ListView) findViewById(R.id.subjects);
        listView.setEmptyView(findViewById(R.id.empty));
        CustomAdapter adapter = new CustomAdapter(this, topics, imgid);
        listView.setAdapter(adapter);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
                intent.putExtra("Subject", position);
                startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();

            Log.d(TAG, "logged out");
            return true;
        } else
            return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
