package com.lightgo.schooldaily.ActActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lightgo.schooldaily.R;

public class EditActivity extends Activity implements View.OnClickListener{
    private LinearLayout layout;
    private EditText entryText=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);
        entryText = (EditText) findViewById(R.id.info);
        layout = (LinearLayout) findViewById(R.id.confirm);
        layout.setOnClickListener(this);
    }
    public void onClick(View view) {
        Intent intent =getIntent();
        intent.putExtra("name",entryText.getText().toString().trim());
        setResult(1,intent);
        finish();
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

}
