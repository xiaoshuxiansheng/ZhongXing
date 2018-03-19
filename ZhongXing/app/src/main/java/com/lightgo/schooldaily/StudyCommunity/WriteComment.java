package com.lightgo.schooldaily.StudyCommunity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.StudyCommunity.Sq_util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

public class WriteComment extends AppCompatActivity {

    private  final int MIN_CLICK_DELAY_TIME = 1000;
    private  long lastClickTime;
    private RichTextEditor et_new_content;
    private JSONObject LL=null;

    private String CommentTime;
    private String pathAPI = "http://120.77.40.249:2017/BBS/CommentUpload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sq_write_comment);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("评论");

        et_new_content=(RichTextEditor)findViewById(R.id.edt_content);
        CommentTime= DateUtils.date2string(new Date());
    }


    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        int i = 0;
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
//                content.append(itemData.inputStr).append("$^");
                content.append(itemData.inputStr);
            } else if (itemData.imageurl != null) {
                //    content.append("<img src=\""+itemData.imageurl+"\"/>");
                content.append(itemData.imageurl).append("$&");
            }
        }
        return content.toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_send:
                long curClickTime = System.currentTimeMillis();
                if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    Upload();}
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void Upload() {
        LL = new JSONObject();
        try {
            LL.put("TextTime", CommentTime);
            LL.put("CommentUser", "2");
            LL.put("CommentInfo", getEditData());
            LL.put("TextID", "43");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                String s = httpUrlConnection(pathAPI, LL.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = s;
            }

        };
        thread.start();
    }
    private String httpUrlConnection(String uriAPI, String requestString){
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
}
