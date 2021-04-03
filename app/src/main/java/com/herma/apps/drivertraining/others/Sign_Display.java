package com.herma.apps.drivertraining.others;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.adapter.PicsAdapter;
import com.herma.apps.drivertraining.questions.DB;

public class Sign_Display extends AppCompatActivity {
    ListView picslist;

    DB db;

//    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signs);

        open("read","full.hrm");

        String where = getIntent().getStringExtra("where");

        Cursor c = db.getSelect("*", "sign", "type='"+where+"'");

        String itemname[] = new String[c.getCount()];
        Integer signimg[] = new Integer[c.getCount()];
        int i = 0;
        if (c.moveToFirst()) {
            do{
                itemname[i] = c.getString(2);
                signimg[i] = getResources().getIdentifier(c.getString(1), "drawable", getApplicationContext().getPackageName());
                i++;
            } while (c.moveToNext());
        }

        db.close();


        PicsAdapter adapter = new PicsAdapter(this, itemname, signimg);
        this.picslist = (ListView) findViewById(R.id.listView19);
        this.picslist.setAdapter(adapter);


//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

//        mAdView = findViewById(R.id.adViewSign);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

    }

    public void open(String write, String db_name) {

        db = new DB(this, db_name);
        try {
            if (write.equals("write"))
                db.writeDataBase();
            else
                db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
}
