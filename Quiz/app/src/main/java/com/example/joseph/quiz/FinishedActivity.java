package com.example.joseph.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class FinishedActivity extends AppCompatActivity {

    private String TotalTime;
    private String LiAvgTime;
    private String QuaAvgTime;

    private int[] num_li = new int[3];
    private int[] num_qua = new int[3];

    private ArrayList<String> each_li_time;
    private ArrayList<String> each_qua_time;

    //Widgets

    private TextView tv_time_usage;
    private TextView tv_li_avg_time;
    private TextView tv_qu_avg_time;
    private TextView tv_li_cor;
    private TextView tv_li_wro;
    private TextView tv_li_giveup;
    private TextView tv_qu_giveup;
    private TextView tv_qu_cor;
    private TextView tv_qu_wro;

    private Button btn_quiz_again;
    private Button btn_back_home;

    //btn_back_home被点击的次数
    private int num_btn_back_click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        initializeWidgets();
        resovleIntentData(getIntent());
        showData();

    }


    //初始化组件
    private void initializeWidgets(){
        this.tv_time_usage = (TextView) findViewById(R.id.tv_time_usage);
        this.tv_li_avg_time = (TextView) findViewById(R.id.tv_li_avg_time);
        this.tv_qu_avg_time = (TextView) findViewById(R.id.tv_qu_avg_time);
        this.tv_li_cor = (TextView) findViewById(R.id.tv_li_cor);
        this.tv_li_wro = (TextView) findViewById(R.id.tv_li_wro);
        this.tv_li_giveup = (TextView) findViewById(R.id.tv_li_giveup);
        this.tv_qu_cor = (TextView) findViewById(R.id.tv_qu_cor);
        this.tv_qu_wro = (TextView) findViewById(R.id.tv_qu_wro);
        this.tv_qu_giveup = (TextView) findViewById(R.id.tv_qu_giveup);

        this.btn_back_home = (Button) findViewById(R.id.btn_back_home);
        this.btn_quiz_again = (Button) findViewById(R.id.btn_quiz_agian);

        //跳回主界面
        this.btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                num_btn_back_click++;
                if(num_btn_back_click < 2){
                    Toast tot = Toast.makeText(FinishedActivity.this,
                            "Click again to go back to homepage !",
                            Toast.LENGTH_LONG);
                    tot.show();
                return;
                }
                Intent intent = new Intent(FinishedActivity.this, IdActivity.class);
                startActivity(intent);
            }
        });
        //do quiz again
        this.btn_quiz_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishedActivity.this, ExamActivity.class);
                startActivity(intent);
            }
        });


    }



    private void resovleIntentData(Intent intent){
        Bundle bundle = intent.getExtras();

        long timeA = bundle.getLong("timeA");
        long timeB = bundle.getLong("timeB");
        long timeC = bundle.getLong("timeC");



        intArrayHdCp(bundle.getIntArray("num_li"), this.num_li);
        intArrayHdCp(bundle.getIntArray("num_qua"), this.num_qua);

//        this.num_li = bundle.getIntArray("num_li");
//        this.num_qua = bundle.getIntArray("num_qua");
        this.each_li_time = bundle.getStringArrayList("each_li_time");
        this.each_qua_time = bundle.getStringArrayList("each_qua_time");


        this.TotalTime = convertLongTime2String(timeC - timeA);
        this.LiAvgTime = convertLongTime2String(calcAvgTime(this.each_li_time));  //平均时间，所以除以五
        this.QuaAvgTime = convertLongTime2String(calcAvgTime(this.each_qua_time));

    }


    private void intArrayHdCp(int[] arr, int[] arr_2){
        for(int i = 0; i < arr.length; i++) {
            arr_2[i] = arr[i];
        }
    }



    //将得到的Data展示出来
    private void showData(){
        this.tv_time_usage.setText(this.TotalTime);
        this.tv_li_avg_time.setText(this.LiAvgTime);
        this.tv_qu_avg_time.setText(this.QuaAvgTime);

        this.tv_li_cor.setText(this.num_li[0] + "");
        this.tv_li_wro.setText(this.num_li[1] + "");
        this.tv_li_giveup.setText(this.num_li[2] + "");
        this.tv_qu_cor.setText(this.num_qua[0] + "");
        this.tv_qu_wro.setText(this.num_qua[1] + "");
        this.tv_qu_giveup.setText(this.num_qua[2] + "");


    }

    //将Long格式的时间转换为形如 3'44"
    private String convertLongTime2String(long duration){

        return new SimpleDateFormat("mm:ss.SSS").format(duration);

    }

    //从ExamActivity传递来的String ArrayList 解析平均时间
    private long calcAvgTime(ArrayList<String> each_time){

        if (each_time.size() == 0) {
            return (long)0;
        }

        long totalTime = 0;
        for(String time : each_time) {
            totalTime = totalTime + Long.parseLong(time);
        }
        return totalTime / each_time.size();
    }

}
