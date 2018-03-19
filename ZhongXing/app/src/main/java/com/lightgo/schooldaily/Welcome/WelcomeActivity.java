package com.lightgo.schooldaily.Welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.lightgo.schooldaily.MainActivity;
import com.lightgo.schooldaily.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/2.
 */

public class WelcomeActivity extends Activity implements View.OnTouchListener{
    private ViewPager mViewPager = null;
    private ViewPagerAdapter adapter = null;
    private List<ImageView> lists = null;
    int currentItem = -1;
    private int[] Img = {R.drawable.first,R.drawable.second};

    public static Boolean Is_First = true;

    float startX;
    float startY;//没有用到
    float endX;
    float endY;//没有用到
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if(!Is_First){
            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        lists = new ArrayList<ImageView>();

        for (int i = 0;i < Img.length; i++){
            ImageView iv = new ImageView(this);
            iv.setImageResource(Img[i]);
            lists.add(iv);
        }

        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                endX=event.getX();
                endY=event.getY();
                WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);//获取屏幕的宽度
                Point size = new Point();
                windowManager.getDefaultDisplay().getSize(size);
                int width=size.x;//首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                Log.d("TAGG",currentItem + ";"+lists.size());
                if(currentItem == (lists.size() - 1) && startX - endX >= (width/4)){
                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    this.finish();
                    Is_First = false;
                }
                break;
        }
        return false;
    }


    private class ViewPagerAdapter extends PagerAdapter {
        public int getCount() {//getCount获取条目数量
            return lists.size();//返回当前要滑动图片的个数
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//判断key是否和View相等
        }

        //该方法实现的功能是移除一个给定位置的页面。适配器有责任从容器中删除这个视图。
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(lists.get(position));
        }

        public Object instantiateItem(ViewGroup view, int position) {
            currentItem = position;
            view.addView(lists.get(position));//将指定的position视图添加到view中并显示出来
            return lists.get(position);//返回一个代表新增视图页面的Object（Key），没必要非要返回视图本身
        }
    }
}
