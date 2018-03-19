package com.lightgo.schooldaily.HostelCheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lightgo.schooldaily.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HCheck1Activity extends AppCompatActivity implements View.OnClickListener{
    private String mInfo=null;
    private TextView mName;
    private LinearLayout mBack;
    private Button mShut=null;
    private ListView mStuList;
    private int mCount=41;
    private boolean []flag=null;
    List<Map<String, Object>> list =null;
    Map<String, Object> map =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hcheck1);
        Intent intent = getIntent();
        mInfo = intent.getStringExtra("info");
        list = new ArrayList<Map<String, Object>>();
        map = new HashMap<String, Object>();
        mName=(TextView)findViewById(R.id.mName1);
        mBack=(LinearLayout) findViewById(R.id.mBack1);
        mShut=(Button) findViewById(R.id.mShut1);
        mBack.setOnClickListener(this);
        mShut.setOnClickListener(this);
        mName.setText(mInfo);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBack1:
                finish();
                break;
            case R.id.mShut1: {
                AlertDialog.Builder builder = new AlertDialog.Builder(HCheck1Activity.this);
                builder.setMessage("确认提交吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HCheck1Activity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
            break;
        }
    }

}

