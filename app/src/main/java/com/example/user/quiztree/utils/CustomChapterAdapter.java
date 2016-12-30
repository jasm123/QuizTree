package com.example.user.quiztree.utils;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.quiztree.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CustomChapterAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] chapters;
    private int subject;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    public CustomChapterAdapter(Activity context, String[] itemname, int subject) {
        super(context, R.layout.chapter_list_item, itemname);
        // TODO Auto-generated constructor stub
        //progress=40;
        this.subject = subject;
        this.context = context;
        this.chapters = itemname;
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid().toString());
        Log.d(getClass().toString(), mDatabase.toString());
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.chapter_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        txtTitle.setText(chapters[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.bullet);
        imageView.setImageResource(R.mipmap.bullet);

        final ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        final TextView progressTxt = (TextView) rowView.findViewById(R.id.progresstxt);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d(getClass().toString(), user.score_m_1 + "");
                if (subject == 0) {
                    if (position == 0) {
                        progressBar.setProgress(user.score_m_1 * 20);
                        progressTxt.setText(user.score_m_1 * 20 + "%");
                    } else if (position == 1) {
                        progressBar.setProgress(user.score_m_2 * 20);
                        progressTxt.setText(user.score_m_2 * 20 + "%");
                    }
                } else if (subject == 1) {
                    if (position == 0) {
                        progressBar.setProgress(user.score_s_1 * 20);
                        progressTxt.setText(user.score_s_1 * 20 + "%");
                    } else if (position == 1) {
                        progressBar.setProgress(user.score_s_2 * 20);
                        progressTxt.setText(user.score_s_2 * 20 + "%");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progressBar.incrementProgressBy(0);


        return rowView;

    }

}
