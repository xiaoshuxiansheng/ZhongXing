package com.lightgo.schooldaily.CheckActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DCheckActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private String mInfo=null;
    private TextView mName;
    private LinearLayout mBack;
    private Button mShut=null;
    private MyListView mStuList;
    private String mGrade;
    private String mMajor;
    private String mClass;
    private int mCount=0;
    private TextView mCheckCount=null;
    private TextView mClassCount=null;

    private boolean []flag=null;
    private String[] mStuID=null;
    private String[] mStuName=null;
    private String[] Split=null;
    private int j=0;
    private JSONObject LL=null;
    private JSONObject StuList=null;
    private String pathAPI = "http://47.93.35.144:8888/Home/";
    private HashMap newmap=null;
    private String allCount=null;
    private String mTime=null;
    private String StuData="";
    private int getCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dcheck);
        LL = new JSONObject();
        StuList=new JSONObject();
        mName=(TextView)findViewById(R.id.mName);
        mBack=(LinearLayout) findViewById(R.id.mBack);
        mShut=(Button) findViewById(R.id.mShut);
        mStuList=(MyListView) findViewById(R.id.mStuInfo);
        mCheckCount=(TextView)findViewById(R.id.mCheckCount);
        mClassCount=(TextView)findViewById(R.id.mClassCount);
        Intent intent = getIntent();
        mInfo = intent.getStringExtra("info");
        mGrade = intent.getStringExtra("mGrade");
        mMajor = intent.getStringExtra("mMajor");
        mClass= intent.getStringExtra("mClass");
        allCount= intent.getStringExtra("allCount");
        mTime= intent.getStringExtra("mTime");
        getCount=Integer.valueOf(allCount);
        Getdata();
        mBack.setOnClickListener(this);
        mShut.setOnClickListener(this);
        mName.setText(mInfo);

        mStuList.setOnItemClickListener(this);
        flag=new boolean[60];
        newmap = new HashMap();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBack:
                finish();
                break;
            case R.id.mShut: {
                if(j<getCount)
                {
                    UpDialog();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DCheckActivity.this);
                    builder.setMessage("确认提交吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Postdata();
                            DCheckActivity.this.finish();
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
            }
            break;
        }
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0;i<mCount;i++) {
            Map<String, Object> map = new HashMap<String, Object>();;
            map.put("mID", i+1);
            map.put("mStuName", mStuName[i]);
            map.put("mStuID", mStuID[i]);
            list.add(map);
        }
        return  list;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==-1)
            return;
        else  {
            if(flag[i]==false) {
                if(j < getCount){
                    view.setBackgroundColor(Color.parseColor("#BCC2EE"));
                    HashMap<String, String> map1 = (HashMap<String, String>) mStuList.getItemAtPosition(i);
                    j++;
                    mCheckCount.setText("迟到总人数："+j);
                    String mStuID = map1.get("mStuID");
                    newmap.put(i,mStuID);
                    flag[i] = true;
                }else {
                    TestDialog();
                }
            } else if(flag[i] = true){
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    j--;
                    mCheckCount.setText("迟到总人数："+j);
                    newmap.remove(i);
                    flag[i]=false;
            }
        }
    }
    private void TestDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DCheckActivity.this);
        builder.setMessage("迟到人员已达到上限");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void UpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DCheckActivity.this);
        builder.setMessage("迟到人员未添加完毕");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private String httpUrlConnection( String uriAPI,String requestString){
        StringBuffer sb = new StringBuffer();
        try{
            //建立连接
            URL url=new URL(uriAPI);
            HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
            String param =  "UserStr="+ URLEncoder.encode(requestString,"UTF-8");
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
            DataOutputStream outputStream = new DataOutputStream( httpConn.getOutputStream());
            outputStream.writeBytes(param);
            outputStream.close();
            //获得响应状态
            int responseCode = httpConn.getResponseCode();

            if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
                String readLine=new String();
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                return sb.toString();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }
    public void Getdata() {
        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("mGrade",mGrade);
            LL.put("mMajor",mMajor);
            LL.put("mClass",mClass);
            LL.put("mTime",mTime);
            LL.put("allCount",allCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String str = httpUrlConnection(pathAPI+"AndroidStuInfo", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                Message msg = new Message();
                msg.what=1;
                msg.obj=str;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    public void Postdata() {
        StuList = new JSONObject();//java代码封装为json字符串
        String Count=Integer.toString(j);
        try {
            Iterator iter = newmap.entrySet().iterator();
            StuList.put("Count",Count);
            StuList.put("AttendId",DailyCheckActivity.AttendId);
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                StuData=StuData+val+"~";
            }
            StuList.put("mStuID",StuData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String str = httpUrlConnection(pathAPI+"AndroidGetData", StuList.toString());//toString();把JSONArray对象转换为json格式的字符串
            }
        }.start();
    }
    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                JSONObject q=null,p=null;
                String s=null;
                s=(String)msg.obj;
                try {
                    q=new JSONObject(s);
                    mCount=q.getInt("mCount");
                    mStuID=new String[mCount];
                    mStuName=new String[mCount];
                    Split= new String[mCount];
                    Split=s.split(";");
                    for (int i=0;i<mCount;i++)
                    {
                        p=new JSONObject(Split[i]);
                        mStuID[i]=p.getString("mStuID");
                        mStuName[i]=p.getString("mStuName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                New();
                mClassCount.setText("班级总人数："+mCount);
            }
        }
    };
    private void New(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.stu_info,
                new String[]{"mID","mStuName","mStuID","mState"},
                new int[]{R.id.mID,R.id.mStuName,R.id.mStuID,R.id.mState,});
        mStuList.setAdapter(simpleAdapter);
    }
}
class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //此处是代码的关键
        //MeasureSpec.AT_MOST的意思就是wrap_content
        //Integer.MAX_VALUE >> 2 是使用最大值的意思,也就表示的无边界模式
        //Integer.MAX_VALUE >> 2 此处表示是福布局能够给他提供的大小
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

