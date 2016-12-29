package com.example.user.quiztree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private float percentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView chapter=(TextView) findViewById(R.id.chapter);
        TextView result=(TextView) findViewById(R.id.result);
        TextView score=(TextView) findViewById(R.id.score);
        TextView report=(TextView) findViewById(R.id.report);
        Button back=(Button)findViewById(R.id.back);
        CoordinatorLayout backgrnd = (CoordinatorLayout)findViewById(R.id.bg);
        backgrnd.getBackground().setAlpha(50);
        if(getIntent().hasExtra("Chapter")){
            chapter.setText(getIntent().getStringExtra("Chapter"));
        }
        if(getIntent().hasExtra("score") && getIntent().hasExtra("total")){
            int points=getIntent().getIntExtra("score",0);
            int total=getIntent().getIntExtra("total", 5);
            score.setText(points+"/"+total);
            percentage= (points/(float)total)*100;
            result.setText(String.format("%.2f", percentage)+"%");
            if(percentage<50)
                result.setTextColor(getResources().getColor(R.color.red));
            else
                result.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        if(percentage<50){
            report.setText(getResources().getString(R.string.bad_report));
            report.setTextColor(getResources().getColor(R.color.red));
        }
        else{
            report.setText(getResources().getString(R.string.good_report));
            report.setTextColor(getResources().getColor(R.color.correct));
        }
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }
}
