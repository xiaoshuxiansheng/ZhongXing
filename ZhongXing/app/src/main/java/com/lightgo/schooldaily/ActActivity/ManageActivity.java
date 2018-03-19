package com.lightgo.schooldaily.ActActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lightgo.schooldaily.CheckActivity.DailyCheckActivity;
import com.lightgo.schooldaily.HostelCheck.HostelActivity;
import com.lightgo.schooldaily.R;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mActivityButton;      //活动按钮图片
    private ImageButton mKnowingButton;       //查寝按钮图片
    private ImageButton mBackButton;          //返回按钮图片
    private ImageButton mCallnamesButton;     //点名按钮图片
    private LinearLayout mActivityLilayout,mKnowingLilayout,mBackLilayout,mCallnamesLilayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        inti();
    }
    private void inti(){
        mActivityButton= (ImageButton) findViewById(R.id.id_ActivityImg);
        mBackButton= (ImageButton) findViewById(R.id.id_BackImg);
        mCallnamesButton= (ImageButton) findViewById(R.id.id_CallnamesImg);
        mKnowingButton= (ImageButton) findViewById(R.id.id_KnowingImg);

        mActivityLilayout= (LinearLayout) findViewById(R.id.id_Activity);
        mKnowingLilayout= (LinearLayout) findViewById(R.id.id_Knowing);
        mBackLilayout= (LinearLayout) findViewById(R.id.id_Back);
        mCallnamesLilayout=(LinearLayout) findViewById(R.id.id_Callnames);

        mActivityButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        mCallnamesButton.setOnClickListener(this);
        mKnowingButton.setOnClickListener(this);

        mActivityLilayout.setOnClickListener(this);
        mKnowingLilayout.setOnClickListener(this);
        mBackLilayout.setOnClickListener(this);
        mCallnamesLilayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_Callnames:
                mCallnamesButton.setImageResource(R.drawable.callnameimg2);
                Intent callNameIntent = new Intent(ManageActivity.this, DailyCheckActivity.class);
                startActivity(callNameIntent);
                break;
            case R.id.id_Back:
                mBackButton.setImageResource(R.drawable.backimg2);
                Intent backIntent = new Intent(ManageActivity.this, DailyCheckActivity.class);
                startActivity(backIntent);
                break;
            case R.id.id_Knowing:
                mKnowingButton.setImageResource(R.drawable.knowingimg2);
                Intent knowingIntent = new Intent(ManageActivity.this, HostelActivity.class);
                startActivity(knowingIntent);
                break;
            case R.id.id_Activity:
                mActivityButton.setImageResource(R.drawable.activityimg2);
                Intent activityIntent = new Intent(ManageActivity.this, ActShowActivity.class);
                startActivity(activityIntent);
                break;
        }
    }
}
