package com.herma.apps.drivertraining.others;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import com.herma.apps.drivertraining.MainActivity;
import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.about.About_us;
import com.herma.apps.drivertraining.adapter.PicsAdapter;
import com.herma.apps.drivertraining.questions.AnswersActivity;
import com.herma.apps.drivertraining.questions.DB;

public class Sign_Home extends AppCompatActivity {

//    ListView picslist;
//    DB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_home);

//        setContentView(R.layout.signs);


//        open("read","quiz.hrm");
//
//        Cursor c = db.getSelect("*", "sign", "type='c'");
//
//        String itemname[] = new String[c.getCount()];
//        Integer signimg[] = new Integer[c.getCount()];
//        int i = 0;
//        if (c.moveToFirst()) {
//            do{
//                itemname[i] = c.getString(2);
//                signimg[i] = getResources().getIdentifier(c.getString(1), "drawable", getApplicationContext().getPackageName());
//                i++;
//            } while (c.moveToNext());
//        }
//
//        db.close();
//
//
//        PicsAdapter adapter = new PicsAdapter(this, itemname, signimg);
//        this.picslist = (ListView) findViewById(R.id.listView19);
//        this.picslist.setAdapter(adapter);


        ((Button) findViewById(R.id.info_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Info_signs.class));
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Sign_Display.class));

                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "i");
                startActivity(questions);

            }
        });
        ((Button) findViewById(R.id.first_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), First_signs.class));


                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "f");
                startActivity(questions);

            }
        });
        ((Button) findViewById(R.id.must_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Must_signs.class));
                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "m");
                startActivity(questions);

            }
        });
        ((Button) findViewById(R.id.war_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), War_signs.class));
                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "w");
                startActivity(questions);
            }
        });
        ((Button) findViewById(R.id.light_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Light_signs.class));


                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "l");
                startActivity(questions);
            }
        });
        ((Button) findViewById(R.id.pro_s)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Pro_signs.class));

                Intent questions = new Intent(Sign_Home.this, Sign_Display.class);
                questions.putExtra("where", "p");
                startActivity(questions);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutmenu) {
            startActivity(new Intent(getApplicationContext(), About_us.class));
            return true;
        } else if (id == R.id.fb) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://facebook.com/BunaApps")));
            return true;
        } else if (id == R.id.exitmenu) {
            finish();
            return true;
        } else if (id == R.id.rateapp) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            return true;
        } else if (id == R.id.topinfo) {
            startActivity(new Intent(getApplicationContext(), About_us.class));
            return true;
        } else {
            if (id == R.id.moreapp) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=BUNA+APPS")));
            } else if (id == R.id.shareapp) {
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.SUBJECT", getText(R.string.app_name));
                sendIntent.putExtra("android.intent.extra.TEXT", "Downloads \nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getText(R.string.app_name)));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

//    public void open(String write, String db_name) {
//
//        db = new DB(this, db_name);
//        try {
//            if (write.equals("write"))
//                db.writeDataBase();
//            else
//                db.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//        try {
//            db.openDataBase();
//        } catch (SQLException sqle) {
//            throw sqle;
//        }
//    }
}
