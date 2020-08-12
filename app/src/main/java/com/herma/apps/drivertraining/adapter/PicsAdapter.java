package com.herma.apps.drivertraining.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.herma.apps.drivertraining.R;

public class PicsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] signimg;
    private final String[] signname;

    public PicsAdapter(Activity context, String[] signname, Integer[] signimg) {
        super(context, R.layout.signlist, signname);
        this.context = context;
        this.signname = signname;
        this.signimg = signimg;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.signlist, null);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.signpics);
        ((TextView) rowView.findViewById(R.id.hatata)).setText(this.signname[position]);
        imageView.setImageResource(this.signimg[position].intValue());
        return rowView;
    }
}
