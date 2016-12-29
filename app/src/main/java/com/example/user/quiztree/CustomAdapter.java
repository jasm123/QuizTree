package com.example.user.quiztree;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] subjects;
    private final Integer[] imgid;

    public CustomAdapter(Activity context, String[] itemname,Integer[] imgid) {
        super(context, R.layout.subject_item, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.subjects= itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.subject_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(subjects[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    }


}
