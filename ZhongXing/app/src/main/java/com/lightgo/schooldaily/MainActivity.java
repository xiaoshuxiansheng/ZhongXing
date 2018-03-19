package com.lightgo.schooldaily;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightgo.schooldaily.ActActivity.ActShowActivity;
import com.lightgo.schooldaily.CheckActivity.DailyCheckActivity;
import com.lightgo.schooldaily.CollegeIntroduce.IntroduceActvity;
import com.lightgo.schooldaily.CollegeIntroduce.MessageActivity;
import com.lightgo.schooldaily.CollegeIntroduce.ProcessActivity;
import com.lightgo.schooldaily.HostelCheck.HostelActivity;
import com.lightgo.schooldaily.StudyCommunity.BrowsList;
import com.lightgo.schooldaily.UserMessage.AdminActivity;
import com.lightgo.schooldaily.UserMessage.LoginActivity;
import com.lightgo.schooldaily.UserMessage.StudentActivity;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener,Handler.Callback{
    /*广告变量*/
    private LinearLayout mylayout = null;
    private boolean isVisible = true;
    private MainActivity _this;
    private ViewPager vp = null;
    private List<ImageView> image = null;
    private List<View> dot = null;
    private int currentItem;
    private ViewPagerAdapter adapter = null;
    private ScheduledExecutorService ses = null;
    private int oldp = 0;
    private ImageButton mjidi = null;
    private ImageButton mxyjs = null;
    private ImageButton mbslc = null;
    private ImageButton mxygg = null;
    private ImageButton my = null;
    private ImageButton msq=null;
    private ImageView iv;
    private Bitmap[] bm = new Bitmap[6];
    private String[] dateAfterSplit = new String[1];
    private String[] dateAfterSplit1 = null;
    private String[] Splits = null;
    private String pathAPI = "http://120.77.40.249:2017/Carousel/";
    private JSONObject LL = null;
    private int b;    ;
    private String a;
    public static Boolean Is_Login = false;
    public static Boolean Is_flag = false;
    public static Boolean Is_Key = false;
    private LinearLayout ll_mHomeImg,ll_mNagivationImg,ll_mFrdImg,ll_mSettingImg;
    private ImageButton mHomeImg,mNagivationImg,mFrdImg,mSettingImg;
    private Handler myHandler = null;
    private Handler h = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(!isNetworkAvailable(this)){
            setContentView(R.layout.nointernet);
            return;
        }
        setContentView(R.layout.activity_main);
        _this = this;
        MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadpicture();//加载图片

//        mylayout = (LinearLayout)findViewById(R.id.xiacaidang) ;
//        mylayout.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域

        h = new Handler(this);
        myHandler = h;
        HttpThread httpThread = new HttpThread("CollegeBulletin/AndroidCollegeBulletin",0);
        httpThread.start();

        vp = (ViewPager) findViewById(R.id.viewpager);
        my = (ImageButton)findViewById(R.id.grzx);
        mjidi = (ImageButton)findViewById(R.id.jd);
        mxyjs = (ImageButton)findViewById(R.id.xyjs);
        mbslc = (ImageButton)findViewById(R.id.bslc);
        mxygg = (ImageButton)findViewById(R.id.xygg);
        msq=(ImageButton)findViewById(R.id.sq);

        mHomeImg = (ImageButton)findViewById(R.id.id_tab_home_img) ;
        mNagivationImg = (ImageButton)findViewById(R.id.id_tab_nagivation_img) ;
        mFrdImg = (ImageButton)findViewById(R.id.id_tab_frd_img);
        mSettingImg = (ImageButton)findViewById(R.id.id_tab_settings_img);

        ll_mHomeImg = (LinearLayout)findViewById(R.id.id_tab_home);
        ll_mNagivationImg = (LinearLayout)findViewById(R.id.id_tab_nagivation) ;
        ll_mFrdImg = (LinearLayout)findViewById(R.id.id_tab_frd);
        ll_mSettingImg = (LinearLayout)findViewById(R.id.id_tab_settings);

//        ll_mHomeImg.setOnClickListener(this);
//        ll_mNagivationImg.setOnClickListener(this);
//        ll_mFrdImg.setOnClickListener(this);
//        ll_mSettingImg.setOnClickListener(this);

        mjidi.setOnClickListener(this);
        mxyjs.setOnClickListener(this);
        mbslc.setOnClickListener(this);
        mxygg.setOnClickListener(this);
        msq.setOnClickListener(this);
       // my.setOnClickListener(this);
        //显示图片
        image = new ArrayList<ImageView>();
        //显示小点
        dot = new ArrayList<View>();
        dot.add(findViewById(R.id.dot_0));
        dot.add(findViewById(R.id.dot_1));
        dot.add(findViewById(R.id.dot_2));
        dot.add(findViewById(R.id.dot_3));
        dot.add(findViewById(R.id.dot_4));
        dot.add(findViewById(R.id.dot_5));

        adapter = new ViewPagerAdapter();
        vp.setAdapter(adapter);

        //页面变化监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                dot.get(position).setBackgroundResource(R.drawable.dot_focused);
                dot.get(oldp).setBackgroundResource(R.drawable.dot_normal);
                oldp = position;
                currentItem = position;
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            public void onPageScrollStateChanged(int agr0) {
            }
        });
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //******************音量下键呼出导航栏点名
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(Is_Login && Is_flag) //登录且具有权限
            {
                if (isVisible) {
                    isVisible = false;
                    mylayout.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    mylayout.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    private void resetImg() {
        mHomeImg.setImageResource(R.drawable.banji1);//加载图片，会根据设备分辨率进行图片大小缩放适配
        mNagivationImg.setImageResource(R.drawable.kaoqing2);
        mFrdImg.setImageResource(R.drawable.zzx2);
        mSettingImg.setImageResource(R.drawable.huodong1);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tab_home: {  //******************导航栏的返回事件按钮
                resetImg();
                mFrdImg.setImageResource(R.drawable.zzx1);
                if (isVisible) {
                    isVisible = false;
                    mylayout.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    mylayout.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
                break;
            }
            case R.id.id_tab_nagivation: {  //******************晚归点名按钮
                resetImg();
                mHomeImg.setImageResource(R.drawable.banji2);
                Intent HCheckIntent = new Intent(MainActivity.this, HostelActivity.class);
                startActivity(HCheckIntent);
                break;
            }
            case R.id.id_tab_frd: {  //******************日常点名按钮
                resetImg();
                mNagivationImg.setImageResource(R.drawable.kaoqing1);
                Intent DCheckIntent = new Intent(MainActivity.this, DailyCheckActivity.class);
                startActivity(DCheckIntent);
                break;
            }
            case R.id.id_tab_settings: {  //******************活动按钮
                resetImg();
                mSettingImg.setImageResource(R.drawable.huodong2);
                Intent ActCheckIntent = new Intent(MainActivity.this, ActShowActivity.class);
                startActivity(ActCheckIntent);
                break;
            }
            case R.id.grzx:
                if(!Is_Login){
                    Intent mLoginIntent = new Intent(MainActivity.this, LoginActivity.class);  //******************个人中心根据mUserType判断是否有权限
                    startActivity(mLoginIntent);
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("test",
                            Activity.MODE_PRIVATE);
                    String mUserName = sharedPreferences.getString("UserName", "");
                    String mPassWord = sharedPreferences.getString("PassWord", "");
                    String mUserType = sharedPreferences.getString("UserType", "");
                    JumpPage(mUserType);
                }
                break;
            case R.id.xyjs:
                Intent xyjsIntent = new Intent(MainActivity.this,IntroduceActvity.class);
                startActivity(xyjsIntent);
                break;
            case R.id.jd:
                Intent jdIntent = new Intent(MainActivity.this,JidiActivity.class);
                startActivity(jdIntent);
                break;
            case R.id.bslc:
                Intent bslcIntent = new Intent(MainActivity.this,ProcessActivity.class);
                startActivity(bslcIntent);
                break;
            case R.id.xygg:
                Intent xyggIntent = new Intent(MainActivity.this,MessageActivity.class);
                startActivity(xyggIntent);
                break;
            case R.id.sq:
                if(!Is_Login){
                    Intent mLoginIntent = new Intent(MainActivity.this, LoginActivity.class);  //******************个人中心根据mUserType判断是否有权限
                    startActivity(mLoginIntent);
                } else {
                    Intent mLoginIntent = new Intent(MainActivity.this, BrowsList.class);  //******************个人中心根据mUserType判断是否有权限
                    startActivity(mLoginIntent);
                }
                //Intent sqIntent=new Intent(MainActivity.this, BrowsList.class);
                //startActivity(sqIntent);
                break;
            default:
                Log.d("TAG",view.getId() + "");
                break;
        }
    }

    private void JumpPage(String mUserType){
        switch(mUserType){
            case "1\n":  //*************************到管理员的个人中心 1为辅导员 2为普通学生
                Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                startActivity(intent);
                break;
            case "2\n":
                Intent sintent = new Intent(MainActivity.this,StudentActivity.class);
                startActivity(sintent);
                break;
            default:
                break;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String str = (String)msg.obj;
                Splits = new String[5];
                Splits = str.split(";");
                TextView textView = (TextView) findViewById(R.id.roll);
                RollTestView rollTestView = new RollTestView(textView);
                for(int i = 0; i < 5; i++){
                    rollTestView.AddItem(Splits[i]);
                }
                break;
            default:
                break;
        }
        return false;
    }

    //自定义Adapter类，它继承自PagerAdapter，PagerAdapter中实现了图片切换的动画效果
    private class ViewPagerAdapter extends PagerAdapter {
        public int getCount() {//getCount获取条目数量
            return image.size();//返回当前要滑动图片的个数
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//判断key是否和View相等
        }

        //该方法实现的功能是移除一个给定位置的页面。适配器有责任从容器中删除这个视图。
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(image.get(position));
        }

        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(image.get(position));//将指定的position视图添加到view中并显示出来
            return image.get(position);//返回一个代表新增视图页面的Object（Key），没必要非要返回视图本身
        }
    }

    protected void onStart() {
        super.onStart();
        ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new ViewPageTask(), 2, 2, TimeUnit.SECONDS);
    }

    private class ViewPageTask implements Runnable {
        public void run() {
            currentItem = (currentItem + 1) % image.size();
            mHandler.sendEmptyMessage(0);
        }
    }

    //接收ViewPageTask消息完成UI更新操作
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            vp.setCurrentItem(currentItem);
        }
    };

    //在这个方法中主要实现的操作就是停止线程池的执行，释放线程池资源。
    protected void onStop() {
        super.onStop();
        if (ses != null) {
            ses.shutdown();
            ses = null;
        }
    }
    private void loadpicture() {
        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("ImageId", "zx01");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                for (int i = 1; i <= b; i++)
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
                            for (int i = 0; i <=b; i++) {
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
                                    Message msg = handler.obtainMessage();
                                    msg.what = i;
                                    handler.sendMessage(msg);
                                }
                            }
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
        public void handleMessage(android.os.Message msg) {
            if (msg.what >= 0 && msg.what <= 6) {
                iv = new ImageView(_this);
                iv.setImageBitmap(bm[msg.what]);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                image.add(iv);
                adapter.notifyDataSetChanged();
            } else {
                super.handleMessage(msg);
            }
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

    class HttpThread extends Thread implements Runnable {
        private String pathAPI = "http://120.77.40.249:2017/";
        private String mAction;
        private int mWhat;
        HttpThread(String Action, int what){
            mAction = Action;
            mWhat = what;
        }

        @Override
        public void run() {
            try {
                String s = httpUrlConnection(pathAPI + mAction, "");//toString();把JSONArray对象转换为json格式的字符串
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
