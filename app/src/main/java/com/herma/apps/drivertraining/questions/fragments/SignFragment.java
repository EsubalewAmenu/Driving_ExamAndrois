package com.herma.apps.drivertraining.questions.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.others.Sign_Display;
import com.herma.apps.drivertraining.others.Sign_Home;

/**
 * This fragment provide the RadioButton/Single Options.
 */
public class SignFragment extends Fragment
{
    private FragmentActivity mContext;

    private boolean screenVisible = false;


    public SignFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sign_home, container, false);


        ((Button) rootView.findViewById(R.id.info_s)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "i");
                startActivity(questions);

//                Intent questions = new Intent(getActivity(), SignDisplayFragment.class);
//                questions.putExtra("where", "i");
//                startActivity(questions);

            }
        });
        ((Button) rootView.findViewById(R.id.first_s)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), First_signs.class));


                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "f");
                startActivity(questions);

            }
        });
        ((Button) rootView.findViewById(R.id.must_s)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Must_signs.class));
                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "m");
                startActivity(questions);

            }
        });
        ((Button) rootView.findViewById(R.id.war_s)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), War_signs.class));
                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "w");
                startActivity(questions);
            }
        });
        ((Button) rootView.findViewById(R.id.light_s)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Light_signs.class));


                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "l");
                startActivity(questions);
            }
        });
        ((Button) rootView.findViewById(R.id.pro_s)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (Sign_Home.this.mInterstitialAd.isLoaded() && ((int) (Math.random() * 100.0d)) > 35) {
//                    Sign_Home.this.mInterstitialAd.show();
//                }
//                Sign_Home.this.startActivity(new Intent(Sign_Home.this.getApplicationContext(), Pro_signs.class));

                Intent questions = new Intent(getActivity(), Sign_Display.class);
                questions.putExtra("where", "p");
                startActivity(questions);

//                mContext.fragmentViewPage

//                getActivity().get
            }
        });

        return rootView;
    }

    /*This method get called only when the fragment get visible, and here states of Radio Button(s) retained*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
        {
            screenVisible = true;

        }
    }

    private String getTheStateOfRadioBox(String[] data)
    {
        return "";//appDatabase.getQuestionChoicesDao().isChecked(data[0], data[1]);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        if (getArguments() != null)
        {
//            radioButtonTypeQuestion = getArguments().getStringArray("question");
//            questionId = String.valueOf(radioButtonTypeQuestion != null ? radioButtonTypeQuestion[0] : 0);
//            currentPagePosition = getArguments().getInt("page_position") + 1;
//        }
        }
    }
}