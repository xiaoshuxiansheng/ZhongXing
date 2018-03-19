//package com.example.okita14.forum;
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

public class NewsAdapter extends BaseAdapter {
    private List<NewsBean> mlist;
    private LayoutInflater mInflater;//????
    private int mStar,mEnd;
    public NewsAdapter(Context context, List<NewsBean> data){//构造函数初始化
        mlist=data;
        mInflater= LayoutInflater.from(context);

    }
    public void onDateChange(Context context, List<NewsBean> data) {
        mlist=data;
        notifyDataSetChanged();
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
            view = mInflater.inflate(R.layout.sq_item, null);
            viewhodler.img = (ImageView) view.findViewById(R.id.img);
            viewhodler.replynum = (TextView) view.findViewById(R.id.replynum);
            viewhodler.texttitle = (TextView) view.findViewById(R.id.textname);
            viewhodler.textinfo = (TextView) view.findViewById(R.id.textinfo);
            viewhodler.time = (TextView) view.findViewById(R.id.time);
            viewhodler.username = (TextView) view.findViewById(R.id.username);


            view.setTag(viewhodler);

        } else {
            viewhodler = (ViewHolder) view.getTag();
        }
        viewhodler.img.setImageResource(R.drawable.ey);
        viewhodler.replynum.setText(mlist.get(position).ReplyNum);
        viewhodler.textinfo.setText(mlist.get(position).TextInfo);
        viewhodler.texttitle.setText(mlist.get(position).TextTitle);
        viewhodler.time.setText(mlist.get(position).TextTime);
        viewhodler.username.setText(mlist.get(position).UserName);
        return view;
    }


    class ViewHolder{
        public TextView texttitle,textinfo,username,time,replynum;
        public ImageView img;
    }
}
