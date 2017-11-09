package com.example.joseph.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {


    private LinearEquation le = null;
    private QuadraticEquation qe = null;

    //当前已经做了的题目数
    private int num_question = 0;
    //当前出题模式， false is linear , true is quadratic
    private boolean mode_question = false;
    //默认第二个答题框不可见
    private boolean visible_et_ans_2 = false;

    Long timeA, timeB ,timeC;

    Long timeStart;
    Long timeEnd;

    ArrayList<String> each_li_time = new ArrayList<>();
    ArrayList<String> each_qua_time = new ArrayList<>();


    // num_li[0] 表示作对的题数, 1为做错, 2 为空题
    private int[] num_li = new int[3];
    private int[] num_qua = new int[3];

    //widgets
    private TextView tv_disp_equation;
    private TextView tv_disp_correctness;
    private TextView tv_correct_ans;
    private EditText et_ans_1;
    private EditText et_ans_2;
    private LinearLayout ll_ans_2;
    private Button btn_submit;
    private Button btn_next_question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        //initialize wedgets
        this.tv_disp_equation = (TextView) findViewById(R.id.tv_disp_equation) ;
        this.tv_disp_correctness = (TextView) findViewById(R.id.tv_disp_correctness);
        this.tv_correct_ans = (TextView) findViewById(R.id.tv_correct_ans);
        this.et_ans_1 = (EditText) findViewById(R.id.et_ans_1);
        this.et_ans_2 = (EditText) findViewById(R.id.et_ans_2);
        //this.ll_ans_2 = (LinearLayout) findViewById(R.id.ll_ans_2) ;
        this.btn_submit = (Button) findViewById(R.id.btn_submit);
        this.btn_next_question = (Button) findViewById(R.id.btn_next_question);

        //置空
        this.tv_disp_equation.setText("");
        this.tv_disp_correctness.setText("");


        //默认第二个答题框不可见，因为前五题是线性方程
        this.et_ans_2.setVisibility(View.INVISIBLE);

        //Generate the object of Equations
        //parameters: min and max
        this.le = new LinearEquation(-99, 99);
        this.qe = new QuadraticEquation(-99, 99);

        //btn_submit
        this.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkInputAns()){  //如果用户输入非法字符，或者未输入而点击submit，返回false
                    return;
                }


                //num_question++;
                disableBtn(btn_submit); //一次点击后就设置为不可点击，直至下一题
                showCorrectness();

                timeEnd = System.currentTimeMillis();
                setAvgTime();//计算平均时间
            }
        });
        //btn_next_question
        this.btn_next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //btn_submit处于可点击状态，说明用户直接跳过了此题
                if(btn_submit.isEnabled()){
                    if (mode_question == false) {num_li[2]++;}
                    else {num_qua[2]++;}

                }

                num_question++;
                if(num_question < 10) {
                    nextQuestion();
                    enableBtn(btn_submit);   //进入下一题，重新设置btn_submit可点击
                }
                else {  //做完题了

                    timeC = System.currentTimeMillis();
                    jumpToFinishedActivity();
                }

            }
        });

        timeA = System.currentTimeMillis(); //计时开始
        nextQuestion();
        showTipsAlertDialog();

    }

    private void nextQuestion(){

        //置空
        this.tv_disp_correctness.setText("");
        this.et_ans_1.setText("");
        this.et_ans_2.setText("");
        this.tv_correct_ans.setText("");



        if (this.num_question < 5) {
            this.mode_question = false;   //设置为linear mode

            displayLinear();
        } else {
            //如果已做题目数等于五，mode_quesiton仍处于false，说明刚要开始Quadratic
            if(this.mode_question == false){timeB = System.currentTimeMillis();}
            this.mode_question = true;

            displayQuadratic();
            showEtAns2();//根据有一个解还是两个解来选择是否显示et_ans_2

        }
        //生成新的题目，开始计时
        timeStart = System.currentTimeMillis();
    }



    //Display five linear equations
    private void displayLinear(){
        this.tv_disp_equation.setText(this.le.generateEquation());
    }
    //Display quadratic equations
    private void displayQuadratic(){
        this.tv_disp_equation.setText(this.qe.generateEquation());
    }


    private void showCorrectness(){
        boolean cor = isCorrect();

        countCorWro(cor);  //根据对错，分别计数

        String ans = null;
        if (mode_question == false) {
            ans = this.le.getAnswer();
        } else {
            ans = this.qe.getAnswer(0) + " , " + this.qe.getAnswer(1);
        }
        ans = "Correct Answer is  " + ans;


        if (cor) {  //if the answer is correct
            //this.tv_disp_correctness.setText("Correct !");
            showAlertDialog("Correct !", ans);
        } else {   //if the answer is incorrect
//            this.tv_disp_correctness.setText("Oops !");
//            this.tv_correct_ans.setText(ans);
            showAlertDialog("Oops !", ans);

        }
    }


    private boolean isCorrect(){
        if (this.mode_question == false) {  //Linear Equation mode
            String ans = this.et_ans_1.getText().toString();
            return this.le.isCorrect(ans);

        } else {  //Quadratic Equation mode

            if(this.visible_et_ans_2 == false){  //第二个答题框不可见，说明只有一个解
                String ans = this.et_ans_1.getText().toString();
                return this.qe.isCorrect(ans, ans);

            } else {  //第二个答题框可见，说明有两个解
                String ans1 = this.et_ans_1.getText().toString();
                String ans2 = this.et_ans_2.getText().toString();
                return this.qe.isCorrect(ans1, ans2);
            }
        }

    }


    //Tools
    private void disableBtn(final Button b) {
        b.setEnabled(false);
   }
    private void enableBtn(final Button b){
        b.setEnabled(true);
    }

    //根据有一个解还是两个解来选择是否显示et_ans_2
    private void showEtAns2(){
        if (this.qe.getDelta() == 0) {  //delta为0，只有一个解
            this.et_ans_2.setVisibility(View.INVISIBLE);
        } else{
            this.et_ans_2.setVisibility(View.VISIBLE);
        }
    }
    //检查输入的答案，看是否是数字，若是就返回true
    private boolean checkInputAns(){

        String ans_1 = null;
        String ans_2 = null;
        if (visible_et_ans_2 == true) {  //如果第二个答案框可用
            ans_2 = this.et_ans_2.getText().toString();
        } else {
            ans_2 = "1.0";
        }

        ans_1 = this.et_ans_1.getText().toString();

        if(!ans_1.equals("") && !ans_2.equals("") ) {   //如果第一第二输入框都非空

            try{
                double a1 = Double.parseDouble(ans_1);
                double a2 = Double.parseDouble(ans_2);
            }catch(NumberFormatException e){
                showToast("Please fill the blank with numbers : - )", Toast.LENGTH_SHORT);
                return false;
            }
            return true;
        } else {  //为空则提醒用户填空
            showToast("Please fill the blank : - )", Toast.LENGTH_SHORT);
            return false;
        }

    }

    private void countCorWro(boolean cor){
       if (mode_question == false) { //linear
           if(cor == true)
               num_li[0]++;
           else
               num_li[1]++;
       } else {
           if(cor == true)
               num_qua[0]++;
           else
               num_qua[1]++;
       }
    }

    //跳转到FinishedActivity
    private void jumpToFinishedActivity(){
        Bundle bundle = new Bundle();
        bundle.putLong("timeA", this.timeA);
        bundle.putLong("timeB", this.timeB);
        bundle.putLong("timeC", this.timeC);
        bundle.putIntArray("num_li", copyIntArray(this.num_li));
        bundle.putIntArray("num_qua", copyIntArray(this.num_qua));

        bundle.putStringArrayList("each_li_time", this.each_li_time);
        bundle.putStringArrayList("each_qua_time", this.each_qua_time);

        Intent intent = new Intent(ExamActivity.this, FinishedActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private int[] copyIntArray(int[] arr){
        int[] arr_2 = new int[arr.length];

        for(int i = 0; i < arr.length; i++) {
            arr_2[i] = arr[i];
        }
        return arr_2;
    }

    private void setAvgTime(){
        String time_used = (this.timeEnd - this.timeStart) + "";
        if (this.mode_question == false){
            this.each_li_time.add(time_used);
        } else {
            this.each_qua_time.add(time_used);

        }
    }

    private void showToast(String s, int duration){

        if(duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG){
            duration = Toast.LENGTH_SHORT;
        }

        Toast tot = Toast.makeText(ExamActivity.this,
                s,
                duration );
        tot.show();
    }

    private void showAlertDialog(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle(title);  //设置标题
        builder.setMessage(content);   //设置消息内容

        //添加按钮
        builder.setPositiveButton("Next Question", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn_next_question.performClick();  //触发next_question
            }
        });

        builder.setNegativeButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showTipsAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle("Tips");  //设置标题
        builder.setMessage("Please round your answer to 2 decimal places if it is not integer");   //设置消息内容


        //添加按钮
        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }



}


