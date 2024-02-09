package com.herma.apps.drivertraining;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.herma.apps.drivertraining.about.About_us;
import com.herma.apps.drivertraining.questions.adaptersj.ViewPagerAdapter;
import com.herma.apps.drivertraining.questions.fragments.CarInternalFragment;
import com.herma.apps.drivertraining.questions.fragments.PlateFragment;
import com.herma.apps.drivertraining.questions.fragments.CauseFragment;
import com.herma.apps.drivertraining.questions.fragments.PenalityFragment;
import com.herma.apps.drivertraining.questions.fragments.QuestionsFragment;
import com.herma.apps.drivertraining.questions.fragments.SettingsActivity;
import com.herma.apps.drivertraining.questions.fragments.SignFragment;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String Ads = "";
    public static int Ads_font = 22;

    QuestionsFragment questionsFragment;
    public ViewPager fragmentViewPager;
    final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    TextView tvAds;

    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    private AdView adView;
    private FrameLayout adContainerView;
    private AtomicBoolean initialLayoutComplete = new AtomicBoolean(false);

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//0
        questionsFragment = new QuestionsFragment();
        fragmentArrayList.add(questionsFragment);

        //1
        SignFragment signFragment = new SignFragment();
        fragmentArrayList.add(signFragment);

        //2
        CauseFragment causeFragment = new CauseFragment();
        fragmentArrayList.add(causeFragment);

        //3
        PenalityFragment lic_penalityFragment = new PenalityFragment();
        lic_penalityFragment.type = 0;
        fragmentArrayList.add(lic_penalityFragment);

        //4
        PenalityFragment way_penalityFragment = new PenalityFragment();
        way_penalityFragment.type = 1;
        fragmentArrayList.add(way_penalityFragment);

        //5
        CarInternalFragment carInternalFragment = new CarInternalFragment();
        fragmentArrayList.add(carInternalFragment);

        //6
        PlateFragment plateFragment = new PlateFragment();
        fragmentArrayList.add(plateFragment);

        fragmentViewPager = findViewById(R.id.pager);
        fragmentViewPager.setOffscreenPageLimit(1);
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        fragmentViewPager.setAdapter(mPagerAdapter);


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

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        () -> {
                            if (!initialLayoutComplete.getAndSet(true)
                                    && googleMobileAdsConsentManager.canRequestAds()) {
                                loadBanner();
                            }
                        });

        tvAds = (TextView) findViewById(R.id.tvAds);
        /// Ad here...
        doApiCall();
        ///

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int QUESTIONNAIRE_REQUEST = 2018;


//        if (requestCode == QUESTIONNAIRE_REQUEST)
//        {
        if (resultCode == RESULT_OK) {


            questionsFragment.queId = data.getStringArrayExtra("queId");
            questionsFragment.timer = data.getStringExtra("timer");
            questionsFragment.current_questions = data.getStringArrayExtra("questions");
            questionsFragment.answerKey = data.getStringArrayExtra("answerKey");
            questionsFragment.response = data.getStringArrayExtra("response");

            questionsFragment.packege = data.getStringExtra("packege");

            questionsFragment.examResult();

//                questionsFragment = new QuestionsFragment();
//                questionsFragment.setArguments(bundle);
//
//                fragmentArrayList.set(0, questionsFragment);
//
//                ViewPagerAdapter queAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
//
//                fragmentViewPager.setOffscreenPageLimit(1);
//                fragmentViewPager.setAdapter(queAdapter);

            fragmentViewPager.setCurrentItem(0);
        }
//        }

