package com.herma.apps.drivertraining.questions;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.herma.apps.drivertraining.GoogleMobileAdsConsentManager;
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

    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    private AdView adView;
    private AtomicBoolean initialLayoutComplete = new AtomicBoolean(false);
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private FrameLayout adContainerView;

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

            parsingData();

            tvAds = (TextView) findViewById(R.id.tvAds);

            setAd();


        adContainerView = findViewById(R.id.ad_view_container);

        googleMobileAdsConsentManager =
                GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        googleMobileAdsConsentManager.gatherConsent(
                this,
                consentError -> {
                    if (consentError != null) {
                        // Consent not obtained in current session.
                        Log.w(
                                "consentError",
                                String.format("%s: %s", consentError.getErrorCode(), consentError.getMessage()));
                    }

                    if (googleMobileAdsConsentManager.canRequestAds()) {
                        initializeMobileAdsSdk();
                    }

                    if (googleMobileAdsConsentManager.isPrivacyOptionsRequired()) {
                        // Regenerate the options menu to include a privacy setting.
                        invalidateOptionsMenu();
                    }
                });

        // This sample attempts to load ads using consent obtained in the previous session.
        if (googleMobileAdsConsentManager.canRequestAds()) {
            initializeMobileAdsSdk();
        }
        adContainerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        () -> {
                            if (!initialLayoutComplete.getAndSet(true)
                                    && googleMobileAdsConsentManager.canRequestAds()) {
                                loadBanner();
                            }
                        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getApplicationContext(), getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(QuestionActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
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
                    max+=1;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem moreMenu = menu.findItem(R.id.action_more);
        moreMenu.setVisible(googleMobileAdsConsentManager.isPrivacyOptionsRequired());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuItemView = findViewById(item.getItemId());
        PopupMenu popup = new PopupMenu(this, menuItemView);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(
                popupMenuItem -> {
                    if (popupMenuItem.getItemId() == R.id.privacy_settings) {
                        // Handle changes to user consent.
                        googleMobileAdsConsentManager.showPrivacyOptionsForm(
                                this,
                                formError -> {
                                    if (formError != null) {
                                        Toast.makeText(this, formError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        return true;
                    }
                    return false;
                });
        return super.onOptionsItemSelected(item);
    }


    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    private void loadBanner() {
        // Create a new ad view.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_question_banner));
        adView.setAdSize(getAdSize());

        // Replace ad container with new ad view.
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });

        // Load an ad.
        if (initialLayoutComplete.get()) {
            loadBanner();
        }
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}