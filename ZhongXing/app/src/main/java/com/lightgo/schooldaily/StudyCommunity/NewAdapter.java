package com.lightgo.schooldaily.StudyCommunity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightgo.schooldaily.R;

import java.util.List;

/**
 * Created by Shu on 2017/7/30.
 */

public class NewAdapter extends BaseAdapter {
    private List<NewsBean> mlist;
    private LayoutInflater mInflater;//????
    private int mStar,mEnd;
    public NewAdapter(Context context, List<NewsBean> data){//构造函数初始化
        mlist=data;
        mInflater= LayoutInflater.from(context);

           }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int positon) {
        return positon;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewhodler = null;
        if (view == null) {
            viewhodler = new ViewHolder();
            view = mInflater.inflate(R.layout.sq_comment_item, null);
            viewhodler.img = (ImageView) view.findViewById(R.id.img);
            viewhodler.textinfo = (TextView) view.findViewById(R.id.textinfo);
            viewhodler.time = (TextView) view.findViewById(R.id.time);
            viewhodler.username = (TextView) view.findViewById(R.id.username);
           viewhodler.floor = (TextView) view.findViewById(R.id.floor);

            view.setTag(viewhodler);

        } else {
            viewhodler = (ViewHolder) view.getTag();
        }

        viewhodler.img.setImageResource(R.drawable.ey);

        viewhodler.textinfo.setText(mlist.get(position).TextInfo);
       viewhodler.floor.setText(String.valueOf(position+1));
        viewhodler.time.setText(mlist.get(position).TextTime);
        viewhodler.username.setText(mlist.get(position).UserName);
        return view;
    }
//

    class ViewHolder{
        public TextView textinfo,username,floor,time;
        public ImageView img;
    }
}
