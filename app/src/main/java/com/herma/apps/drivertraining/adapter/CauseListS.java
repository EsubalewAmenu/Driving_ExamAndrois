package com.herma.apps.drivertraining.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.database.CauseData;

public class CauseListS extends BaseAdapter {
    private Context context;
    CauseData fd = new CauseData();
    String[] socialSite;
    String[] volleey = this.fd.volleey1;

    public CauseListS(Context context) {
        this.context = context;
        this.socialSite = this.volleey;
    }

    public int getCount() {
        return this.socialSite.length;
    }

    public Object getItem(int position) {
        return this.socialSite[position];
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        View row;
        if (converView == null) {
            row = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.song_list, parent, false);
        } else {
            row = converView;
        }
        row.findViewById(R.id.textView);
        ((TextView) row.findViewById(R.id.textView)).setText(this.socialSite[position]);
        return row;
    }
}
