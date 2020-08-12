package com.herma.apps.drivertraining.questions.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.herma.apps.drivertraining.R;
import com.herma.apps.drivertraining.adapter.Common;
import com.herma.apps.drivertraining.questions.AnswersActivity;
import com.herma.apps.drivertraining.questions.DB;
import com.herma.apps.drivertraining.questions.QuestionActivity;

import java.io.IOException;

/**
 * This fragment provide the RadioButton/Single Options.
 */
public class QuestionsFragment extends Fragment
{
    private static final int QUESTIONNAIRE_REQUEST = 2018;
    Button resultButton;
    public String[] answerKey, response, current_questions, queId;
    public String timer, packege;
    String[][] questionsWithAnswer;
    TextView txtScore, doneQuestions, percentAnsQue;
    ImageView imgBadge;
    ProgressBar unseenProgressBar;
    private FragmentActivity mContext;

    private boolean screenVisible = false;
    DB db;

    int countAll = 0, unseen=0;

    public QuestionsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_main, container, false);

        Button questionnaireButton = rootView.findViewById(R.id.questionnaireButton);
        Button btnExamList = rootView.findViewById(R.id.btnExamList);
        resultButton = rootView.findViewById(R.id.resultButton);
        txtScore = (TextView) rootView.findViewById(R.id.txtScore);
        doneQuestions = (TextView) rootView.findViewById(R.id.doneQuestions);
        percentAnsQue = (TextView) rootView.findViewById(R.id.percentAnsQue);
        imgBadge = (ImageView) rootView.findViewById(R.id.imgBadge);

        unseenProgressBar=(ProgressBar) rootView.findViewById(R.id.unseenProgressBar); // initiate the progress bar
        unseenProgressBar.setMax(100); // 100 maximum value for the progress bar

        open("read", "full.hrm");


        try{ Cursor c = db.doExcute("SELECT COUNT(*) FROM que;");
        if(c.moveToFirst()) countAll = c.getInt(0); }catch (Exception lk){countAll = 0;}

        try{Cursor cc = db.doExcute("SELECT COUNT(*) FROM que where seen=0;");
        if(cc.moveToFirst()) unseen = cc.getInt(0);}catch (Exception lk){unseen = countAll;}
db.close();
    int seenPer = 100-((unseen*100)/countAll);

        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int tot_score = pre.getInt("tot_score", 0);
        int tot_asked = pre.getInt("tot_asked", 0);

        int totPerc = 0;
        try{ totPerc = (100*tot_score)/tot_asked; }catch (Exception lk){}

        unseenProgressBar.setProgress(seenPer);
        doneQuestions.setText("(ከጠቅላላው) እስካሁን የሰሩት ፡ " + seenPer + "%");
        percentAnsQue.setText("እስካሁን ከተጠየቁት የመለሱት ፡ " + totPerc + "%");

