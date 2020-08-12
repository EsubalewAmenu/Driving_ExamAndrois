package com.herma.apps.drivertraining.questions.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.herma.apps.drivertraining.R;

/**
 * This fragment provide the RadioButton/Single Options.
 */
public class PlateFragment extends Fragment
{
    private FragmentActivity mContext;

    private boolean screenVisible = false;


    public PlateFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.plate, container, false);

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