package com.example.user.quiztree.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.quiztree.R;
import com.example.user.quiztree.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private String[] chapters;
    private int[] scores;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;

    }

    @Override
    public void onCreate() {
        chapters = mContext.getResources().getStringArray(R.array.all_chapters);
        Log.d("widget create", chapters[0]);
        scores = new int[chapters.length];
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("widget ondata change", user.score_m_1 + " " + user.score_m_2 + " " + user.score_s_1 + " " + user.score_s_2);
                    scores = new int[chapters.length];
                    scores[0] = user.score_m_1 * 20;
                    scores[1] = user.score_m_2 * 20;
                    scores[2] = user.score_s_1 * 20;
                    scores[3] = user.score_s_2 * 20;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            for(int i=0;i<scores.length;i++)
                scores[i]=0;
        }


    }

    @Override
    public void onDataSetChanged() {
       //final long identityToken = Binder.clearCallingIdentity();
        auth = FirebaseAuth.getInstance();
        scores=new int[chapters.length];
        if(auth.getCurrentUser()!=null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    Log.d("widget dataset changed", user.score_m_1 + " " + user.score_m_2 + " " + user.score_s_1 + " " + user.score_s_2);
                    scores[0] = user.score_m_1 * 20;
                    scores[1] = user.score_m_2 * 20;
                    scores[2] = user.score_s_1 * 20;
                    scores[3] = user.score_s_2 * 20;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            for(int i=0;i<scores.length;i++)
                scores[i]=0;
        }
       // Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        chapters=null;
        scores=null;
    }

    @Override
    public int getCount() {
        return chapters.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || chapters == null || mDatabase == null) {
            return null;
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.chapter_name, chapters[position]);
        Log.d("widget getViewsAt", chapters[position]);
        views.setTextViewText(R.id.points, scores[position] + "%");
        views.setImageViewResource(R.id.padnote, R.mipmap.bullet);
        if (scores[position] < 50)
            views.setTextColor(R.id.points, Color.parseColor("#ff0000"));
        else
            views.setTextColor(R.id.points, Color.parseColor("#4CAF50"));

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra("score", scores[position]);
        views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
