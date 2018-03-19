package com.lightgo.schooldaily.AdminUser;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Window;
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
import java.util.List;
import java.util.Map;

public class ToBeProcessActivity extends Activity implements Handler.Callback{

    private ListView mToBeProcessListView = null;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> lists = null;

    public static Handler myHandler = null;
    public Handler mHandler = null;
    private HttpThread httpThread = null;
    private TextView ActivityTitle = null;
    private TextView ActivityPlace = null;
    private TextView ActivityTime = null;
    private TextView IsAudit = null;
    private TextView IsEnd = null;
    private Info[] infos = null;
    private int mCount = -1;
    class Info{
        String mTitle;
        String mPlace;
        String mTime;
        String mIsAudit;
        String mIsEnd;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_to_be_process);

        if(lists == null){
            lists = new ArrayList<Map<String, Object>>();
        }

        mHandler = new Handler(this);
        myHandler = mHandler;

        httpThread = new HttpThread("Home/AndroidIsPass",0);
        httpThread.start();

        mToBeProcessListView = (ListView)findViewById(R.id.mToBeProcessListView);

        ActivityTitle = (TextView)findViewById(R.id.ActivityTitle);
        ActivityPlace = (TextView)findViewById(R.id.ActivityPlace);
        ActivityTime = (TextView)findViewById(R.id.ActivityTime);
        IsAudit = (TextView)findViewById(R.id.IsAudit);
        IsEnd = (TextView)findViewById(R.id.IsEnd);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String str = (String) msg.obj;
                SliptToBeProcess(str);
                break;
            default:
                break;
        }
        return false;
    }
    private void SliptToBeProcess(String str){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            mCount = jsonObject.getInt("mCount");
            if(infos == null){
                infos = new Info[mCount];
            }

            String[] mData = str.split(";");
            for (int i = 0; i < mCount; i++){
                jsonObject = new JSONObject(mData[i]);
                infos[i] = new Info();
                infos[i].mTitle= jsonObject.getString("ActivityTitle");
                infos[i].mPlace = jsonObject.getString("ActivityPlace");
                infos[i].mTime = jsonObject.getString("ActivityTime");
                infos[i].mIsAudit= jsonObject.getString("IsAudit");
                infos[i].mIsEnd= jsonObject.getString("IsEnd");
            }
            if(simpleAdapter == null){
                simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_to_be_processex,
                        new String[]{"mTitle","mPlace","mTime","mIsAudit","mIsEnd"},new int[]{R.id.ActivityTitle,R.id.ActivityPlace,R.id.ActivityTime,R.id.IsAudit,R.id.IsEnd});
                mToBeProcessListView.setAdapter(simpleAdapter);
            }else {
                simpleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<Map<String,Object>> GetData(){
        lists.clear();
        for (int i = 0; i < mCount; i++){
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("mTitle",infos[i].mTitle);
            map.put("mPlace",infos[i].mPlace);
            map.put("mTime",infos[i].mTime);
            map.put("mIsAudit",infos[i].mIsAudit);
            map.put("mIsEnd",infos[i].mIsEnd);
            lists.add(map);
        }
        return lists;
    }
    class HttpThread extends Thread implements Runnable {
        private String pathAPI = "http://120.77.40.249:10068/";
        private String mAction;
        private int mWhat;
        HttpThread(String Action, int what){
            mAction = Action;
            mWhat = what;
        }

        @Override
        public void run() {
            try {
                String s = httpUrlConnection(pathAPI + mAction, "");//toString();把JSONArray对象转换为json格式的字符串
                Message msg = new Message();
                msg.what = mWhat;
                msg.obj = s;
                myHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
}
