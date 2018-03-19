package com.lightgo.schooldaily.CollegeIntroduce;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lightgo.schooldaily.R;

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

public class MessageActivity extends Activity {

    private ListView myListView = null;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
    private String pathAPI = "http://120.77.40.249:2017//CollegeBulletin/";
    String[] dateAfterSplit= new String[1];
    String[] dateAfterSplit1=null;
    int b,c=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        Get();
        myListView = (ListView)findViewById(R.id.myListView);
        simpleAdapter = new SimpleAdapter(this,getData(),
                R.layout.meesage_ex,new String[]{"title","testview","mname","mTime"},new int[]{R.id.mtitle,
                R.id.tv,R.id.mNAme,R.id.mTime});
        myListView.setAdapter(simpleAdapter);
    }
    private List<Map<String, Object>> getData() {
        for (int i = b * c ; i >=1 ; i -= c) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "    " + dateAfterSplit1[i-3]);
            map.put("testview", "    " + dateAfterSplit1[i-2]+"\n");
            map.put("mname", "温馨提示");
            map.put("mTime",dateAfterSplit1[i]);
            lists.add(map);
        }
        return lists;
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
                String s = httpUrlConnection(pathAPI+"AndroidNotice","15190305");
                Message msg = new Message();
                msg.obj=s;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String date="";
            date= (String) msg.obj;
            dateAfterSplit=date.split("~");
            System.out.print(dateAfterSplit[0]);
            String a = dateAfterSplit[0];
            b=Integer.valueOf(a);
            dateAfterSplit1= new String[b];
            dateAfterSplit1=date.split("~");
            for(int i=1;i<=b*c;i++) {
                System.out.print(dateAfterSplit1[i]);
            }
            getData();
            simpleAdapter.notifyDataSetChanged();
        }

    };
}
