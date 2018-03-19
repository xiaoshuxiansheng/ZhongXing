package com.lightgo.schooldaily.UserMessage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightgo.schooldaily.MainActivity;
import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.StudentJar.BaseInformation;
import com.lightgo.schooldaily.StudentJar.ChufaInformation;
import com.lightgo.schooldaily.StudentJar.Gexing;
import com.lightgo.schooldaily.StudentJar.HuodongInformation;
import com.lightgo.schooldaily.StudentJar.JiangliInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/8/14.
 */

public class StudentActivity extends Activity implements View.OnClickListener{
    private String pathAPI = "http://47.93.35.144:6666//Reward/";
    private LinearLayout lstudent;
    private LinearLayout lhuodong;
    private LinearLayout ljiangli;
    private LinearLayout lchufa;
    private LinearLayout lgexing;
    private LinearLayout lsao;
    private TextView lext;
    private SharedPreferences sharedPrefrences;
    private SharedPreferences.Editor editor;
    private TextView jiangliText = null;
    private TextView stkpi = null;
    private TextView strank = null;
    private String studentRank;
    private String studentKpi;
    private TextView myName = null;
    private Button StudentEsc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_user);

        myName = (TextView)findViewById(R.id.myName) ;
        StudentEsc = (Button)findViewById(R.id.StudentEsc);

        Get();
        Inint();
        Inintclick();

        SharedPreferences msharedPreferences = getSharedPreferences("my",
                Activity.MODE_PRIVATE);
        String hedit = msharedPreferences.getString("hedit", "");
        lext.setText(hedit);
        stkpi = (TextView)findViewById(R.id.StKPI);
        strank = (TextView)findViewById(R.id.StRunk);
        myName.setText(LoginActivity.UserName);
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
                String s = httpUrlConnection(pathAPI+"AndroidStudentinfo",LoginActivity.UserName);
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
                studentKpi= SS.getString("Kpi");
                studentRank= SS.getString("Rank");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            stkpi.setText(studentKpi);
            strank.setText(studentRank);
        }

    };
    private void Inint()
    {
        lstudent = (LinearLayout)findViewById(R.id.studentInfo);
        lgexing = (LinearLayout)findViewById(R.id.gexing);
        lsao = (LinearLayout)findViewById(R.id.saoyisao);
        lhuodong = (LinearLayout)findViewById(R.id.huodongInfo);
        ljiangli = (LinearLayout)findViewById(R.id.jiangliInfo);
        lchufa = (LinearLayout)findViewById(R.id.chuInfo);
        lext = (TextView)findViewById(R.id.text);
        jiangliText = (TextView)findViewById(R.id.jiangtext);
    }
    private void Inintclick()
    {
        lstudent.setOnClickListener(this);
        lgexing.setOnClickListener(this);
        lsao.setOnClickListener(this);
        lhuodong.setOnClickListener(this);
        ljiangli.setOnClickListener(this);
        lchufa.setOnClickListener(this);
        StudentEsc.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.StudentEsc:
                MainActivity.Is_Login = false;
                MainActivity.Is_flag = false;
                finish();
                break;
            case R.id.studentInfo: {
                Intent intent = new Intent(StudentActivity.this, BaseInformation.class);
                startActivity(intent);
            }
            break;
            case R.id.gexing: {
                Intent intent = new Intent(StudentActivity.this,Gexing.class);
                startActivityForResult(intent,0);
                break;
            }
            case R.id.saoyisao: {
                break;
            }
            case R.id.huodongInfo: {
                Intent intent = new Intent(StudentActivity.this, HuodongInformation.class);
                startActivity(intent);
                break;
            }
            case R.id.jiangliInfo: {
                Intent intent = new Intent(StudentActivity.this, JiangliInformation.class);
                startActivity(intent);
                break;
            }
            case R.id.chuInfo: {
                Intent intent = new Intent(StudentActivity.this, ChufaInformation.class);
                startActivity(intent);
                break;
            }

            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String text =null;
            if(bundle!=null)
                text=bundle.getString("name");
            Log.d("text",text);
            lext.setText(text);
            SharedPreferences mSharedPreferences = getSharedPreferences("my",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor meditor = mSharedPreferences.edit();
            String yaya=lext.getText().toString();
            meditor.putString("hedit", yaya);
            meditor.commit();
        }
    }
}
