package com.herma.apps.drivertraining.questions;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.herma.apps.drivertraining.R;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnswersActivity extends AppCompatActivity
{
    Context context;
    LinearLayout resultLinearLayout;

    String[] answerKey, response, questions, queId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        context = this;

        resultLinearLayout = findViewById(R.id.resultLinearLayout);
        toolBarInit();

        getResult();
    }

    private void toolBarInit()
    {
        Toolbar answerToolBar = findViewById(R.id.answerToolbar);
        answerToolBar.setNavigationIcon(R.drawable.ic_arrow_back);
        answerToolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /*After, getting all result you can/must delete the saved results
    although we are clearing the Tables as soon we start the QuestionActivity.*/
    private void getResult()
    {
        Completable.fromAction(() -> {
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onComplete()
                    {
                        MakeResultView();
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }
                });
    }

    /*Here, JSON got created and send to make Result View as per Project requirement.
    * Alternatively, in your case, you make Network-call to send the result to back-end.*/
    private void MakeResultView()
    {

        if (getIntent().getExtras() != null)
        {
            queId = getIntent().getStringArrayExtra("queId");
            questions = getIntent().getStringArrayExtra("questions");
            questions = getIntent().getStringArrayExtra("questions");
            answerKey = getIntent().getStringArrayExtra("answerKey");
            response = getIntent().getStringArrayExtra("response");
        }

        questionsAnswerView();
    }

    private void questionsAnswerView()
    {
        if (answerKey.length > 0)
        {
                for (int i = 0; i < answerKey.length; i++)
                {
                    // question will be here....
                    String question = "#"+(i+1) + " " + questions[i];

                    String correct_answer = answerKey[i];

                    TextView questionTextView = new TextView(context);
                    questionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    questionTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    questionTextView.setPadding(40, 30, 16, 30);
                    questionTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    questionTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    questionTextView.setTypeface(null, Typeface.BOLD);

                    questionTextView.setText(question + "\n\n "+ "ትክክለኛ መልስ * " +correct_answer.substring(3));

                    resultLinearLayout.addView(questionTextView);


                    if(!answerKey[i].equals("***"+response[i]))
                    {
                        String answer = response[i];
                        String formattedAnswer = "የሰጡት መልስ " + "• " + answer; // alt + 7 --> •

                        TextView answerTextView = new TextView(context);
                         answerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        answerTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        answerTextView.setPadding(60, 30, 16, 30);
                        answerTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        answerTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                        answerTextView.setText(formattedAnswer);

                        View view = new View(context);
                        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));

                        resultLinearLayout.addView(answerTextView);
                        resultLinearLayout.addView(view);
                    }
                }
        }
    }
}