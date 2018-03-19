package com.lightgo.schooldaily.AdminUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class StudentInfoActivity extends Activity implements View.OnClickListener,Handler.Callback,AdapterView.OnItemClickListener{

    private ListView mStudentInfo_ListView = null;  //根据所选条件进行筛选
    private SimpleAdapter simpleAdapter = null;
    private JSONObject ll = null;

    private Button myw,myl,myq,mjk,mdx,myb,meb;
    private Button oldNJ,oldZY,oldBJ;
    private ImageView SelectFinish = null;
    AlertDialog.Builder builder = null;
    private LinearLayout linearLayout = null;
    private RelativeLayout relativeLayout = null;
    private RelativeLayout mRL = null;
    private LinearLayout mLL = null;

    private Button mScreen = null;
    private Button mEsc = null;
    private boolean IsShow = false;

    private Resources resources = null;
    private Drawable btnDrawable = null;
    private String myNJ,myZY,myBJ;
    private Handler mHandler;
    public static Handler myHandler = null;
    private List<Map<String,Object>> lists = null;
    private int mCount = -1;
    private Info[] infos = null;
    private String StudentID = "";
    private HomeInfo homeInfo = null;

    class HomeInfo{
        String HomeAddr;
        String HomeTel;
    }
    class Info{
        String mStudentID;
        String mStudentName;
        String mStudentKPI;
        String mStudentTel;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_info);

        mRL = (RelativeLayout)findViewById(R.id.wTitle);
        mLL = (LinearLayout) findViewById(R.id.nTitle);
        mRL.removeView(mLL);
        if(lists == null){
            lists = new ArrayList<Map<String, Object>>();
        }
        mHandler = new Handler(this);
        myHandler = mHandler;
        resources = this.getResources();

        mStudentInfo_ListView = (ListView)findViewById(R.id.mStudentInfo_ListView) ;
        SelectFinish = (ImageView)findViewById(R.id.SelectFinish);
        myw = (Button)findViewById(R.id.myw);
        myl = (Button)findViewById(R.id.myl);
        myq = (Button)findViewById(R.id.myq);
        mjk = (Button)findViewById(R.id.mjk);
        mdx = (Button)findViewById(R.id.mdx);
        myb = (Button)findViewById(R.id.myb);
        meb = (Button)findViewById(R.id.meb);
        mScreen = (Button)findViewById(R.id.mScreen);
        mEsc = (Button)findViewById(R.id.mEsc);

        SelectFinish.setOnClickListener(this);
        myw.setOnClickListener(this);
        myl.setOnClickListener(this);
        myq.setOnClickListener(this);
        mjk.setOnClickListener(this);
        mdx.setOnClickListener(this);
        myb.setOnClickListener(this);
        meb.setOnClickListener(this);
        mScreen.setOnClickListener(this);
        mEsc.setOnClickListener(this);
        mStudentInfo_ListView.setOnItemClickListener(this);

        oldNJ = myw;
        oldZY = mjk;
        oldBJ = myb;
        linearLayout = (LinearLayout) findViewById(R.id.wSelect);
        relativeLayout = (RelativeLayout) findViewById(R.id.nSelect);
        linearLayout.removeView(relativeLayout);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView text = (TextView)view.findViewById(R.id.mStudenetName);
        TextView t = (TextView)view.findViewById(R.id.mStudentID);
        StudentID = t.getText().toString().trim();
        HttpThread httpThread = new HttpThread("Home/AndroidStudentDetailsInfo",1);
        httpThread.start();
        builder = new AlertDialog.Builder(this);
        builder.setTitle(text.getText().toString().trim());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mEsc:  //返回
                this.finish();
                break;
            case R.id.mScreen:  //筛选
                if(!IsShow){
                    mRL.removeView(mLL);
                    linearLayout.addView(relativeLayout);
                    IsShow = true;
                    if(simpleAdapter != null){
                        lists.clear();
                    }
                }else {
                    linearLayout.removeView(relativeLayout);
                    IsShow = false;
                }
                break;
            case R.id.SelectFinish:
                myNJ = (String) oldNJ.getText();
                myZY = (String) oldZY.getText();
                myBJ = (String) oldBJ.getText();
                HttpThread httpThread = new HttpThread("Home/AndroidClassStudentInfo",0);
                httpThread.start();
                break;
            case R.id.myw:
            case R.id.myl:
            case R.id.myq:
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                oldNJ.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                v.setBackgroundDrawable(btnDrawable);
                oldNJ = (Button) v;
                break;
            case R.id.mjk:
            case R.id.mdx:
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                oldZY.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                v.setBackgroundDrawable(btnDrawable);
                oldZY = (Button) v;
                break;
            case R.id.myb:
            case R.id.meb:
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                oldBJ.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                v.setBackgroundDrawable(btnDrawable);
                oldBJ = (Button) v;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String ClassStudentInfo = (String) msg.obj;
                SliptClassStudentInfo(ClassStudentInfo);
                break;
            case 1:
                String StudentDetailsInfo = (String)msg.obj;
                SlipStudentDetailsInfo(StudentDetailsInfo);
                break;
            case 2:
                HomeInfo info = (HomeInfo) msg.obj;

                View  view=(LinearLayout) getLayoutInflater().inflate(R.layout.activity_builder,null);

                TextView HomeAddr = (TextView)view.findViewById(R.id.HomeAddr);
                HomeAddr.setText(info.HomeAddr);
                TextView HomeTel = (TextView)view.findViewById(R.id.HomeTel);
                HomeTel.setText(info.HomeTel);

                builder.setView(view);
                builder.show();
                break;
            default:
                break;
        }
        return false;
    }

    private void SlipStudentDetailsInfo(String str){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            homeInfo = new HomeInfo();
            homeInfo.HomeAddr= jsonObject.getString("HomeAddress");
            homeInfo.HomeTel = jsonObject.getString("HomeTel");
            Message msg = new Message();
            msg.obj = homeInfo;
            msg.what = 2;
            myHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SliptClassStudentInfo(String str){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
            mCount = jsonObject.getInt("mCount");
            infos = new Info[mCount];
            String[] mData = str.split(";");
            for (int i = 0; i < mCount; i++){
                jsonObject = new JSONObject(mData[i]);
                infos[i] = new Info();
                infos[i].mStudentID= jsonObject.getString("StudentId");
                infos[i].mStudentName = jsonObject.getString("StudentName");
                infos[i].mStudentKPI = jsonObject.getString("Kpi");
                infos[i].mStudentTel= jsonObject.getString("StudentTel");
            }
            if(simpleAdapter == null){
                simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_student_infoex,
                        new String[]{"mStudentID","mStudentName","mStudentKPI","mStudentTel"},new int[]{R.id.mStudentID,R.id.mStudenetName,R.id.mStudentKPI,R.id.mStudentTel});
                mStudentInfo_ListView.setAdapter(simpleAdapter);
            }else {
                GetData();
                simpleAdapter.notifyDataSetChanged();
            }
            if(IsShow){
                linearLayout.removeView(relativeLayout);
                mRL.addView(mLL);
                IsShow = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Map<String,Object>> GetData(){
        lists.clear();
        for (int i = 0; i < mCount; i++){
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("mStudentID",infos[i].mStudentID);
            map.put("mStudentName",infos[i].mStudentName);
            map.put("mStudentKPI",infos[i].mStudentKPI);
            map.put("mStudentTel",infos[i].mStudentTel);
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
            ll = new JSONObject();
            try {
                if(mAction  == "Home/AndroidStudentDetailsInfo"){
                    ll.put("StudentID",StudentID);
                }else {
                    ll.put("mGrade",oldNJ.getText());
                    ll.put("mMajor",oldZY.getText());
                    ll.put("mClass",oldBJ.getText());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String s = httpUrlConnection(pathAPI + mAction, ll.toString());//toString();把JSONArray对象转换为json格式的字符串
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
