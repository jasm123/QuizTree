package com.example.user.quiztree.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.quiztree.R;
import com.example.user.quiztree.data.ScoresContract;

/*public class CustomChapterAdapter extends ArrayAdapter<String> implements LoaderManager.LoaderCallbacks<Cursor> {
    private final Activity context;
    private final String[] chapters;
    private int subject;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    public CustomChapterAdapter(Activity context, String[] itemname, int subject) {
        super(context, R.layout.chapter_list_item, itemname);
        // TODO Auto-generated constructor stub
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
*/
public class CustomChapterAdapter extends CursorAdapter{
    private LayoutInflater cursorInflater;
    private Activity context;

    public CustomChapterAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        this.context=context;
        cursorInflater=(LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.chapter_list_item, null, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTitle = (TextView) view.findViewById(R.id.item);
        txtTitle.setText(cursor.getString(cursor.getColumnIndex(ScoresContract.Scores.CHAPTER)));
        ImageView imageView = (ImageView) view.findViewById(R.id.bullet);
        imageView.setImageResource(R.mipmap.bullet);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        final TextView progressTxt = (TextView) view.findViewById(R.id.progresstxt);
        progressBar.setProgress(cursor.getInt(cursor.getColumnIndex(ScoresContract.Scores.SCORE))*20);
        progressTxt.setText(cursor.getInt(cursor.getColumnIndex(ScoresContract.Scores.SCORE))*20+"%");
        progressBar.incrementProgressBy(0);


    }
}