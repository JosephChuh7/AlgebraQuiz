package com.example.joseph.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IdActivity extends AppCompatActivity {


    private static final String TAG_EXIT = "exit";

    //widgets
    private Button btn_startQuiz;
    private Button btn_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        btn_startQuiz = (Button)findViewById(R.id.btn_startQuiz);
        btn_quit = (Button) findViewById(R.id.btn_quit);

        btn_startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdActivity.this, ExamActivity.class);
                startActivity(intent);
            }
        });

        //退出程序
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdActivity.this, IdActivity.class);
                intent.putExtra(IdActivity.TAG_EXIT, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if(isExit) {
                this.finish();
            }
        }





    }
}
