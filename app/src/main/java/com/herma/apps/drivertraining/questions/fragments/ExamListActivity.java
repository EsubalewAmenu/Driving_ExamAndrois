package com.herma.apps.drivertraining.questions.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.questions.DB;
import com.herma.apps.drivertraining.questions.QuestionActivity;

import java.io.IOException;

public class ExamListActivity extends AppCompatActivity {

    private static final int BASED_ON_CHOOSE_REQUEST = 1000;

    DB db;

    int numOfDbRow = 0;

    int number_of_questions_per;

    int list_rows = 0;

//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        ListView questionsListView = (ListView) findViewById(R.id.questionsListView);

        open("read","full.hrm");

        Cursor c = db.doExcute("SELECT COUNT(*) FROM que");

        if (c.moveToFirst()) {

            numOfDbRow = c.getInt(0);
        }

            db.close();


        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(ExamListActivity.this);

        try {
            number_of_questions_per = Integer.parseInt(pre.getString("no_of_que", "anon"));
        }catch (Exception klk) { number_of_questions_per = 50; }

        list_rows = numOfDbRow/number_of_questions_per;

        if( numOfDbRow % number_of_questions_per > 0)
            list_rows++;

        String[] dataArray = new String[list_rows];

        for(int i = 0; i < list_rows; i ++) {

            dataArray[i] = "መልመጃ #" + (i + 1);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.exam_list_listview, R.id.textView, dataArray);
        questionsListView.setAdapter(arrayAdapter);

        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int str = ((i + 1) * number_of_questions_per); str -= number_of_questions_per;

                Intent questions = new Intent(ExamListActivity.this, QuestionActivity.class);
                questions.putExtra("start", str);
                questions.putExtra("type", "fixed");
                questions.putExtra("packege", "Free");
                startActivityForResult(questions, BASED_ON_CHOOSE_REQUEST);

            }
        });

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });


//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BASED_ON_CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {

                /* Here, You go back from where you started OR If you want to go next Activity just change the Intent*/
                Intent returnIntent = new Intent();

                returnIntent.putExtra("answerKey", data.getStringArrayExtra("answerKey"));
                returnIntent.putExtra("response", data.getStringArrayExtra("response"));
                returnIntent.putExtra("questions", data.getStringArrayExtra("questions"));
                returnIntent.putExtra("questionsWithAnswer", data.getStringArrayExtra("questionsWithAnswer"));

                returnIntent.putExtra("timer", data.getStringExtra("timer"));

                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        }
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
