package com.lightgo.schooldaily.UserMessage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.lightgo.schooldaily.R;

public class BslcActivity extends Activity {

    private String total = "";
    LinearLayout ll = null;
    private Resources resources = null;
    private Drawable btnDrawable = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bslc);

        resources = this.getResources();
        ll = (LinearLayout)findViewById(R.id.mPic);
        Intent intent = getIntent();
        total = intent.getStringExtra("total");
        switch (total){
            case "llone":
                btnDrawable = resources.getDrawable(R.drawable.qj);
                ll.setBackgroundDrawable(btnDrawable);
                break;
            case "lltwo":
                btnDrawable = resources.getDrawable(R.drawable.xsbd);
                ll.setBackgroundDrawable(btnDrawable);
                break;
            case "llthree":
                btnDrawable = resources.getDrawable(R.drawable.lx);
                ll.setBackgroundDrawable(btnDrawable);
                break;
            case "llfour":
                btnDrawable = resources.getDrawable(R.drawable.xiux);
                ll.setBackgroundDrawable(btnDrawable);
                break;
            case "llfive":
                btnDrawable = resources.getDrawable(R.drawable.fx);
                ll.setBackgroundDrawable(btnDrawable);
                break;
            default:
                break;
        }
    }
}
