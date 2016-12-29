package com.example.user.quiztree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private static Firebase fb;
    private static final String Firebase_TOKEN="VfQ88pzvp16JFFIvJAWhYv8IQAbSnPokrJhW9yAI";
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private TextView questionText;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;
    private RadioButton selected;
    private RadioGroup radio;
    private ProgressDialog Dialog;
  // private TextView status;
    String[] subtopics;
    private Button buttonNext;


    private int questionIndex;
    private ArrayList<Question> questions;
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initialize();






    }

    public void initialize(){
        questionText = (TextView) findViewById(R.id.questiontxt);
        option1 = (RadioButton) findViewById(R.id.opt1);
        option2 = (RadioButton) findViewById(R.id.opt2);
        option3 = (RadioButton) findViewById(R.id.opt3);
        option4 = (RadioButton) findViewById(R.id.opt4);
        buttonNext = (Button) findViewById(R.id.nextQuiz);
        radio = (RadioGroup) findViewById(R.id.radio);
        //status= (TextView) findViewById(R.id.status);
        score=0;
        final MediaPlayer correct = MediaPlayer.create(this, R.raw.level_up);
        final MediaPlayer incorrect = MediaPlayer.create(this, R.raw.wrong_buzzer);


        int subject=0;
        int chapter=0;
        if(getIntent().hasExtra("Subject")){
            subject=getIntent().getIntExtra("Subject",0);
            chapter=getIntent().getIntExtra("Chapter",0);
        }
        Resources resources = getResources();
        if(subject==0)//maths
            subtopics = resources.getStringArray(R.array.maths_chapters);
        else if(subject==1)//science
            subtopics= resources.getStringArray(R.array.science_chapters);

        if(this.getSupportActionBar()!=null)
            this.getSupportActionBar().setTitle(subtopics[chapter]);
        if(subtopics[chapter].equals("Ratio and Proportion"))
            subtopics[chapter] = "RatioNProportion";

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        new LoadFirebaseData(this).execute(subtopics[chapter]);
        buttonNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (this.answerIsRight()) {
                            //status.setTextColor(getResources().getColor(R.color.correct));
                            //status.setText("Bravo! Keep it going!!");

                            score++;
                            correct.start();

                        } else {
                            //status.setTextColor(getResources().getColor(R.color.red));
                            //status.setText("Wrong answer!!");
                            incorrect.start();

                        }
                        Log.d("Quiz","in next button "+score);

                        advance();
                    }

                    private boolean answerIsRight() {
                        String ans = "";
                        int id = radio.getCheckedRadioButtonId();
                        selected = (RadioButton) findViewById(id);
                        if (selected == null)
                            return false;
                        if (selected.equals(option1)) ans = "a";
                        if (selected.equals(option2)) ans = "b";
                        if (selected.equals(option3)) ans = "c";
                        if (selected.equals(option4)) ans = "d";
                        return questions.get(questionIndex).isCorrect(ans);

                    }
                }
        );
    }

    private class LoadFirebaseData extends AsyncTask<String, Void, Void> {

    private QuizActivity activity;
        AlertDialog.Builder ad;
        AlertDialog alert;
        public LoadFirebaseData(QuizActivity quizActivity) {
            activity=quizActivity;
            ad= new AlertDialog.Builder(activity);
        }

        @Override
        protected void onPreExecute() {


                Log.d("Quiz", "showing progress dialog");
                Dialog = ProgressDialog.show(activity,"Loading","Loading questions..",true);
            ad.setTitle("Please wait").setMessage("Loading...").setCancelable(true);
            alert=ad.show();
                //sleep(3000);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.d("Quiz","loading data");
            fb=new Firebase("https://quiztree-11838.firebaseio.com/questions/"+params[0]);
            fb.authWithCustomToken(Firebase_TOKEN, null);
            fb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    questions = new ArrayList<>();
                    for (DataSnapshot q : dataSnapshot.getChildren())
                        questions.add(q.getValue(Question.class));
                    questionIndex = 0;

                    displayQuestion();

                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return null;
        }

        @Override
        public void onPostExecute(Void result) {

            Log.d("Quiz", "progress dialog hiding");
            // after completed finished the progressbar
            Dialog.dismiss();
            //alert.cancel();
            super.onPostExecute(result);
        }

    }

    private void displayQuestion() {


        Log.d("Quiz","in display question "+questionIndex);
        questionText.setText(questions.get(questionIndex).questionText);
        option1.setText(questions.get(questionIndex).option1);
        option2.setText(questions.get(questionIndex).option2);
        option3.setText(questions.get(questionIndex).option3);
        option4.setText(questions.get(questionIndex).option4);
        radio.clearCheck();

    }
    private void advance(){
        Log.d("Quiz","in advance. "+questionIndex);
        if(questionIndex==questions.size()-1){
            String title=getSupportActionBar().getTitle().toString();
            switch(title){
                case "Ratio and Proportion":
                    mDatabase.child("users").child(auth.getCurrentUser().getUid().toString()).child("score_m_1").setValue(score);
                    break;
                case "Decimals":
                    mDatabase.child("users").child(auth.getCurrentUser().getUid().toString()).child("score_m_2").setValue(score);
                    break;
                case "Biology":
                    mDatabase.child("users").child(auth.getCurrentUser().getUid().toString()).child("score_s_1").setValue(score);
                    break;
                case "Physics":
                    mDatabase.child("users").child(auth.getCurrentUser().getUid().toString()).child("score_s_2").setValue(score);
                    break;
            }
            Intent intent=new Intent(this,ResultActivity.class);
            intent.putExtra("Chapter",this.getSupportActionBar().getTitle());
            intent.putExtra("score",score);
            intent.putExtra("total",questions.size());
            startActivity(intent);
            finish();
        }else{
            questionIndex=(questionIndex+1);
            displayQuestion();

        }

    }




}



