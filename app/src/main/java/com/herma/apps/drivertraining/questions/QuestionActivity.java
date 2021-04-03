package com.herma.apps.drivertraining.questions;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.herma.apps.drivertraining.MainActivity;
import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.questions.adaptersj.ViewPagerAdapter;
import com.herma.apps.drivertraining.questions.fragments.CheckBoxesFragment;
import com.herma.apps.drivertraining.questions.fragments.RadioBoxesFragment;

import static com.herma.apps.drivertraining.MainActivity.Ads;

public class QuestionActivity extends AppCompatActivity
{
    final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    //private TextView questionToolbarTitle;
    private TextView questionPositionTV;
    private String totalQuestions = "1";
    private ViewPager questionsViewPager;
    DB db;
    public String[][] questionsWithAnswer;
    public String[] answerKey, response, questions, queId;

    long startTime = 0L;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    public int mins, secs;
    TextView timerValue;

    int per_exam;

    public String show_answer, packege;
    TextView tvAds;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(QuestionActivity.this);
        show_answer = pre.getString("show_answer", "anon");
try{
        per_exam = Integer.parseInt(pre.getString("no_of_que", "anon"));
    }catch (Exception klk) { per_exam = 50; }

        toolBarInit();

//        if (getIntent().getExtras() != null)
//        {
//            Bundle bundle = getIntent().getExtras();
//            parsingData(bundle);
//        }
            parsingData();

            tvAds = (TextView) findViewById(R.id.tvAds);

            setAd();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8011674951494696/2410308247");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }
    private void toolBarInit()
    {
        Toolbar questionToolbar = findViewById(R.id.questionToolbar);
        questionToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        questionToolbar.setNavigationOnClickListener(v -> onBackPressed());

        //questionToolbarTitle = questionToolbar.findViewById(R.id.questionToolbarTitle);
        questionPositionTV = questionToolbar.findViewById(R.id.questionPositionTV);

        //questionToolbarTitle.setText("Questions");
    }

    /*This method decides how many Question-Screen(s) will be created and
    what kind of (Multiple/Single choices) each Screen will be.*/
    private void parsingData() {
        open("read", "full.hrm");

        if (getIntent().getExtras() != null) {

            int start = getIntent().getIntExtra("start", 0);
            String type = getIntent().getStringExtra("type");
            packege = getIntent().getStringExtra("packege");


            if(type.equalsIgnoreCase("fixed")){

            questionsWithAnswer = db.getSelectArray("*", "que", "id > " +start+ " and id <=  "+(start+per_exam));

        } else if(type.equalsIgnoreCase("rand")){// ORDER BY RANDOM()  ORDER BY random
                Cursor c = db.doExcute("SELECT MIN(seen) FROM que");
                int max = 0;
                if(c.moveToFirst()) max = c.getInt(0);
                do{
        questionsWithAnswer = db.getSelectArray("*", "que",  "seen >= (SELECT MIN(seen) FROM que) and seen <=" + max + " order by random() limit " + per_exam);//and id <=  "+(start+per_exam));
                    max++;
                }while(questionsWithAnswer.length!=per_exam);

            }

        queId = new String[questionsWithAnswer.length];
        answerKey = new String[questionsWithAnswer.length];
        response = new String[questionsWithAnswer.length];
        questions = new String[questionsWithAnswer.length];
        db.close();

        totalQuestions = (questionsWithAnswer.length)+"";

        String questionPosition = "1/" + totalQuestions;
        setTextWithSpan(questionPosition);


        for (int i = 0; i < (questionsWithAnswer.length); i++)
        {
            queId[i] = questionsWithAnswer[i][0];
            if (questionsWithAnswer[i][6].equals("CheckBox"))
            {
                CheckBoxesFragment checkBoxesFragment = new CheckBoxesFragment();
                Bundle checkBoxBundle = new Bundle();
                checkBoxBundle.putInt("page_position", i);
                checkBoxBundle.putStringArray("question", questionsWithAnswer[i]);
                checkBoxesFragment.setArguments(checkBoxBundle);
                fragmentArrayList.add(checkBoxesFragment);
            }

            if (questionsWithAnswer[i][6].equals("R"))
            {
                RadioBoxesFragment radioBoxesFragment = new RadioBoxesFragment();
                Bundle radioButtonBundle = new Bundle();
                radioButtonBundle.putStringArray("question", questionsWithAnswer[i]);
                radioButtonBundle.putInt("page_position", i);
                radioBoxesFragment.setArguments(radioButtonBundle);
                fragmentArrayList.add(radioBoxesFragment);
            }
        }

        questionsViewPager = findViewById(R.id.pager);
        questionsViewPager.setOffscreenPageLimit(1);
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        questionsViewPager.setAdapter(mPagerAdapter);


            timerValue = (TextView) findViewById(R.id.timerValue);


        //timer
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

    }
    }

    public void nextQuestion()
    {
        int item = questionsViewPager.getCurrentItem() + 1;
        questionsViewPager.setCurrentItem(item);

        String currentQuestionPosition = String.valueOf(item + 1);

        String questionPosition = currentQuestionPosition + "/" + totalQuestions;
        setTextWithSpan(questionPosition);
    }

    public int getTotalQuestionsSize()
    {
        return questionsWithAnswer.length;
    }


    private void setTextWithSpan(String questionPosition)
    {
        int slashPosition = questionPosition.indexOf("/");

        Spannable spanText = new SpannableString(questionPosition);
        spanText.setSpan(new RelativeSizeSpan(0.7f), slashPosition, questionPosition.length(), 0);
        questionPositionTV.setText(spanText);
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs)
//                    + ":"+ String.format("%03d", milliseconds)
            );
            customHandler.postDelayed(this, 0);
        }

    };

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

    public void setAd(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAds.setText(Html.fromHtml(Ads, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvAds.setText(Html.fromHtml(Ads));
        }

        tvAds.setTextSize(MainActivity.Ads_font);
        tvAds.setMovementMethod(LinkMovementMethod.getInstance());
        tvAds.setSelected(true);

    }
}