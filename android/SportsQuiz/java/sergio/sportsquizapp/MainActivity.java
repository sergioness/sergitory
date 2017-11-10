package sergio.geoquizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    private Button TrueButton;
    private Button FalseButton;
    private Button CheatButton;
    private ImageButton NextButton;
    private ImageButton PrevButton;
    private TextView QuestionView;
    private TrueFalse[] Questions={
            new TrueFalse(R.string.basketball,false),
            new TrueFalse(R.string.football,true),
            new TrueFalse(R.string.liecht,true),
            new TrueFalse(R.string.olympics,true),
            new TrueFalse(R.string.golf,true)};
    private int current=0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_CHEATED = "cheated";

    private boolean IsCheater;

    private boolean[] isCheated={false, false, false, false, false};

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, current);
        savedInstanceState.putBoolean(KEY_CHEATER,IsCheater);
        savedInstanceState.putBooleanArray(KEY_CHEATED,isCheated);
    }

    private void updateQuestion() {
        int question = Questions[current].getQuestion();
        QuestionView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = Questions[current].isTrueQuestion();
        int messageResId = 0;
        if(IsCheater)
            isCheated[current]=true;
        if(isCheated[current])
            messageResId=R.string.judgment_toast;
        else
        {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.toastTrue;
            } else {
                messageResId = R.string.toastFalse;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void nextQuestion()
    {

        current = (current + 1) % Questions.length;
        IsCheater=false;
        updateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        QuestionView = (TextView) findViewById(R.id.textView);
        QuestionView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nextQuestion();
            }
        });

        TrueButton=(Button)findViewById(R.id.buttonTrue);
        TrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(true);
                nextQuestion();
            }
        });
        FalseButton=(Button)findViewById(R.id.buttonFalse);
        FalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(false);
                nextQuestion();
            }
        });
        NextButton=(ImageButton)findViewById(R.id.nextButton);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        PrevButton=(ImageButton)findViewById(R.id.prevButton);
        PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current==0)
                    current=Questions.length-1;
                else
                    current = (current - 1) % Questions.length;
                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            current = savedInstanceState.getInt(KEY_INDEX, 0);
            IsCheater = savedInstanceState.getBoolean(KEY_CHEATER,false);
            isCheated = savedInstanceState.getBooleanArray(KEY_CHEATED);
        }

        CheatButton = (Button)findViewById(R.id.cheatButton);
        CheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CheatActivity.class);
                boolean answerIsTrue = Questions[current].isTrueQuestion();
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(intent,0);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
        {
            return;
        }
        IsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }
}
