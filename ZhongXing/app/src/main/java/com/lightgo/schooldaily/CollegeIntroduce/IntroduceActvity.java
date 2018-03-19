package com.lightgo.schooldaily.CollegeIntroduce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import com.lightgo.schooldaily.ElegantActivity;
import com.lightgo.schooldaily.Inweb;
import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.TuanduiActivity;
import com.lightgo.schooldaily.XueyuanActivity;
import com.lightgo.schooldaily.ZTEActivity;
import com.lightgo.schooldaily.ZhuanxingActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntroduceActvity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageButton ib_xyjj = null;
    private ImageButton ib_zxtx = null;
    private ImageButton ib_zxfz = null;
    private ImageButton ib_xxfc = null;
    private ImageButton ib_sztd = null;
    private ListView inlist = null;
    private SimpleAdapter simpleAdapter=null;
    //List<Map<String,Object>> listdata=null;
    Context context = this;
    private Bitmap []Bp;
    private Message  msgs=new Message();
    private ScrollView sc = null;
    private String pathAPI = "http://120.77.40.249:2017/CollegeStyle/";
    String[ ] dateAfterSplit= new String[1];
    String[ ] dateAfterSplit1=null;
    private JSONObject LL=null;
    int b,c=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.introduce_style);

        Get();

        ib_xyjj = (ImageButton)findViewById(R.id.ib_xyjj);
        ib_zxtx = (ImageButton)findViewById(R.id.ib_zxtx);
        ib_zxfz = (ImageButton)findViewById(R.id.ib_zxfz);
        ib_xxfc = (ImageButton)findViewById(R.id.ib_xxfc);
        ib_sztd = (ImageButton)findViewById(R.id.ib_sztd);

        sc = (ScrollView)findViewById(R.id.sv);
        sc.smoothScrollTo(0, 0);

        ib_xyjj.setOnClickListener(this);
        ib_zxtx.setOnClickListener(this);
        ib_zxfz.setOnClickListener(this);
        ib_xxfc.setOnClickListener(this);
        ib_sztd.setOnClickListener(this);

        inlist = (ListView)findViewById(R.id.list5);
        inlist.setOnItemClickListener(this);
        /* listdata=new ArrayList<Map<String, Object>>();
       ListAdapter = new SimpleAdapter(this, getData(), R.layout.activity_elegant,
                new String[]{"mName", "mInfo","mPhoto"}, new int[]{R.id.mingzi,R.id.info,R.id.picture});
        inlist.setAdapter(ListAdapter);*/
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
       for (int i = 1 ,j=0; i <= 20; i += c) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mName", dateAfterSplit1[i]);
            map.put("mInfo", dateAfterSplit1[i+1]);
            map.put("mPhoto", Bp[j++]);
            list.add(map);
        }
        return list;
    }
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
    public void Get() {
        LL = new JSONObject();
        try {
            LL.put("ImageId","h001");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String s = httpUrlConnection(pathAPI+"AndroidLogin", LL.toString());
                Message msg = new Message();
                msg.what=1;
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
            //开启一个线程贴图
            Thread thread = new Thread()
            {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //1: 确定网址
                    String []path =new String[b];
                    int j=0;
                    for (int i=3;i<=b*c;i+=4)
                    {
                        path[j++]=dateAfterSplit1[i];
                    }
                    try {
                        //2:把网址封装为一个URL对象
                        Bp=new Bitmap[b];
                        for (int i=0;i<path.length;i++) {
                            URL url = new URL(path[i]);

                            //3:获取客户端和服务器的连接对象，此时还没有建立连接
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            //4:初始化连接对象
                            conn.setRequestMethod("GET");
                            //设置连接超时
                            conn.setConnectTimeout(5000);
                            //设置读取超时
                            conn.setReadTimeout(5000);
                            //5:发生请求，与服务器建立连接
                            conn.connect();
                            //如果响应码为200，说明请求成功
                            if (conn.getResponseCode() == 200) {
                                //获取服务器响应头中的流
                                InputStream is = conn.getInputStream();
                                //读取流里的数据，构建成bitmap位图
                                Bp[i] = BitmapFactory.decodeStream(is);
                                //发生更新UI的消息
                                Message msg = handler.obtainMessage();
                                msg.what=1;
                            }
                        }
                        handler.sendMessage(msgs);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
            //启动线程任务
            thread.start();
         //   simpleAdapter.notifyDataSetChanged();
        }


    };

    Handler handler = new Handler()
    {
        public void handleMessage(Message msg) {
             simpleAdapter = new SimpleAdapter(context,
                    getData(),
                    R.layout.activity_elegant,
                    new String[]{"mName", "mInfo","mPhoto"},
                     new int[]{R.id.mingzi,R.id.info,R.id.picture});
            inlist.setAdapter(simpleAdapter);

            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String s) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView  && data instanceof Bitmap){
                        ImageView iv = (ImageView)view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_xyjj:
                Intent xyjjIntent = new Intent(IntroduceActvity.this,XueyuanActivity.class);
                startActivity(xyjjIntent);
                break;
            case R.id.ib_zxtx:
                Intent zteIntent = new Intent(IntroduceActvity.this,ZTEActivity.class);
                startActivity(zteIntent);
                break;
            case R.id.ib_zxfz:
                Intent intent = new Intent(IntroduceActvity.this, ZhuanxingActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_xxfc:
                Intent i = new Intent(IntroduceActvity.this, ElegantActivity.class);
                startActivity(i);
                break;
            case R.id.ib_sztd:
                Intent sztdintent = new Intent(IntroduceActvity.this, TuanduiActivity.class);
                startActivity(sztdintent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == -1)
            return;
        else{
            Intent intent = new Intent(IntroduceActvity.this,Inweb.class);
            intent.putExtra("wb",dateAfterSplit1[(position+1)*4]);
            startActivity(intent);
        }
    }
}
