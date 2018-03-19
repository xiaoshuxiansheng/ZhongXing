package com.lightgo.schooldaily.StudyCommunity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lightgo.schooldaily.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BrowsContent extends AppCompatActivity implements View.OnClickListener {

    private TextView write;

    private LinearLayout myLinearLayout;
    private View childView,headerView,commment_headView;
    private TextView title,time,author,str;
    private LayoutInflater inflater;
    private String T_title,T_author,T_time,T_content;
    private String post_id,mesg;
    private String content_url="http://120.77.40.249:2017/BBS/BrowseContent";
    private JSONObject LL=null;

    private JSONArray jsonarray =new JSONArray();
    private JSONObject jsonObject = null;
    private NewsBean newsBean;
    private ListView lstv;
    public List<NewsBean> newsBeanList=new ArrayList<>();
    private String comment_url = "http://120.77.40.249:2017/BBS/AndroidComment";
    private int mark = 0;
    String TextID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sq_brows_contents);
        Bundle bundle = getIntent().getExtras();
        TextID = bundle.getString("TextID");
        initView();
        initdata();
        final NewsAsyncTask newsAsyncTask = new NewsAsyncTask();
        newsAsyncTask.execute(comment_url);//启动多线程

    }
    Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                JSONObject q=null;
                try {
                    q=new JSONObject((String) msg.obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    T_title=q.getString("TextTitle");
                    T_time=q.getString("TextTime");
                    T_author=q.getString("UserID");
                    T_content=q.getString("TextInfo");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                title.setText(T_title);
                time.setText(T_time);
                author.setText(T_author);
                final String content=T_content;
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap mbitmap = null;
                        String s;
                        int j=0;
                        for(int i=0;i<content.length();i++){
                            if(content.charAt(i)=='$'){
                                if(content.charAt(i+1)=='^'){
                                    s=content.substring(j,i);
                                    j=i+2;
                                    i++;
                                    Message msg = new Message();
                                    msg.what=0;
                                    msg.obj=s;
                                    mHandler.sendMessage(msg);
                                }
                                else if(T_content.charAt(i+1)=='&'){
                                    String imgurl=content.substring(j,i);
                                    try {
                                        URL url = new URL(imgurl);
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setRequestMethod("GET");
                                        conn.setConnectTimeout(5000);
                                        conn.setReadTimeout(5000);
                                        conn.connect();
                                        if (conn.getResponseCode() == 200) {
                                            InputStream is = conn.getInputStream();
                                            mbitmap = BitmapFactory.decodeStream(is);
                                        }
                                        j=i+2;
                                        i++;
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.what=1;
                                    msg.obj=mbitmap;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                    }
                });
                thread.start();
            }
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String s=(String)msg.obj;
                    childView = inflater.inflate(R.layout.sq_textcontent, null);
                    myLinearLayout.addView(childView,mark);
                    TextView str=(TextView)childView.findViewById(R.id.con_str);
                    str.setText(s);
                    mark++;
                    break;
                case 1:
                    Bitmap mbitmap=(Bitmap)msg.obj;
                    childView = inflater.inflate(R.layout.sq_imagecontent, null);
                    myLinearLayout.addView(childView,mark);
                    ImageView imageview=(ImageView)childView.findViewById(R.id.con_image);
                    imageview.setImageBitmap(mbitmap);
                    mark++;
                    break;
            }
        }
    };

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitle("详情");
        lstv=(MyListView) findViewById(R.id.lstv);

//        lstv.smoothScrollTo(0, 0);
        write=(TextView)findViewById(R.id.writeComment);
        write.setOnClickListener(this);
        title=(TextView)findViewById(R.id.title);
        time=(TextView)findViewById(R.id.time);
        author=(TextView)findViewById(R.id.author);
        myLinearLayout=(LinearLayout)findViewById(R.id.post_content);
        inflater = LayoutInflater.from(getApplicationContext());

    }

    private void initdata() {
        LL = new JSONObject();
        try {
            LL.put("TextID",TextID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mesg= httpUrlConnection(content_url,LL.toString());
//                Message msg = new Message();
//                msg.what=1;
//                msg.obj=mesg;
//                mHandler1.sendMessage(msg);
//            }
//        }).start();
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
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.writeComment){
            Intent intent=new Intent(BrowsContent.this,WriteComment.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {
        @Override
        //后台处理
        protected List<NewsBean> doInBackground(String... params) {

                    mesg= httpUrlConnection(content_url,LL.toString());
                    Message msg = new Message();
                    msg.what=1;
                    msg.obj=mesg;
                    mHandler1.sendMessage(msg);



            return GetJsonData(params[0]);      //耗时子线程处理，返回一个 List<NewsBean> 的 newsBeanList数组
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {//更新UI
            super.onPostExecute(newsBeen);
            //创建自定义baseadapter适配器
            NewAdapter newsadapter=new NewAdapter(BrowsContent.this,newsBeen);
            lstv.setAdapter(newsadapter);//设置适配器贴到listview上
        }

    }
    private List<NewsBean> GetJsonData(String param){
        String jsonString =PostInfo(param);//获取postInfo传回来的值
        try {
            jsonObject = new JSONObject(jsonString);
            jsonarray = jsonObject.getJSONArray("data");
            for (int i=0;i<jsonarray.length()-1;i++){
                newsBean=new NewsBean();
                jsonObject=new JSONObject();
                jsonObject=jsonarray.getJSONObject(i);
                newsBean.UserName=jsonObject.getString("CommentUser");
                newsBean.TextTime=jsonObject.getString("CommenTime");
                newsBean.TextInfo=jsonObject.getString("CommenInfo");
                newsBeanList.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  newsBeanList;
    }

    private String PostInfo(String param){
        String s=new String();
        JSONObject LL=new JSONObject();
        try {
            LL.put("TextID",TextID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        s=ReadStream(param , LL.toString());
        return  s;
    }
    private String ReadStream(String uriAPI, String requestString) {


        StringBuffer sb = new StringBuffer();
        try {
//建立连接
            URL url = new URL(uriAPI);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            String param = "UserStr=" + URLEncoder.encode(requestString, "UTF-8");
//设置请求属性
//获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            httpConn.setDoOutput(true); //需要输出
            httpConn.setDoInput(true); //需要输入
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
    }//获得字符串方式1
}

class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //此处是代码的关键
        //MeasureSpec.AT_MOST的意思就是wrap_content
        //Integer.MAX_VALUE >> 2 是使用最大值的意思,也就表示的无边界模式
        //Integer.MAX_VALUE >> 2 此处表示是福布局能够给他提供的大小
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
