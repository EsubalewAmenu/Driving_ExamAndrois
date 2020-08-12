package com.herma.apps.drivertraining.questions.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
//import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.adapter.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This fragment provide the RadioButton/Single Options.
 */
public class PenalityFragment extends Fragment
{
    private FragmentActivity mContext;

    private boolean screenVisible = false;

    private ExpandableListView expandableListView;

    private ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;

    private HashMap<String, List<String>> listDataChild;

    public int type = 0;
    public PenalityFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_penality, container, false);

        getActivity().setTitle(R.string.penality);

        // initializing the views
        expandableListView = rootView.findViewById(R.id.expandableListView);


        // initializing the listeners
        initListeners();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();


        return rootView;
    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getActivity(),
//                        listDataGroup.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataGroup.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
//                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * method to initialize the objects
     */
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), listDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);

    }

    /*
     * Preparing the list data
     *
     * Dummy Items
     */
    private void initListData() {

        // array of strings
        String[] array;

        if(type == 0) {
            // Adding group data
            listDataGroup.add(getString(R.string.on_licence_short));
            listDataGroup.add(getString(R.string.on_licence_long));

            // list of alcohol
            List<String> alcoholList = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_on_licence_short);
            for (String item : array) {
                alcoholList.add(item);
            }

            // list of coffee
            List<String> coffeeList = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_on_licence_long);
            for (String item : array) {
                coffeeList.add(item);
            }

            // Adding child data
            listDataChild.put(listDataGroup.get(0), alcoholList);
            listDataChild.put(listDataGroup.get(1), coffeeList);

        }
        else if(type == 1) {

            listDataGroup.add(getString(R.string._1st));
            listDataGroup.add(getString(R.string._2nd));
            listDataGroup.add(getString(R.string._3rd));
            listDataGroup.add(getString(R.string._4th));
            listDataGroup.add(getString(R.string._5th));
            listDataGroup.add(getString(R.string._6th));

            // list of pasta
            List<String> _1st = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_1st);
            for (String item : array) {
                _1st.add(item);
            }

            // list of cold drinks
            List<String> _2nd = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_2nd);
            for (String item : array) {
                _2nd.add(item);
            }
            // list of cold drinks
            List<String> _3rd = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_3rd);
            for (String item : array) {
                _3rd.add(item);
            }
            // list of cold drinks
            List<String> _4th = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_4th);
            for (String item : array) {
                _4th.add(item);
            }
            // list of cold drinks
            List<String> _5th = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_5th);
            for (String item : array) {
                _5th.add(item);
            }
            // list of cold drinks
            List<String> _6th = new ArrayList<>();
            array = getResources().getStringArray(R.array.string_array_6th);
            for (String item : array) {
                _6th.add(item);
            }

            listDataChild.put(listDataGroup.get(0), _1st);
            listDataChild.put(listDataGroup.get(1), _2nd);
            listDataChild.put(listDataGroup.get(2), _3rd);
            listDataChild.put(listDataGroup.get(3), _4th);
            listDataChild.put(listDataGroup.get(4), _5th);
            listDataChild.put(listDataGroup.get(5), _6th);

        }

        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
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