package com.lightgo.schooldaily.ActActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActShowActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private LinearLayout mSelect = null;
    private ListView mCheckList = null;
    private String pathAPI = "http://47.93.35.144:8888//Home/";
    private JSONObject LL = null;
    private int aCount=0;
    private String[] ActivityId=null;
    private String[] ActivityTitle=null;
    private String[] ActivityTime=null;
    private String[] Initiator=null;
    private String[] ActivityInfo=null;
    private String[] Split1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_act_show);

        loadInfo();
        mSelect = (LinearLayout) findViewById(R.id.mSelect);
        mCheckList = (ListView) findViewById(R.id.mCheckList);
        mSelect.setOnClickListener(this);
        mCheckList.setOnItemClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mSelect:
                Intent intent = new Intent(ActShowActivity.this, AddActivity.class);
                startActivity(intent);
                 break;
        }
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.clear();
        for (int i = 0; i < aCount; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", ActivityTitle[i]);
            map.put("daybegin", "开始时间:" + ActivityTime[i] + "...");
            map.put("name", Initiator[i]);
            list.add(map);
        }
        return list;
    }
    private void NewSimpleAdapter()
    {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.list_check,
                new String[]{"title","daybegin","name"},
                new int[]{R.id.title, R.id.time, R.id.name});
        mCheckList.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==-1)
            return;
        else {
            Intent intent = new Intent(ActShowActivity.this, SignInActivity.class);
            intent.putExtra("ActivityId", ActivityId[i]);
            intent.putExtra("ActivityInfo", ActivityInfo[i]);
            intent.putExtra("ActivityTitle", ActivityTitle[i]);
            startActivity(intent);
        }
    }
    private void loadInfo() {
        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("ImageId", "zx01");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    String s = httpUrlConnection(pathAPI + "AndroidGetActInfo", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = s;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                JSONObject q = null, p = null;
                String s = null;
                s = (String) msg.obj;
                try {
                    q = new JSONObject(s);
                    aCount = q.getInt("mCount");
                    ActivityId = new String[aCount];
                    ActivityTitle = new String[aCount];
                    ActivityTime = new String[aCount];
                    ActivityInfo = new String[aCount];
                    Initiator = new String[aCount];
                    Split1 = new String[aCount];
                    Split1 = s.split(";");
                    for (int k = 0; k < aCount; k++) {
                        p = new JSONObject(Split1[k]);
                        ActivityId[k] = p.getString("ActivityId");
                        ActivityTitle[k] = p.getString("ActivityTitle");
                        ActivityTime[k] = p.getString("ActivityTime");
                        ActivityInfo[k] = p.getString("ActivityInfo");
                        Initiator[k] = p.getString("Initiator");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NewSimpleAdapter();
            }
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
