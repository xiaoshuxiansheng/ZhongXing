package com.lightgo.schooldaily.UserMessage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.lightgo.schooldaily.MainActivity;
import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.StudyCommunity.BrowsList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/8/8.
 */

public class LoginActivity extends Activity implements View.OnClickListener,Handler.Callback{

    private ImageView mLogin = null;
    private EditText mUserName = null;
    private EditText mPassWord = null;
    private Handler mHandler = null;
    private UserInfo userInfo = null;
    private JSONObject Json = null;
    public static String UserName = null;
    private String pathAPI = "http://120.77.40.249:8888/Home/";
    public class UserInfo{
        String UserName;
        String PassWord;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLogin = (ImageView)findViewById(R.id.mLogin);
        mUserName = (EditText)findViewById(R.id.mUserName);
        mPassWord = (EditText)findViewById(R.id.mPassWord);
        mPassWord.setInputType(0x81);
        mLogin.setOnClickListener(this);
        mHandler = new Handler(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mLogin:
                    userInfo = new UserInfo();
                    userInfo.UserName = mUserName.getText().toString();
                    userInfo.PassWord = mPassWord.getText().toString();
                    Message message = new Message();
                    message.what = 0;
                    message.obj = userInfo;
                    mHandler.sendMessage(message);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 0:
                UserInfo muserInfo = (UserInfo) message.obj;
                UserName = muserInfo.UserName;
                String mPassWord = muserInfo.PassWord;
                GetInfo(UserName,mPassWord);
                break;
            case 1:
                String  UserType = (String) message.obj;
                SharedPreferences mySharedPreferences = getSharedPreferences("test",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("UserName", userInfo.UserName);
                editor.putString("PassWord", userInfo.PassWord);
                editor.putString("UserType", UserType);
                editor.commit();
                MainActivity.Is_Login = true;
                Intent intent = new Intent(LoginActivity.this, BrowsList.class);
                startActivity(intent);
                finish();
                //Receive(UserType);
            default:
                break;
        }
        return false;
    }
    private void Receive(String UserType){
        switch (UserType){
            case "1\n":  //到管理员的个人中心
                MainActivity.Is_flag = true;
                Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                startActivity(intent);
                finish();
                break;
            case "2\n":
                MainActivity.Is_flag = false;
                Intent sintent = new Intent(LoginActivity.this,StudentActivity.class);
                startActivity(sintent);
                finish();
                break;
            default:
                break;
        }
    }
    private void GetInfo(String mUserName,String mPassWord){
        Json = new JSONObject();
        try {
            Json.put("mUserName",mUserName);
            Json.put("mPassWord",mPassWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String s = httpUrlConnection(pathAPI+"AndroidLogin", Json.toString());
                Message msg = new Message();
                msg.what=1;
                msg.obj=s;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    private String httpUrlConnection( String uriAPI,String requestString){
        StringBuffer sb = new StringBuffer();
        try{
            //建立连接
            URL url=new URL(uriAPI);
            HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
            String param =  "UserInfo="+ URLEncoder.encode(requestString,"UTF-8");
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
}