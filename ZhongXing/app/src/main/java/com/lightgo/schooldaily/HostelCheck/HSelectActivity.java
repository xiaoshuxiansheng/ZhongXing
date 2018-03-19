package com.lightgo.schooldaily.HostelCheck;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightgo.schooldaily.R;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HSelectActivity extends Activity implements View.OnClickListener{
    private LinearLayout layout;
    private Button Mor=null;
    private Button Eve=null;
    private Button Com=null;
    private Button Ele=null;
    private Button mFinish=null;
    private Resources resources=null;
    private Drawable drawable=null;
    private Button myl=null;
    private Button myq=null;
    private Button myw=null;
    private Button mone=null;
    private Button mtwo=null;
    private Button mFlag1 = null;
    private Button mFlag2 = null;
    private Button mFlag3 = null;
    private Button mFlag4 = null;
    private String info="";
    private String a = "";
    private String b = "";
    private String c = "";
    private String d = "";
    private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hselect);
        Init();
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Com.setOnClickListener(this);
        Ele.setOnClickListener(this);
        Eve.setOnClickListener(this);
        myl.setOnClickListener(this);
        myw.setOnClickListener(this);
        myq.setOnClickListener(this);
        Mor.setOnClickListener(this);
        mone.setOnClickListener(this);
        mtwo.setOnClickListener(this);
        mFinish.setOnClickListener(this);
        resources=this.getResources();
        resources.getDrawable(R.drawable.roundedrect);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent = getIntent();
        intent.putExtra("test", "");
        setResult(0, intent);
        HSelectActivity.this.finish();
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            Intent intent = getIntent();
            intent.putExtra("test", "");
            setResult(0, intent);
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
    private void Init(){
        layout = (LinearLayout) findViewById(R.id.pop_layout);
        Com=(Button)findViewById(R.id.bt_Com);
        Ele=(Button)findViewById(R.id.bt_Ele);
        Mor=(Button)findViewById(R.id.bt_Mor);
        Eve=(Button)findViewById(R.id.bt_Eve);
        myl=(Button)findViewById(R.id.bt_yl);
        myw=(Button)findViewById(R.id.bt_yw);
        myq=(Button)findViewById(R.id.bt_yq);
        mone=(Button)findViewById(R.id.bt_one);
        mtwo=(Button)findViewById(R.id.bt_two);
        mFinish=(Button)findViewById(R.id.mFinish);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mFinish:
                int len1=a.length();
                int len2=b.length();
                int len3=c.length();
                int len4=d.length();
                if(len1==0|| len2 ==0||len3==0 ||len4==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HSelectActivity.this);
                    builder.setMessage("请完善信息！");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    Intent intent = getIntent();
                    intent.putExtra("a",a );  //
                    intent.putExtra("b",b);
                    intent.putExtra("c",c);
                    intent.putExtra("d",d);
                    setResult(0, intent);
                    HSelectActivity.this.finish();
                }
                break;
            case R.id.bt_Com:
            case R.id.bt_Ele:
                Button button = (Button)findViewById(view.getId());//根据id获取
                a = button.getText().toString();
                if(mFlag1 != null){
                    drawable = resources.getDrawable(R.drawable.roundedrect);
                    mFlag1.setBackgroundDrawable(drawable);
                }
                drawable = resources.getDrawable(R.drawable.button_selector);
                view.setBackgroundDrawable(drawable);
                mFlag1 = button;
                break;
            case R.id.bt_Mor:
            case R.id.bt_Eve:
                Button button1 = (Button)view;
                b = button1.getText().toString();
                if(mFlag2 != null){
                    drawable = resources.getDrawable(R.drawable.roundedrect);
                    mFlag2.setBackgroundDrawable(drawable);
                }
                drawable = resources.getDrawable(R.drawable.button_selector);
                view.setBackgroundDrawable(drawable);
                mFlag2 = button1;
                break;
            case R.id.bt_yl:
            case R.id.bt_yw:
            case R.id.bt_yq:
                Button button2 =(Button)findViewById(view.getId());//根据id获取
                c = button2.getText().toString();
                if(mFlag3 != null){
                    drawable = resources.getDrawable(R.drawable.roundedrect);
                    mFlag3.setBackgroundDrawable(drawable);
                }
                drawable = resources.getDrawable(R.drawable.button_selector);
                view.setBackgroundDrawable(drawable);
                mFlag3 = button2;
                break;
            case R.id.bt_one:
            case R.id.bt_two:
                Button button3 = (Button)view;
                d = button3.getText().toString();
                if(mFlag4 != null){
                    drawable = resources.getDrawable(R.drawable.roundedrect);
                    mFlag4.setBackgroundDrawable(drawable);
                }
                drawable = resources.getDrawable(R.drawable.button_selector);
                view.setBackgroundDrawable(drawable);
                mFlag4 = button3;
                break;
        }
    }
}

