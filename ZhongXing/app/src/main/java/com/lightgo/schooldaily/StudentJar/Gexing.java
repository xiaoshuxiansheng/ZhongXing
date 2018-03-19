package com.lightgo.schooldaily.StudentJar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.UserMessage.LoginActivity;

public class Gexing extends Activity implements View.OnClickListener {
    private LinearLayout linearLayout = null;
    private TextView textView = null;
    private EditText edit = null;
    private boolean IsFlag = false;
    private TextView Nunber = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gexing);
        linearLayout = (LinearLayout) findViewById(R.id.ret);
        textView = (TextView) findViewById(R.id.fini);
        Nunber = (TextView)findViewById(R.id.Nunber);
        edit = (EditText)findViewById(R.id.edit);
        linearLayout.setOnClickListener(this);
        textView.setOnClickListener(this);
        Nunber.setText(LoginActivity.UserName);
       if(IsFlag){
           SharedPreferences msharedPreferences = getSharedPreferences("my",
                   Activity.MODE_PRIVATE);
           String hedit = msharedPreferences.getString("hedit", "");
           edit.setText(hedit);
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ret:
                finish();
                break;
            case R.id.fini:
                SharedPreferences mSharedPreferences = getSharedPreferences("my",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor meditor = mSharedPreferences.edit();
                String haha = edit.getText().toString();
                meditor.putString("hedit", haha);
                IsFlag = true;
                meditor.commit();
                Intent intent =getIntent();
                //这里使用bundle绷带来传输数据
                Bundle bundle =new Bundle();
                 //传输的内容仍然是键值对的形式
                bundle.putString("name",edit.getText().toString().trim());//回发的消息,hello world from secondActivity!
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
