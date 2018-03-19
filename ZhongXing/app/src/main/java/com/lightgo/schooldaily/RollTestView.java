package com.lightgo.schooldaily;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class RollTestView implements Runnable, Handler.Callback{
    private TextView mTextView;
    private List<String> mData;
    private boolean IsRuning;
    private int Max = 0, Cur;
    private Thread mThread = null;
    private Handler handler;

    public RollTestView(TextView textView) {
        mTextView = textView;
        InitData();
    }

    private void InitData(){
        mData = new ArrayList<>();
        handler = new Handler(this);

        IsRuning = true;
        mThread = new Thread(this);
        mThread.start();
        /*IsRuning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void AddItem(String str){
        synchronized (mData){
            mData.add(str);
            Max = mData.size();
        }
    }

    public void Clear(){
        synchronized (mData) {
            Max = 0;
            mData.clear();
        }
    }

    @Override
    public void run() {
        while(IsRuning){
            synchronized (mData) {
                if(Max != 0) {
                    Cur = ++Cur % Max;

                    Message msg = handler.obtainMessage();
                    msg.sendToTarget();
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        mTextView.setText(mData.get(Cur));
        return false;
    }
}
