package com.lightgo.schooldaily.AdminUser;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

public class TodayCheckActivity extends Activity implements View.OnClickListener,Handler.Callback {

    private Handler mHandler;
    public static Handler myHandler = null;

    private ListView mToDayCheck_ListView = null;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> lists = null;
    private Map<String,Object> map = null;
    private Button bt_zzx,bt_wzx,bt_wg;
    private Resources resources = null;
    private Drawable btnDrawable = null;
    private Button oldBt = null;
    private Button CurrentBt = null;
    private int[] mTextColor = {Color.argb(255,32,178,170),Color.argb(255,0,0,0)};
    private int bgColor = Color.argb(0,0,0,255);
    private Info[] infoLists = null;
    private int mCount = -1;
    class Info{
        String mGrade;
        String mMajor;
        String mClassName;
        String AttendanceTime;
        String StudentName;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_today_check);
        mHandler = new Handler(this);
        myHandler = mHandler;
        resources = this.getResources();

        if(lists == null){
            lists = new ArrayList<Map<String, Object>>();
        }

        mToDayCheck_ListView = (ListView)findViewById(R.id.mToDayCheck_ListView);
       /* simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_today_checkex,
                new String[]{"jj","zy","bj","xm"},new int[]{R.id.mjj,R.id.mzy,R.id.mbj,R.id.mxm});
        mToDayCheck_ListView.setAdapter(simpleAdapter);*/

        bt_zzx = (Button)findViewById(R.id.bt_zzx);
        bt_wzx = (Button)findViewById(R.id.bt_wzx);
        bt_wg = (Button)findViewById(R.id.bt_wg);

        bt_zzx.setOnClickListener(this);
        bt_wzx.setOnClickListener(this);
        bt_wg.setOnClickListener(this);

        oldBt = bt_zzx;
    }
    private void ChangeTextcolor(View view){
        CurrentBt = (Button)view;
        oldBt.setTextColor(mTextColor[1]);
        CurrentBt.setTextColor(mTextColor[0]);
        oldBt = CurrentBt;
        view.setBackgroundDrawable(btnDrawable);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_zzx:
                btnDrawable = resources.getDrawable(R.drawable.buttonstyle);
                bt_wzx.setBackgroundColor(bgColor);
                bt_wg.setBackgroundColor(bgColor);
                v.setBackgroundDrawable(btnDrawable);
                ChangeTextcolor(bt_zzx);
                HttpThread MhttpThread = new HttpThread("Home/AndroidMorCheckInfo",0);
                MhttpThread.start();
                break;
            case R.id.bt_wzx:
                btnDrawable = resources.getDrawable(R.drawable.buttonstyle);
                bt_zzx.setBackgroundColor(bgColor);
                bt_wg.setBackgroundColor(bgColor);
                v.setBackgroundDrawable(btnDrawable);
                ChangeTextcolor(bt_wzx);
                HttpThread NhttpThread = new HttpThread("Home/AndroidNigCheckInfo",1);
                NhttpThread.start();
                break;
            case R.id.bt_wg:
                btnDrawable = resources.getDrawable(R.drawable.buttonstyle);
                bt_wzx.setBackgroundColor(bgColor);
                bt_zzx.setBackgroundColor(bgColor);
                v.setBackgroundDrawable(btnDrawable);
                ChangeTextcolor(bt_wg);
                HttpThread DhttpThread = new HttpThread("Home/AndroidHostelCheckInfo",2);
                DhttpThread.start();
                break;
            default:
                break;
        }
    }

    private List<Map<String,Object>> GetData(){
        for(int i = 0; i < mCount; i++){
            map = new HashMap<String, Object>();
            map.put("mGrade",infoLists[i].mGrade);
            map.put("mMajor",infoLists[i].mMajor);
            map.put("mClassName",infoLists[i].mClassName);
            map.put("StudentName",infoLists[i].StudentName);
            map.put("AttendanceTime",infoLists[i].AttendanceTime);
            lists.add(map);
        }
        return lists;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String Info = (String) msg.obj;
                SliptTodayCheck(Info);
                break;
            case 2:
                String SInfo = (String)msg.obj;
                SliptDormitoryInfo(SInfo);
                break;
            default:
                break;
        }
        return false;
    }
    private void SliptDormitoryInfo(String str){
        JSONObject jsonObject = null;
        try {
            String[] mData = str.split(";");
            mCount = Integer.parseInt(mData[0]);
            infoLists = new Info[mCount];
            for (int i = 0; i < mCount; i++){
                jsonObject = new JSONObject(mData[i + 1]);
                infoLists[i] = new Info();
                infoLists[i].mGrade= jsonObject.getString("GradeName");
                infoLists[i].mMajor = jsonObject.getString("MajorName");
                infoLists[i].mClassName= jsonObject.getString("ClassName");
                infoLists[i].StudentName= jsonObject.getString("StudentName");
                infoLists[i].AttendanceTime= jsonObject.getString("AttendanceTime");
            }
            if(simpleAdapter == null){
                simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_today_checkex,
                        new String[]{"mGrade","mMajor","mClassName","StudentName","AttendanceTime"},new int[]{R.id.mjjj,R.id.mzyy,R.id.mbjj,R.id.mxmm,R.id.msj});
                mToDayCheck_ListView.setAdapter(simpleAdapter);
            }else {
                lists.clear();
                GetData();
                simpleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void SliptTodayCheck(String str){
        JSONObject jsonObject = null;
        try {
            String[] mData = str.split(";");
            mCount = Integer.parseInt(mData[0]);
            infoLists = new Info[mCount];
            for (int i = 0; i < mCount; i++){
                jsonObject = new JSONObject(mData[i + 1]);
                infoLists[i] = new Info();
                infoLists[i].mGrade= jsonObject.getString("GradeName");
                infoLists[i].mMajor = jsonObject.getString("MajorName");
                infoLists[i].mClassName= jsonObject.getString("ClassName");
                infoLists[i].StudentName= jsonObject.getString("StudentName");
                infoLists[i].AttendanceTime= jsonObject.getString("AttendanceTime");
            }
            if(simpleAdapter == null){
                simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_today_checkex,
                        new String[]{"mGrade","mMajor","mClassName","StudentName","AttendanceTime"},new int[]{R.id.mjjj,R.id.mzyy,R.id.mbjj,R.id.mxmm,R.id.msj});
                mToDayCheck_ListView.setAdapter(simpleAdapter);
            }else {
                lists.clear();
                GetData();
                simpleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class HttpThread extends Thread implements Runnable {
        private String pathAPI = "http://120.77.40.249:10068/";
        private String mAction;
        private int mWhat;
        HttpThread(String Action, int what){
            mAction = Action;
            mWhat = what;
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mTime = sDateFormat.format(new java.util.Date());
        @Override
        public void run() {
            try {
                String s = httpUrlConnection(pathAPI + mAction, mTime);//toString();把JSONArray对象转换为json格式的字符串
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
                String param = "Str=" + URLEncoder.encode(requestString, "UTF-8");
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
