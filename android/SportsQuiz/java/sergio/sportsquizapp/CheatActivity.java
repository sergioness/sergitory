package sergio.geoquizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends Activity
{
    private TextView AnswerTextView;
    private Button ShowAnswer;
    private TextView buildVersionTextView;

    public static final String EXTRA_ANSWER_IS_TRUE="sergio.geoquizapp.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN ="sergio.geoquizapp.answer_shown";

    private static final String KEY_SHOWN = "shown";
    private static final String KEY_ANSWER = "answer";
    private boolean isAnswerShown;
    private boolean AnswerIsTrue;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_SHOWN,isAnswerShown);
        savedInstanceState.putBoolean(KEY_ANSWER,AnswerIsTrue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        AnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        AnswerTextView=(TextView) findViewById(R.id.answerTextView);

        buildVersionTextView = (TextView) findViewById(R.id.buildVersionTextView);

        isAnswerShown = false;
        if(savedInstanceState!=null)
        {
            isAnswerShown = savedInstanceState.getBoolean(KEY_SHOWN,false);
            if(isAnswerShown)
            {
                if(savedInstanceState.getBoolean(KEY_ANSWER,false))
                    AnswerTextView.setText(R.string.trueButton);
                else
                    AnswerTextView.setText(R.string.falseButton);
            }
        }
        setAnswerShownResult(isAnswerShown);

        ShowAnswer=(Button)findViewById(R.id.answerButton);
        ShowAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(AnswerIsTrue)
                    AnswerTextView.setText(R.string.trueButton);
                else
                    AnswerTextView.setText(R.string.falseButton);

                isAnswerShown=true;
                setAnswerShownResult(isAnswerShown);
            }
        });
    }

    private void setAnswerShownResult(boolean shown)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,shown);
        setResult(RESULT_OK,data);
    }
}