//    if (requestCode == QUESTIONNAIRE_REQUEST)
//        {
//            if (resultCode == RESULT_OK)
//            {
//
//                current_questions = data.getStringArrayExtra("questions");
//                answerKey = data.getStringArrayExtra("answerKey");
//                response = data.getStringArrayExtra("response");
//
//                int score = 0;
//
//                if (answerKey.length > 0)
//                {
//                    for (int i = 0; i < answerKey.length; i++) {
//                        if(answerKey[i].equals("***"+response[i]))
//                            score++;
//                    }
//                }
//                txtScore.setText("Score : " + score+"/"+answerKey.length);
//                resultButton.setVisibility(View.VISIBLE);
//                txtScore.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "Completed!!", Toast.LENGTH_LONG).show();
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem moreMenu = menu.findItem(R.id.action_more);
        moreMenu.setVisible(googleMobileAdsConsentManager.isPrivacyOptionsRequired());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.action_rate:
                Toast.makeText(MainActivity.this, "Rate this app :)", Toast.LENGTH_SHORT).show();
                rateApp();
                return true;
            case R.id.action_store:
                Toast.makeText(MainActivity.this, "More apps by us :)", Toast.LENGTH_SHORT).show();
                openUrl("https://play.google.com/store/apps/developer?id=Herma%20plc");
                return true;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), About_us.class));
                return true;
            case R.id.action_exit:

                System.exit(0);
                return true;
            case R.id.privacy_settings:

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
        adView.setAdUnitId(getString(R.string.ad_home_banner));
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exam_questions) {
            fragmentViewPager.setCurrentItem(0);
            MainActivity.this.setTitle(R.string.exam_questions);

        } else if (id == R.id.nav_trafic_sign) {
            fragmentViewPager.setCurrentItem(1);
            MainActivity.this.setTitle(R.string.trafic_sign);

        } else if (id == R.id.nav_cause) {

            fragmentViewPager.setCurrentItem(2);
            MainActivity.this.setTitle(R.string.cause);

        } else if (id == R.id.nav_driving_license) {

            fragmentViewPager.setCurrentItem(3);
            MainActivity.this.setTitle(R.string.menu_drving_license);

        } else if (id == R.id.nav_on_the_way) {

            fragmentViewPager.setCurrentItem(4);
            MainActivity.this.setTitle(R.string.menu_on_the_way);

        } else if (id == R.id.nav_car_internal) {

            fragmentViewPager.setCurrentItem(5);
            MainActivity.this.setTitle(R.string.menu_car_internal);

        } else if (id == R.id.nav_plate_type) {

            fragmentViewPager.setCurrentItem(6);
            MainActivity.this.setTitle(R.string.menu_on_plate_type);

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction("android.intent.action.SEND");
            sendIntent.putExtra("android.intent.extra.SUBJECT", MainActivity.this.getText(R.string.app_name));
            sendIntent.putExtra("android.intent.extra.TEXT", "Downloads \nhttps://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName());
            sendIntent.setType("text/plain");
            MainActivity.this.startActivity(Intent.createChooser(sendIntent, MainActivity.this.getText(R.string.app_name)));

        } else if (id == R.id.nav_rate) {
            Toast.makeText(MainActivity.this, "Rate this app :)", Toast.LENGTH_SHORT).show();
            rateApp();
            return true;
        } else if (id == R.id.nav_store) {
            Toast.makeText(MainActivity.this, "More apps by us :)", Toast.LENGTH_SHORT).show();
            openUrl("https://play.google.com/store/apps/developer?id=Herma%20plc");
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About_us.class));
            return true;
        } else if (id == R.id.nav_exit) {
        System.exit(0);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void doApiCall() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String url ="https://datascienceplc.com/apps/manager/api/items/blog/ad?";

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// Request a string response from the provided URL.

                final int random = new Random().nextInt((99999 - 1) + 1) + 1;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"v=1.0&app_id=1&company_id=1&rand="+random,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    try {
                                        // Getting JSON Array node
                                        JSONObject jsonObj = new JSONObject(response);
                                        Ads = jsonObj.getString("ads");

                                        if(jsonObj.has("font")) Ads_font = jsonObj.getInt("font");
//                                        System.out.println("ads is " + Ads);

                                        setAd();
                                        if(jsonObj.has("open_tab")) {
                                                questionsFragment.youtubeEmbededPlay(jsonObj.getString("open_tab"), jsonObj.getString("play_open"));
                                        }

                                    } catch (final JSONException e) { }

                                }
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error is " + error);
                    }

                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", "bloger_api@datascienceplc.com");//public user
                        params.put("password", "public-password");
                        params.put("Authorization", "Basic YmxvZ2VyX2FwaUBkYXRhc2NpZW5jZXBsYy5jb206cHVibGljLXBhc3N3b3Jk");
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                stringRequest.setTag(this);
// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }, 1500);
    }

    public void setAd(){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvAds.setText(Html.fromHtml(Ads, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvAds.setText(Html.fromHtml(Ads));
            }

            tvAds.setTextSize(Ads_font);
            tvAds.setMovementMethod(LinkMovementMethod.getInstance());
            tvAds.setSelected(true);

        }

}
