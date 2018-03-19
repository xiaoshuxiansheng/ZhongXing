package com.lightgo.schooldaily.CheckActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.List;
import java.util.Map;

public class DailyCheckActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private Button mSelect;
    private ListView mCheckList;
    private String mInfo=null;
    private String mTime=null;
    private String mGrade=null;
    private String mMajor=null;
    private String mClass=null;
    private String allCount=null;
    private String text=null;

    private JSONObject LL=null;
    public static String AttendId="";
    private String pathAPI = "http://47.93.35.144:8888/Home/";

    private int mCount=0;
    private List<String> data=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_check);
        new TimeThread().start();
        mSelect=(Button)findViewById(R.id.mSelect);
        mCheckList=(ListView) findViewById(R.id.mCheckList);
        mSelect.setOnClickListener(this);
        mCheckList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(DailyCheckActivity.this, DSelectActivity.class);
        startActivityForResult(intent, 0);
    }
    private List<Map<String, Object>> getData() {
        if(mInfo!=null) {
            data.add(mInfo);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0;i<data.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mInfo", data.get(i));
            map.put("mTime", mTime);
            list.add(map);
        }
        return  list;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        mInfo= bundle.getString("param");
        mGrade= bundle.getString("mGrade");
        mMajor= bundle.getString("mMajor");
        mClass= bundle.getString("mClass");
        allCount= bundle.getString("allCount");
        text=bundle.getString("test");
        NewSimpleAdapter();
    }
    private void NewSimpleAdapter(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.check_list,
                new String[]{"mInfo","mTime"},
                new int[]{R.id.mMessage,R.id.mTime});
        mCheckList.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==-1)
            return;
        else {
            Intent intent = new Intent(DailyCheckActivity.this, DCheckActivity.class);
            UpAttend();
            intent.putExtra("info", mInfo);
            intent.putExtra("mGrade", mGrade);
            intent.putExtra("mMajor", mMajor);
            intent.putExtra("mClass", mClass);
            intent.putExtra("allCount", allCount);
            intent.putExtra("mTime", mTime);
            startActivity(intent);
        }
    }
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    mTime = sDateFormat.format(new java.util.Date());
                    break;
                default:
                    break;
            }
        }
    };
    public void UpAttend() {
        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("mGrade",mGrade);
            LL.put("mMajor",mMajor);
            LL.put("mClass",mClass);
            LL.put("allCount",allCount);
            LL.put("mTime",mTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                AttendId = httpUrlConnection(pathAPI+"AndroidAttendInfo", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
            }
        }.start();
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
                    sb.append(readLine).append("");
                }
                responseReader.close();
                return sb.toString();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
