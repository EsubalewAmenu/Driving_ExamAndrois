package com.herma.apps.drivertraining.questions.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.adapter.CauseListS;
import com.herma.apps.drivertraining.others.Sign_Display;

/**
 * This fragment provide the RadioButton/Single Options.
 */
public class CauseFragment extends Fragment
{
    private FragmentActivity mContext;

    private boolean screenVisible = false;

    CauseListS myad1;

    public CauseFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sucact, container, false);

        ListView home_listView = (ListView) rootView.findViewById(R.id.listView);
        this.myad1 = new CauseListS(getActivity());
        home_listView.setAdapter(this.myad1);

        getActivity().setTitle(R.string.cause);

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