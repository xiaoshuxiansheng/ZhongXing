package com.lightgo.schooldaily.CheckActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightgo.schooldaily.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DSelectActivity extends Activity implements View.OnClickListener{

    private LinearLayout layout;
    private Button Mor=null;
    private Button Cla=null;
    private Button Eve=null;
    private Button Yiwu=null;
    private Button Yiliu=null;
    private Button YiQi=null;
    private Button YiQi1=null;
    private Button YiBa=null;
    private Button One=null;
    private Button Two=null;
    private Button Three=null;
    private Button Com=null;
    private Button Ele=null;
    private Button mFinish=null;
    private Resources resources = null;
    private Drawable btnDrawable = null;
    private Button flag1 = null;
    private Button flag2 = null;
    private Button flag3 = null;
    private Button flag4 = null;
    private String mCheckTime="";
    private String mGrade="";
    private String mClass="";
    private String mMajor="";
    private String mInfo="";
    private String pathAPI = "http://47.93.35.144:10001/Home/";
    private int GradeCount;
    private int MajorCount;
    private int ClassCount;
    private String[] mClassInfo;
    private String[] mGradeInfo;
    private String[] mMajorInfo;
    private String[] Split;
    private EditText editText;
    JSONObject LL = null;
    private View jiange=null;
    private String allCount=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dselect);
        resources = this.getResources();
        resources.getDrawable(R.drawable.roundedrect);
        Init();
        Getdata();
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Click();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent = getIntent();
        intent.putExtra("test", "");
        setResult(0, intent);
        DSelectActivity.this.finish();
        return true;
    }
    private void Click(){
        Mor.setOnClickListener(this);
        Cla.setOnClickListener(this);
        Eve.setOnClickListener(this);
        Com.setOnClickListener(this);
        Ele.setOnClickListener(this);
        Yiwu.setOnClickListener(this);
        Yiliu.setOnClickListener(this);
        YiQi1.setOnClickListener(this);
        YiQi.setOnClickListener(this);
        YiBa.setOnClickListener(this);
        One.setOnClickListener(this);
        Two.setOnClickListener(this);
        Three.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }
    private void Init(){
        layout = (LinearLayout) findViewById(R.id.pop_layout);
        Mor=(Button)findViewById(R.id.bt_Mor);
        Cla=(Button)findViewById(R.id.bt_Cla);
        Eve=(Button)findViewById(R.id.bt_Eve);
        flag1 = Mor;
        Yiwu=(Button)findViewById(R.id.bt_Yiwu);
        Yiliu=(Button)findViewById(R.id.bt_Yiliu);
        YiQi=(Button)findViewById(R.id.bt_YiQi);
        YiQi1=(Button)findViewById(R.id.bt_YiQi1);
        YiBa=(Button)findViewById(R.id.bt_YilBa);
        flag2 = Yiwu;
        Com=(Button)findViewById(R.id.bt_Com);
        Ele=(Button)findViewById(R.id.bt_Ele);
        flag3 = Com;
        One=(Button)findViewById(R.id.bt_One);
        Two=(Button)findViewById(R.id.bt_Two);
        Three=(Button)findViewById(R.id.bt_Three);
        flag4 = One;
        jiange=(View)findViewById(R.id.d);
        editText=(EditText)findViewById(R.id.AllCount);

        mFinish=(Button)findViewById(R.id.mFinish);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mFinish:
                allCount=editText.getText().toString();
                int len1=mCheckTime.length();
                int len2=mGrade.length();
                int len3=mMajor.length();
                int len4=mClass.length();
                int len5=allCount.length();
                if(len1==0|| len2 ==0||len3==0 ||len4==0||len5==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DSelectActivity.this);
                    builder.setMessage("请完善信息！");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    mInfo = mCheckTime + "" + mGrade + mMajor + mClass;
                    Intent intent = getIntent();
                    intent.putExtra("param", mInfo);
                    intent.putExtra("mGrade", mGrade);
                    intent.putExtra("mMajor", mMajor);
                    intent.putExtra("mClass", mClass);
                    intent.putExtra("allCount", allCount);
                    setResult(0, intent);
                    DSelectActivity.this.finish();
                }
                break;
            case R.id.bt_Mor:
            case R.id.bt_Cla:
            case R.id.bt_Eve:
                Button button1= (Button) view;
                mCheckTime =  button1.getText().toString();
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                flag1.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                view.setBackgroundDrawable(btnDrawable);
                flag1 = button1;
                break;
            case R.id.bt_Com:
            case R.id.bt_Ele:
                Button button2 = (Button) view;
                mMajor = button2.getText().toString();
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                flag2.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                view.setBackgroundDrawable(btnDrawable);
                flag2 = button2;
                break;
            case R.id.bt_Yiwu:
            case R.id.bt_Yiliu:
            case R.id.bt_YiQi:
            case R.id.bt_YiQi1:
            case R.id.bt_YilBa:
                Button button3 = (Button) view;
                mGrade = button3.getText().toString();
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                flag3.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                view.setBackgroundDrawable(btnDrawable);
                flag3 = button3;
                break;
            case R.id.bt_One:
            case R.id.bt_Two:
            case R.id.bt_Three:
                Button button4= (Button) view;
                mClass =button4.getText().toString();
                btnDrawable = resources.getDrawable(R.drawable.roundedrect);
                flag4.setBackgroundDrawable(btnDrawable);
                btnDrawable = resources.getDrawable(R.drawable.rebottonstyle);
                view.setBackgroundDrawable(btnDrawable);
                flag4 = button4;
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            Intent intent = getIntent();
            intent.putExtra("test", "");
            setResult(0, intent);
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
    public void Getdata() {

        LL = new JSONObject();//java代码封装为json字符串
        try {
            LL.put("UserId", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread()
        {
            @Override
            public void run()
            {
                String str = httpUrlConnection(pathAPI+"AndroidAllInfo", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                Message msg = new Message();
                msg.what=1;
                msg.obj=str;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                JSONObject q=null,p=null;
                String s=null;
                s=(String)msg.obj;
                Split=s.split(";");
                try {
                    q=new JSONObject(s);
                    GradeCount=q.getInt("GradeCount");
                    MajorCount=q.getInt("MajorCount");
                    ClassCount=q.getInt("ClassCount");
                    mGradeInfo =new String[GradeCount];
                    mMajorInfo=new String[MajorCount];
                    mClassInfo= new String[ClassCount];
                    for (int i=0;i<GradeCount;i++)
                    {
                        p=new JSONObject(Split[i+1]);
                        mGradeInfo[i]=p.getString("GradeName");
                    }
                    for (int i=GradeCount,j=0;i<GradeCount+MajorCount;i++)
                    {
                        p=new JSONObject(Split[i+1]);
                        mMajorInfo[j++]=p.getString("MajorName");
                    }
                    for (int i=GradeCount+MajorCount,k=0;i<GradeCount+MajorCount+ClassCount;i++)
                    {
                        p=new JSONObject(Split[i+1]);
                        mClassInfo[k++]=p.getString("ClassName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Yiwu.setText(mGradeInfo[0]);
                Yiliu.setText(mGradeInfo[1]);

                Com.setText(mMajorInfo[0]);
                Ele.setText(mMajorInfo[1]);

                One.setText(mClassInfo[0]);
                Two.setText(mClassInfo[1]);

                if(ClassCount==3)
                {
                    Three.setVisibility(View.VISIBLE);
                    Three.setText(mClassInfo[2]);
                }
                if(GradeCount==3)
                {
                    YiQi1.setVisibility(View.VISIBLE);
                    YiQi1.setText(mGradeInfo[2]);
                }
                else if(GradeCount==4)
                {
                    YiQi1.setVisibility(View.GONE);
                    YiQi.setVisibility(View.VISIBLE);
                    YiQi.setText(mGradeInfo[2]);
                    YiBa.setVisibility(View.VISIBLE);
                    YiBa.setText(mGradeInfo[3]);
                    jiange.setVisibility(View.VISIBLE);
                }
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
}

