package com.lightgo.schooldaily.StudentJar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.UserMessage.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BaseInformation extends Activity implements View.OnClickListener {

    private String pathAPI = "http://47.93.35.144:6666//Reward/";
    private JSONObject LL=null;

    String[ ] dateAfterSplit= new String[1];
    String[ ] dateAfterSplit1=null;
    int b,c=4;
    private Message msgs=new Message();
    private LinearLayout linearLayout = null;

    private String studentName;

    private String studentMajor;
    private String studentphone;
    private String studentRank;
    private String studentKpi;

    private TextView stuname = null;
    private TextView Stunum = null;
    private TextView stumajor = null;
    private TextView stuphone = null;

    private TextView stkpi = null;
    private TextView strank = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_information);

        linearLayout = (LinearLayout)findViewById(R.id.retur);
        linearLayout.setOnClickListener(this);

        Get();
        stuname = (TextView)findViewById(R.id.sname);
        Stunum = (TextView)findViewById(R.id.sno);
        stumajor = (TextView)findViewById(R.id.smajor);
        stuphone = (TextView)findViewById(R.id.sphone);
        // stkpi = (TextView)findViewById(R.id.StKPI);
        // strank = (TextView)findViewById(R.id.StRunk);

    }
    private String httpUrlConnection( String uriAPI,String requestString){
        StringBuffer sb = new StringBuffer();
        try{//建立连接
            URL url=new URL(uriAPI);
            HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
            String param = "str="+ URLEncoder.encode(requestString,"UTF-8");//设置请求属性
            //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            httpConn.setDoOutput(true); //需要输出
            httpConn.setDoInput(true); //需要输入
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.connect();
            DataOutputStream outputStream = new DataOutputStream( httpConn.getOutputStream());//建立输出流，并写入数据
            outputStream.writeBytes(param);
            outputStream.close();
            int responseCode = httpConn.getResponseCode(); //获得响应状态
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
    public void Get() {
        new Thread()
        {
            @Override
            public void run()
            {
                String s = httpUrlConnection(pathAPI+"AndroidStudentinfo", LoginActivity.UserName);
                Message msg = new Message();
                msg.obj=s;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {

            JSONObject SS=null;
            String jl = null;
            jl = (String)msg.obj;
            try{
                SS = new JSONObject(jl);
                studentName = SS.getString("StudentName");
                studentMajor = SS.getString("MajorName");
                studentphone = SS.getString("StudentTel");
                studentKpi= SS.getString("Kpi");
                studentRank= SS.getString("Rank");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            stuname.setText(studentName);
            stuphone.setText(studentphone);
            stumajor.setText(studentMajor);
            Stunum.setText(LoginActivity.UserName);
            //stkpi.setText(studentKpi);
            //strank.setText(studentRank);
        }

    };
    @Override
    public void onClick(View view) {
        finish();
    }
}
