package com.lightgo.schooldaily.HostelCheck;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HCheckActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener,Handler.Callback{
    private String a,b,c,d =null;
    private String pathAPI = "http://120.77.40.249:10068/Home/";
    private TextView mName;
    private LinearLayout mBack;
    private Button mShut=null;
    private JSONObject ll=null;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String, Object>> list =null;
    private Map<String, Object> map =null;
    private TextView mClassAllCount = null;
    private TextView mPeopleCount = null;
    private Handler myHandler = null;
    private Handler mHandler = null;
    public MyListView mStuList = null;
    private int mCount = -1;
    private String[] Split = null;
    private int NotAttend = 0;
    private HostelInfo[] info = null;
    private JSONObject[] j ;
    private HashMap hashMap = null;
    private PostInfo postInfo = null;

    class HostelInfo{
        String mStuID;
        String mStuName;
        String mHostelName;
        Boolean Isflag;
    }
    class PostInfo{
        String mStuName;
        String mHostelName;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hcheck);
        hashMap = new HashMap();

        mClassAllCount = (TextView)findViewById(R.id.mClassAllCount);
        mPeopleCount = (TextView)findViewById(R.id.mPeopleCount);

        mHandler = new Handler(this);
        myHandler = mHandler;

        Intent intent = getIntent();
        a = intent.getStringExtra("a"); //专业
        b = intent.getStringExtra("b");  //时间
        c = intent.getStringExtra("c");   //年级
        d = intent.getStringExtra("d");    //班级
        list = new ArrayList<Map<String, Object>>();
        map = new HashMap<String, Object>();
        mName=(TextView)findViewById(R.id.mName);
        mBack=(LinearLayout) findViewById(R.id.mBack);
        mShut=(Button) findViewById(R.id.mShut);
        mStuList=(MyListView) findViewById(R.id.mStuInfoListview);
        mBack.setOnClickListener(this);
        mShut.setOnClickListener(this);
        mName.setText(a+b+c+d);
        mStuList.setOnItemClickListener(this);
        load();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBack:
                finish();
                break;
            case R.id.mShut: {
                AlertDialog.Builder builder = new AlertDialog.Builder(HCheckActivity.this);
                builder.setMessage("确认提交吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HCheckActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                Posthosteldata();
            }
            break;
            default:
                break;
        }
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==-1)return;
        else  {
            if(info[i].Isflag==false) {
                view.setBackgroundColor(Color.parseColor("#BCC2EE"));
                NotAttend++;
                mPeopleCount.setText("晚归总人数："+NotAttend);
                postInfo = new PostInfo();
                postInfo.mStuName = info[i].mStuName;
                postInfo.mHostelName = info[i].mHostelName;
                hashMap.put(i,postInfo);
                info[i].Isflag = true;
            } else {
                view.setBackgroundColor(Color.parseColor("#00000000"));
                NotAttend--;
                mPeopleCount.setText("晚归总人数："+NotAttend);
                info[i].Isflag = false;
                hashMap.remove(i);
            }
        }
    }

    public void Posthosteldata() {
        ll = new JSONObject();//java代码封装为json字符串
        String str="";
        try {
            Iterator iter = hashMap.entrySet().iterator();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String mTime = sDateFormat.format(new java.util.Date());
            ll.put("mTime",mTime);
            ll.put("Count",NotAttend + "");
            j = new JSONObject[NotAttend];
            int i = 0;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                PostInfo val = (PostInfo) entry.getValue();
                str=str+val.mStuName+"~" + val.mHostelName + "~";
            }
            ll.put("mStr",str);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String str = httpUrlConnection(pathAPI+"AndroidGetHostelData", ll.toString());//toString();把JSONArray对象转换为json格式的字符串
            }
        }.start();
    }

    private List<Map<String, Object>> getData() {
        for (int i= 0;i < mCount;i++) {
            map = new HashMap<String, Object>();
            map.put("StudentId",info[i].mStuID);
            map.put("StudentName", info[i].mStuName);
            map.put("DormitoryNumber",info[i].mHostelName);
            map.put("mState", "");
            list.add(map);
        }
        return list;
    }

    private void load() {
        ll = new JSONObject();
        try {
            ll.put("mGrade",c);
            ll.put("mMajor",a);
            ll.put("mClass",d);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    String s = httpUrlConnection(pathAPI + "AndroidStuInfo", ll.toString());//toString();把JSONArray对象转换为json格式的字符串
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = s;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        //启动线程任务
        thread1.start();    //开启一个线程
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
            //   httpConn.setRequestProperty("Content-Length",param.length()+"");
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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String str = (String) msg.obj;
                GetHostelData(str);
                break;
            default:
                break;
        }
        return false;
    }

    void GetHostelData(String str){
        JSONObject q=null,p=null;
        try {
            q=new JSONObject(str);

            mCount=q.getInt("mCount");
            info = new HostelInfo[mCount];
            Split= new String[mCount];

            Split=str.split(";");
            for (int i=0;i<mCount;i++)
            {
                p=new JSONObject(Split[i]);
                info[i] = new HostelInfo();
                info[i].mStuID=p.getString("mStuID");
                info[i].mStuName=p.getString("mStuName");
                info[i].mHostelName=p.getString("mHostel");
                info[i].Isflag = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(simpleAdapter == null){
            simpleAdapter = new SimpleAdapter(this, getData(), R.layout.stu_info,
                    new String[]{"DormitoryNumber","StudentName","StudentId","mState"},
                    new int[]{R.id.mID,R.id.mStuName,R.id.mStuID,R.id.mState,});
            mStuList.setAdapter(simpleAdapter);
        }else {
            getData();
            simpleAdapter.notifyDataSetChanged();
        }
        mClassAllCount.setText("班级总人数："+mCount);
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
