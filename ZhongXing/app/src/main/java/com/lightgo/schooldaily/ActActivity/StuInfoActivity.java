package com.lightgo.schooldaily.ActActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightgo.schooldaily.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StuInfoActivity extends Activity implements View.OnClickListener{
    private LinearLayout layout;
    private Button mSava;
    private MyListView mStuList;
    private List<DataBean> mDatas;
    private MyAdapter mAdapter;
    private String[] mStuID;
    private String[] mStuName;
    String[] mStuID1;
    String[] mStuName1;
    String[] Split1;
    private int iCount=0;
    private int xCount;
    private Button bt_selectall;
    private Button bt_cancel;
    private Button bt_deselectall;
    private String mGrade="";
    private String mMajor="";
    private String mClass="";
    private JSONObject LL = null;
    private String pathAPI = "http://47.93.35.144:8888//Home/";
    public static List<String> ids =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stu_info);
        Intent intent=getIntent();
        mGrade=intent.getStringExtra("mGrade");
        mMajor=intent.getStringExtra("mMajor");
        mClass=intent.getStringExtra("mClass");
        ids = new ArrayList<>();
        find();
        GetStuInfo() ;
        click();
        view();
    }
    private void find() {
        layout = (LinearLayout) findViewById(R.id.back);
        mSava=(Button) findViewById(R.id.mSave);
        mStuList=(MyListView) findViewById(R.id.mStuInfo);
        bt_selectall = (Button) findViewById(R.id.bt_selectall);
        bt_cancel = (Button) findViewById(R.id.bt_cancleselectall);
        bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
    }
    private void click() {
        mSava.setOnClickListener(this);
        layout.setOnClickListener(this);
        bt_selectall.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_deselectall.setOnClickListener(this);
    }

    private void view() {
        mDatas = new ArrayList<>();
        for (int i = 0; i <xCount; i++) {
            DataBean dataBean = new DataBean("" + i,""+(i+1),  mStuID1[i], mStuName1[i]);
            mDatas.add(dataBean);
        }
        mAdapter = new MyAdapter(this, mDatas);
        mStuList.setAdapter(mAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = getIntent();
                intent.putExtra("test", "");
                setResult(0, intent);
                finish();
                break;
            case R.id.bt_selectall:
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).isCheck = true;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_deselectall:
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).isCheck = false;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_cancleselectall:
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).isCheck) {
                        mDatas.get(i).isCheck = false;
                    } else {
                        mDatas.get(i).isCheck = true;
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.mSave:
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).isCheck) {
                        ids.add(mStuID1[i]);
                        iCount++;
                    }
                }
                Intent intent1 = getIntent();
                intent1.putExtra("StuCount", iCount);
                setResult(2, intent1);
                StuInfoActivity.this.finish();
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        mGrade = bundle.getString("Grade");
        mMajor = bundle.getString("Major");
        mClass = bundle.getString("Class");
    }
    //上传选择的筛选条件并获取信息
    private void GetStuInfo() {
        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("mGrade", mGrade);
            LL.put("mMajor", mMajor);
            LL.put("mClass", mClass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String s = httpUrlConnection(pathAPI + "AndroidSelectStu", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = s;
                    mHandler1.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                JSONObject q = null, p = null;
                String s = null;
                s = (String) msg.obj;
                try {
                    q = new JSONObject(s);
                    xCount = q.getInt("mCount");
                    mStuID1 = new String[xCount];
                    mStuName1 = new String[xCount];
                    Split1 = new String[xCount];
                    Split1 = s.split(";");
                    for (int k = 0; k < xCount; k++) {
                        p = new JSONObject(Split1[k]);
                        mStuID1[k] = p.getString("StudentId");
                        mStuName1[k] = p.getString("StudentName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }view();
        }
    };
    private String httpUrlConnection(String uriAPI, String requestString) {
        StringBuffer sb = new StringBuffer();
        try {
            //建立连接
            URL url = new URL(uriAPI);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            String param = "UserStr=" + URLEncoder.encode(requestString, "UTF-8");
            //设置请求属性
            //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.connect();

            //建立输出流，并写入数据
            DataOutputStream outputStream = new DataOutputStream(httpConn.getOutputStream());
            outputStream.writeBytes(param);
            outputStream.close();
            //获得响应状态
            int responseCode = httpConn.getResponseCode();

            if (HttpURLConnection.HTTP_OK == responseCode) {//连接成功
                String readLine = new String();
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                return sb.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}

class DataBean {
    public String id;
    public String number;
    public String title;

    public String desc;

    public boolean isCheck;

    public DataBean(String id,String number, String title, String desc) {
        this.id = id;
        this.number=number;
        this.title = title;
        this.desc = desc;
    }
}

class MyAdapter extends BaseAdapter {
    private Context mContext;

    private List<DataBean> mDatas;

    private LayoutInflater mInflater;

    public boolean flage = false;



    public MyAdapter(Context mContext, List<DataBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;

        mInflater = LayoutInflater.from(this.mContext);

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.check_box, null);

            holder = new ViewHolder();

            holder.textNumber = (TextView) convertView.findViewById(R.id.text_number);
            holder.textTitle=(TextView)convertView.findViewById(R.id.text_title);
            holder.textDesc = (TextView) convertView.findViewById(R.id.text_desc);
            holder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkbox_operate_data);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final DataBean dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.textNumber.setText(dataBean.number);
            holder.textTitle.setText(dataBean.title);
            holder.textDesc.setText(dataBean.desc);

            if (flage) {
                holder.checkboxOperateData.setVisibility(View.VISIBLE);
            }

            holder.checkboxOperateData.setChecked(dataBean.isCheck);

            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataBean.isCheck) {
                        dataBean.isCheck = false;
                    } else {
                        dataBean.isCheck = true;
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {

        public CheckBox checkboxOperateData;

        public TextView textNumber;

        public TextView textTitle;

        public TextView textDesc;
    }
}