//        getActivity().setTitle(R.string.exam_questions);

        questionnaireButton.setOnClickListener(v -> {
            resultButton.setVisibility(View.GONE);
            txtScore.setVisibility(View.GONE);

                    Intent questions = new Intent(getActivity(), QuestionActivity.class);
                    questions.putExtra("type", "rand");
                    startActivityForResult(questions, QUESTIONNAIRE_REQUEST);

        });
        btnExamList.setOnClickListener(v -> {
            resultButton.setVisibility(View.GONE);
            txtScore.setVisibility(View.GONE);

            Intent examList = new Intent(getActivity(), ExamListActivity.class);
            startActivityForResult(examList, QUESTIONNAIRE_REQUEST);

        });

        resultButton.setOnClickListener(v -> {
            Intent questions = new Intent(getActivity(), AnswersActivity.class);

            questions.putExtra("queId", queId);
            questions.putExtra("answerKey", answerKey);
            questions.putExtra("response", response);
            questions.putExtra("questions", current_questions);

            startActivity(questions);
        });

        return rootView;
    }
    public void examResult(){

        int score = 0;

        if (answerKey.length > 0)
        {
            for (int i = 0; i < answerKey.length; i++) {
                if(answerKey[i].equals("***"+response[i]))
                    score++;
            }
        }

        int perc = (100*score)/answerKey.length;

        String rank;
        if(perc >= 74) {rank = "አልፈዋል!";
            txtScore.setBackgroundColor(Color.GREEN);

        }
        else {
            rank = "አላለፉም!";
            txtScore.setBackgroundColor(Color.RED);
            txtScore.setTextColor(Color.WHITE);
        }

        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int tot_score = pre.getInt("tot_score", 0);
        int tot_asked = pre.getInt("tot_asked", 0);
        pre.edit().putInt("tot_score", (tot_score+score)).apply();
        pre.edit().putInt("tot_asked", (tot_asked+answerKey.length)).apply();

        int totPerc = (100*(score+tot_score))/(answerKey.length+tot_asked);


        percentAnsQue.setText("እስካሁን ከተጠየቁት የመለሱት ፡ " + totPerc + "%");

        resultButton.setVisibility(View.VISIBLE);
        txtScore.setVisibility(View.VISIBLE);

        txtScore.setText("ውጤት : " + score+"/"+answerKey.length + " (" + perc + "%) " + rank+"\nየፈጀብዎት ጊዜ :- "+ timer);
        if(perc <74)
            imgBadge.setImageResource(R.drawable.badge_null);
        else if(perc < 77)
            imgBadge.setImageResource(R.drawable.badge_null);
        else if(perc < 85)
            imgBadge.setImageResource(R.drawable.badge_star);
        else if(perc < 90)
            imgBadge.setImageResource(R.drawable.badge_first);
        else if(perc < 95)
            imgBadge.setImageResource(R.drawable.badge_gold);
        else if(perc <= 100)
            imgBadge.setImageResource(R.drawable.badge_platinum);

        open("read", "full.hrm");

        Cursor c = db.doExcute("SELECT COUNT(*) FROM que;");
        if(c.moveToFirst()) countAll = c.getInt(0);

        Cursor cc = db.doExcute("SELECT COUNT(*) FROM que where seen=0;");
        if(cc.moveToFirst()) unseen = cc.getInt(0);
        db.close();
        int seenPer = 100-((unseen*100)/countAll);
        unseenProgressBar.setProgress(seenPer); // 50 default progress value for the progress bar
        doneQuestions.setText("(ከጠቅላላው) እስካሁን የሰሩት ፡ " + seenPer + "%");


//        Toast.makeText(getActivity(), "Completed!!", Toast.LENGTH_LONG).show();

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

//        mContext = getActivity();
        if (getArguments() != null)
        {
//            radioButtonTypeQuestion = getArguments().getStringArray("question");
//            questionId = String.valueOf(radioButtonTypeQuestion != null ? radioButtonTypeQuestion[0] : 0);
//            currentPagePosition = getArguments().getInt("page_position") + 1;
//        }
        }
    }
//
//    @Override
//    protected void supper.onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == QUESTIONNAIRE_REQUEST)
//        {
//            if (resultCode == RESULT_OK)
//            {
//
//                current_questions = data.getStringArrayExtra("questions");
//                answerKey = data.getStringArrayExtra("answerKey");
//                response = data.getStringArrayExtra("response");
////                questionsWithAnswer = data.getStringArrayExtra("questionsWithAnswer");
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
//    }


    public void open(String write, String db_name) {

        db = new DB(getActivity(), db_name);
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
    /**
     * Check if there is the network connectivity
     *
     * @return true if connected to the network
     */
    private boolean isOnline() {
//        // Get a reference to the ConnectivityManager to check the state of network connectivity
//        ConnectivityManager connectivityManager = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Get details on the currently active default data network
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
        return true;
    }

    /**
     * Show a dialog when there is no internet connection
     *
     * @param isOnline true if connected to the network
     */
    private void showNetworkDialog(final boolean isOnline, int title, int message, String packege) {

        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
        // Set an Icon and title, and message

        if (!isOnline) {
            builder.setIcon(R.drawable.ic_warning);
        }
        builder.setTitle(getString(title));
        builder.setMessage(getString(message));
        builder.setPositiveButton(getString(R.string.go_to_exam_question), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent questions = new Intent(getActivity(), QuestionActivity.class);
                questions.putExtra("type", "rand");
                questions.putExtra("packege", packege);
                startActivityForResult(questions, QUESTIONNAIRE_REQUEST);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * Show a dialog when there is no internet connection
     *
     * @param title true if connected to the network
     */
    private void showAwardDialog(int title, String rank, int message) {

        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
        // Set an Icon and title, and message

        builder.setTitle(getString(title));
        builder.setMessage(rank+"\n"+getString(message));

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private boolean isValidPhone(String phone) {
        if(!java.util.regex.Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
}