package com.lightgo.schooldaily;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class TuanduiActivity extends ListActivity {
    private MainActivity _this;
    private String pathAPI = "http://120.77.40.249:2017//TeacherTeam/";
    private JSONObject LL=null;
    String[ ] dateAfterSplit= new String[1];
    String[ ] dateAfterSplit1=null;
    public static  String  wb;
    int b,c=5;
    private ListAdapter listAdapter=null;
    List<Map<String,Object>> listdata=null;
    HashMap<String,Bitmap> MyMap=new HashMap<String, Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Get();
        listdata=new ArrayList<Map<String, Object>>();
        listAdapter=new ListAdapter(this);
        setListAdapter(listAdapter);

    }
    private List<Map<String, Object>> getData() {
        for (int i = 1; i <= b * c; i += c) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mName", dateAfterSplit1[i]);
            map.put("mInfo", dateAfterSplit1[i+1]+"\n\n"+dateAfterSplit1[i+2]);
            map.put("mPhoto",dateAfterSplit1[i+3]);
            listdata.add(map);
        }
        return listdata;
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(position == -1)
            return;
        else{
            Intent intent = new Intent(TuanduiActivity.this, TWebActivity.class);
            wb=dateAfterSplit1[(position+1)*c];
            startActivity(intent);
        }
    }
    private String httpUrlConnection( String uriAPI,String requestString){
        StringBuffer sb = new StringBuffer();
        try{//建立连接
            URL url=new URL(uriAPI);
            HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
            String param = "UserStr="+ URLEncoder.encode(requestString,"UTF-8");//设置请求属性
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
        LL = new JSONObject();
        try {
            LL.put("TeacherID","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String s = httpUrlConnection(pathAPI+"AndroidLogin",LL.toString());
                Message msg = new Message();
                msg.obj=s;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    Handler mHandler = new Handler(){
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
            listAdapter.notifyDataSetChanged();
        }
    };
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            Load load=(Load)msg.obj;
            load.img.setImageBitmap(load.bitmap);
        }
    };
    class ListAdapter extends BaseAdapter {
        private Context context;
        ListAdapter(Context c){
            context=c;
        }
        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public Object getItem(int i) {
            return listdata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        //重用了convertView，很大程度上的减少了内存的消耗。通过判断convertView是否为null，
        // 是的话就需要产生一个视图出来，然后给这个视图数据，最后将这个视图返回给底层，呈献给用户。
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView =inflater.inflate(R.layout.activity_tuandui,null);

                viewHolder = new ViewHolder();
                viewHolder.mPhoto =(ImageView)convertView.findViewById(R.id.picture);
                viewHolder.mName =(TextView)convertView.findViewById(R.id.mingzi);
                viewHolder.mInfo=(TextView)convertView.findViewById(R.id.info);
                convertView.setTag(viewHolder);
            }else {
                viewHolder =(ViewHolder)convertView.getTag();
            }
            ListLoadPicture(viewHolder.mPhoto,listdata.get(position).get("mPhoto").toString());
            viewHolder.mName.setText((String)((HashMap)listdata.get(position)).get("mName"));
            viewHolder.mInfo.setText((String)((HashMap)listdata.get(position)).get("mInfo"));
            return convertView;
        }
    }
    void ListLoadPicture(ImageView img,String url)
    {
        if(!MyMap.containsKey(url)){
            LoadThread thread = new LoadThread(img, url);
            thread.start();
        }else {
            img.setImageBitmap(MyMap.get(url));
        }
    }
    class Load
    {
        public ImageView img;
        public Bitmap bitmap;
    }
    class LoadThread extends Thread implements Runnable{
        private ImageView mImg;
        private String mUrl;
        LoadThread(ImageView img,String url)
        {
            mImg=img;
            mUrl=url;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //1: 确定网址
            try {
                //2:把网址封装为一个URL对象
                InputStream is;
                HttpURLConnection conn;
                Message msg1=new Message();
                URL url = new URL(mUrl);//3:获取客户端和服务器的连接对象，此时还没有建立连接
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET"); //4:初始化连接对象
                conn.setConnectTimeout(5000);//设置连接超时
                conn.setReadTimeout(5000);  //设置读取超时
                conn.connect();      //5:发生请求，与服务器建立连接
                if (conn.getResponseCode() == 200) { //如果响应码为200，说明请求成功
                    is = conn.getInputStream(); //获取服务器响应头中的流
                    Load load=new Load();
                    load.img=mImg;
                    load.bitmap=BitmapFactory.decodeStream(is); //读取流里的数据，构建成bitmap位图
                    MyMap.put(mUrl,load.bitmap);
                    msg1 =handler.obtainMessage();//发生更新UI的消息
                    msg1.obj=load;
                    handler.sendMessage(msg1);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //避免了就是每次在getVIew的时候，都需要重新的findViewById，
    // 重新找到控件，然后进行控件的赋值以及事件相应设置。这样其实在做重复的事情)
    class ViewHolder{
        ImageView mPhoto;
        TextView mName;
        TextView mInfo;
    }
}
