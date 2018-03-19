package com.lightgo.schooldaily;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShiyanshiActivity extends AppCompatActivity {
    private TextView textView = null;
    private TextView textView2 = null;
    private ImageView vstar = null;
    private List<ImageView> image = null;
    private Bitmap bitmap;
    private Bitmap[] bm = new Bitmap[20];
    private ImageView imags;
    String[] dateAfterSplit = new String[1];
    String[] dateAfterSplit1 = null;
    private String pathAPI = "http://120.77.40.249:2017/TrainImage/";
    //private String pathAPI = "http://120.24.92.214:8989//TrainImage/";
    private JSONObject LL = null;
    private int b;
    private String a;
    private ImageView iv;
    private boolean bool = false;
    private int n = 0;
    private String nums;
    private Message msgs;
    private List<int[]> imgs = new ArrayList<int[]>();
    java.util.Random random = new java.util.Random();// 定义随机类
    int p = 4;

    private ViewFlipper viewFlipper;
    Activity mActivity = null;
    Timer timer = null;
    private ImageView ima;

    private List<Animation> amims = new ArrayList<Animation>();
    int[] animsID = {R.anim.push_right_in, R.anim.push_left_in, R.anim.rotate_in, R.anim.scale_in, R.anim.push_right_out, R.anim.push_left_out, R.anim.rotate_out,
            R.anim.scale_out};
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            int result = random.nextInt(p);
            int result2 = random.nextInt(p);
            viewFlipper.setInAnimation(amims.get(result));
            viewFlipper.setOutAnimation(amims.get(result2 + p));
            viewFlipper.startFlipping();
        }
    };
    private void animation() {
        for (int i = 0; i < animsID.length; i++) {
            Animation Anim = AnimationUtils.loadAnimation(mActivity,
                    animsID[i]);
            amims.add(Anim);
        }
    }

    private void Init() {
        for (int i = 0; i < b; i++) { // 添加图片源
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bm[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(iv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiyanshi);
        //点击播放按钮
        image = new ArrayList<>();
        imags = (ImageView) findViewById(R.id.imgs);
        // ima = (ImageView) findViewById(R.id.imgs);
        vstar = (ImageView) findViewById(R.id.vedioicon);
        vstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                n = intent.getIntExtra("EN", 0);
                Intent intentved = new Intent(ShiyanshiActivity.this, labvedioone.class);
                intentved.putExtra("fn", n);
                startActivity(intentved);
            }
        });
        viewFlipper = (ViewFlipper) findViewById(R.id.show);
        viewFlipper.setAutoStart(false);
        mActivity = this;
        Intent intent = getIntent();
        n = intent.getIntExtra("EN", 0);
        nums = String.valueOf(n);
        loadpicture();

    }
    private void test(int s){
        LL = new JSONObject();//java代码封装为json字符串
        if(s==4){
            try {
                LL.put("TrainId", "515");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==1){
            try {
                LL.put("TrainId", "519");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==3){
            try {
                LL.put("TrainId", "516");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==5){
            try {
                LL.put("TrainId", "510");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==6){
            try {
                LL.put("TrainId", "512");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==2){
            try {
                LL.put("TrainId", "517");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(s==0){
            try {
                LL.put("TrainId", "520");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadpicture() {
        test(n);
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    String s = httpUrlConnection(pathAPI + "AndroidLogin", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = s;
                    mHandler1.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        //启动线程任务
        thread1.start();
        //开启一个线程
    }

    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                String date = "";
                date = (String) msg.obj;
                dateAfterSplit = date.split("~");         //以“/”作为分隔符来分割date字符串，并把结果放入3个字符串中。
                System.out.print(dateAfterSplit[0]);
                a = dateAfterSplit[0];
                b = Integer.valueOf(a);
                dateAfterSplit1 = new String[b];
                dateAfterSplit1 = date.split("~");
                for (int i = 1; i < b; i++)
                    System.out.print(dateAfterSplit1[i]);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //1: 确定网址
                        try {
                            //2:把网址封装为一个URL对象
                            InputStream is;
                            HttpURLConnection conn;
                            for (int i = 0; i < b; i++) {
                                URL url = new URL(dateAfterSplit1[i+1]);
                                //3:获取客户端和服务器的连接对象，此时还没有建立连接
                                conn = (HttpURLConnection) url.openConnection();
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
                                    is = conn.getInputStream();
                                    //读取流里的数据，构建成bitmap位图
                                    bm[i] = BitmapFactory.decodeStream(is);
                                    //发生更新UI的消息
                                }

                            }
                            msgs = handler.obtainMessage();
                            msgs.what = 1;
                            handler.sendMessage(msgs);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                //启动线程任务
                thread.start();
            }
        }
    };
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Init();
            animation();
            //特效
            timer = new Timer();
            timer.schedule(timerTask, 0, 4000);
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
