package com.lightgo.schooldaily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class labvedioone extends AppCompatActivity {
    private VideoView mVideoView;
    private Button mButton;
    private boolean fullscreen=true;
    private String pathAPI = "http://120.77.40.249:2017/Video/";
   // private String pathAPI = "http://120.24.92.214:8888/Video/";
    private JSONObject LL=null;
    private static String VideoUrl;
    private String VideoId;

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_labvedioone);
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mVideoView.setMediaController(new MediaController(labvedioone.this));
        Intent intentved=getIntent();
        String VideoId = intentved.getStringExtra("fn");
        Get(VideoId);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mVideoView.setLayoutParams(layoutParams);
        labvedioone.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fullscreen = true;
    }
    private void initVideoPath() {
        mVideoView.setMediaController(new MediaController(this));
        File file=new File(Environment.getRootDirectory().getPath()+"/at_01.mp4");
        mVideoView.setVideoPath(file.getAbsolutePath());
        mVideoView.start();
    }

    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                JSONObject q=null;
                try {
                    q = new JSONObject((String)msg.obj);//masg.obj=s;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                String s="";
                try {
                    s+=q.getString("VideoUrl")+" ";//获取服务器上的UserI
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mVideoView.setVideoURI(Uri.parse(s));
                mVideoView.start();
            }
        }
    };
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
    private void Get (String vd) {
        LL = new JSONObject();
        try {
            LL.put("VideoId",vd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread() {
            @Override
            public void run() {
                String s = httpUrlConnection(pathAPI + "AndroidLogin", LL.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = s;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
}
