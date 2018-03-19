package com.lightgo.schooldaily.StudyCommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lightgo.schooldaily.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BrowsList extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lstv;
    private RefreshableView refreshableView;
    private NewsBean newsBean;
    public static NewsAsyncTask newsAsyncTask ;
    private JSONArray jsonarray =new JSONArray();
    private JSONObject jsonObject = null;
    private Button post;
    private NewsAdapter newsadapter = null;
    public List<NewsBean> newsBeanList=new ArrayList<>();
    private LinearLayout mback;
    private String url = "http://120.77.40.249:2017/BBS/AndroidBrowse";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sq_brows_list);
       final NewsAsyncTask newsAsyncTask = new NewsAsyncTask();
       newsAsyncTask.execute(url);//启动多线程
       new setOnRefreshListener();
        init();
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
//                    GetJsonData(url);
//                    if(newsadapter == null){
//                        newsadapter=new NewsAdapter(BrowsList.this,newsBeanList);
//                        lstv.setAdapter(newsadapter);}
//                    else {
//                        newsadapter.onDateChange(BrowsList.this,newsBeanList);
//
//                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
        initevent();
    }

    private void initevent() {
        post.setOnClickListener(this);
        lstv.setOnItemClickListener(this);
        mback.setOnClickListener(this);
    }

    private void init() {
        lstv=(ListView)findViewById(R.id.lstv);
        mback=(LinearLayout)findViewById(R.id.mBack);
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        post = (Button)findViewById(R.id.post);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post: {
                Intent intent1=new Intent(BrowsList.this,WriteBBS.class);
                startActivity(intent1);
            }
            break;
            case R.id.mBack:
                finish();
                break;
            default:
                break;
        }
    }

    @Override// 发生点击动作的AdapterView,在AdapterView中被点击的视图,视图在adapter中的位置,被点击元素的行id。
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Bundle bundle = new Bundle();
        bundle.putString("TextID", String.valueOf(newsBeanList.get(i).TextID));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(BrowsList.this,BrowsContent.class);
        startActivity(intent);
    }

    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {


        @Override
        //后台处理
        protected List<NewsBean> doInBackground(String... params) {
            return GetJsonData(params[0]);      //耗时子线程处理，返回一个 List<NewsBean> 的 newsBeanList数组
        }

        @Override
        protected void onPostExecute(List<NewsBean> BeanList) {//更新UI
            super.onPostExecute(BeanList);
            //创建自定义baseadapter适配器
            newsadapter=new NewsAdapter(BrowsList.this,BeanList);
            lstv.setAdapter(newsadapter);//设置适配器贴到listview上
        }

    }

    private List<NewsBean> GetJsonData(String param){
        String jsonString =PostInfo(param);//获取postInfo传回来的值
        try {
            jsonObject = new JSONObject(jsonString);
            jsonarray = jsonObject.getJSONArray("data");
            for (int i=0;i<jsonarray.length();i++){
                newsBean=new NewsBean();
                jsonObject=new JSONObject();
                jsonObject=jsonarray.getJSONObject(i);
                newsBean.UserName=jsonObject.getString("UserName");
                newsBean.TextTitle=jsonObject.getString("TextTitle");
                newsBean.TextTime=jsonObject.getString("TextTime");
                newsBean.TextInfo=jsonObject.getString("TextInfo");
                newsBean.ReplyNum=jsonObject.getString("ReplyNum");
                newsBean.TextID=jsonObject.getString("TextID");
                newsBeanList.add(newsBean);
            }
                /*newsBean.Place=jsonObject.getString("TravelPlace");暂时没有数据*/
                /*newsBean.NickName=jsonObject.getString("TravelNickName");*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  newsBeanList;
    }

    private String PostInfo(String param){
        String s=new String();
        JSONObject LL=new JSONObject();
        try {
            LL.put("UserID","1");
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

    private class setOnRefreshListener {
        public void onRefresh() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshableView.finishRefreshing();
        }
    }
}
