package com.herma.apps.drivertraining.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.database.CauseData;

public class CauseFragment extends Fragment {
    TextView messageTextView;
    TextView messageTextView4;

    public static CauseFragment newInstance(String message) {
        CauseFragment f = new CauseFragment();
        Bundle args = new Bundle();
        args.putString("EXTRA_MESSAGE", message);
        f.setArguments(args);
        return f;
    }

    public View onCreateView(LayoutInflater inf, ViewGroup c, Bundle sI) {
        String message = getArguments().getString("EXTRA_MESSAGE");
        View v = inf.inflate(R.layout.dispo, c, false);
        CauseData fd = new CauseData();
        String[] vv1 = fd.volleey1;
//        String[] vv2 = fd.volleey2;
        this.messageTextView = (TextView) v.findViewById(R.id.section_label);
        this.messageTextView.setText(message);
        this.messageTextView4 = (TextView) v.findViewById(R.id.textView4);
//        if (message == vv1[0]) {
//            this.messageTextView4.setText(vv2[0]);
//        }
//        if (message == vv1[1]) {
//            this.messageTextView4.setText(vv2[1]);
//        }
//        if (message == vv1[2]) {
//            this.messageTextView4.setText(vv2[2]);
//        }
//        if (message == vv1[3]) {
//            this.messageTextView4.setText(vv2[3]);
//        }
//        if (message == vv1[4]) {
//            this.messageTextView4.setText(vv2[4]);
//        }
//        if (message == vv1[5]) {
//            this.messageTextView4.setText(vv2[5]);
//        }
//        if (message == vv1[6]) {
//            this.messageTextView4.setText(vv2[6]);
//        }
//        if (message == vv1[7]) {
//            this.messageTextView4.setText(vv2[7]);
//        }
//        if (message == vv1[8]) {
//            this.messageTextView4.setText(vv2[8]);
//        }
//        if (message == vv1[9]) {
//            this.messageTextView4.setText(vv2[9]);
//        }
//        if (message == vv1[10]) {
//            this.messageTextView4.setText(vv2[10]);
//        }
//        if (message == vv1[11]) {
//            this.messageTextView4.setText(vv2[11]);
//        }
//        if (message == vv1[12]) {
//            this.messageTextView4.setText(vv2[12]);
//        }
//        if (message == vv1[13]) {
//            this.messageTextView4.setText(vv2[13]);
//        }
        return v;
    }
}
