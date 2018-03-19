package com.lightgo.schooldaily.CollegeIntroduce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.UserMessage.BslcActivity;

public class ProcessActivity extends Activity implements View.OnClickListener{
    private RelativeLayout llone,lltwo,llthree,llfour,llfive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process);

        llone = (RelativeLayout)findViewById(R.id.llone);
        lltwo = (RelativeLayout) findViewById(R.id.lltwo);
        llthree = (RelativeLayout) findViewById(R.id.llthree);
        llfour = (RelativeLayout) findViewById(R.id.llfour);
        llfive = (RelativeLayout) findViewById(R.id.llfive);

        llone.setOnClickListener(this);
        lltwo.setOnClickListener(this);
        llthree.setOnClickListener(this);
        llfour.setOnClickListener(this);
        llfive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ProcessActivity.this, BslcActivity.class);
        switch (v.getId()){
            case R.id.llone:
                intent.putExtra("total","llone");
                break;
            case R.id.lltwo:
                intent.putExtra("total","lltwo");
                break;
            case R.id.llthree:
                intent.putExtra("total","llthree");
                break;
            case R.id.llfour:
                intent.putExtra("total","llfour");
                break;
            case R.id.llfive:
                intent.putExtra("total","llfive");
            default:
                break;
        }
        startActivity(intent);
    }
}
