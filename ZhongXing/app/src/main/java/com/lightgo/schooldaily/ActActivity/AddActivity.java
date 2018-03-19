package com.lightgo.schooldaily.ActActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lightgo.schooldaily.R;

import org.json.JSONException;
import org.json.JSONObject;

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

public class AddActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private LinearLayout layout;
    private ImageButton finish;
    private MyListView mStuList;
    LinearLayout choose1;
    TextView dateDisplay1;
    LinearLayout choose2;
    TextView TimeDisplay2;
    int year;
    int month;
    int date;
    int hour;
    int minute;
    private EditText edit1;
    private EditText edit2;
    private TextView lext;
    private TextView mGMC;
    private EditText edit4;
    private String title="";
    private String daybegin="";
    private String name="";
    private String place="";
    private String info="";
    private String timebegin="";
    private Button mselect=null;
    private String ActivityInfo=null;
    Map<String, Object> map =null;
    public  List<Map<String, Object>> list =null;

    private String pathAPI = "http://47.93.35.144:8888//Home/";
    private String mGrade=null;
    private String mMajor=null;
    private String mClass=null;
    private String mInfo=null;
    private String test=null;
    private JSONObject LL = null;
    private List<String> data=new ArrayList<String>();
    private List<String> Grade=new ArrayList<String>();
    private List<String> Major=new ArrayList<String>();
    private List<String> Cla=new ArrayList<String>();
    private List<String> Stu=new ArrayList<String>();
    int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        Init();
        Clinck();
        time();
        map = new HashMap<String, Object>();
        list = new ArrayList<Map<String, Object>>();
        mStuList.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(AddActivity.this,StuInfoActivity.class);
        intent.putExtra("mGrade", Grade.get(i));
        intent.putExtra("mMajor", Major.get(i));
        intent.putExtra("mClass",Cla.get(i));
        startActivityForResult(intent, 2);
    }
    private List<Map<String, Object>> getData() {
        if(mInfo!=null) {
            data.add(mInfo);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0;i<data.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mInfo", data.get(i));
            list.add(map);
        }
        return  list;
    }
    private void NewSimpleAdapter(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.list_item_data,
                new String[]{"mInfo"},
                new int[]{R.id.mInfo});
        mStuList.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }
    private void time() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        year = t.year;
        month = t.month;
        date = t.monthDay;
        hour = t.hour; // 0-23
        minute = t.minute;
        dateDisplay1.setText(String.format("%d-%d-%d", year, month + 1, date));
        TimeDisplay2.setText(String.format("%d:%d", hour, minute));
    }
    private void Init() {
        choose1 = (LinearLayout) findViewById(R.id.begind);
        dateDisplay1 = (TextView) findViewById(R.id.beginday);
        mGMC = (TextView) findViewById(R.id.mGMC);
        choose2 = (LinearLayout) findViewById(R.id.begint);
        TimeDisplay2 = (TextView) findViewById(R.id.begintime);
        layout = (LinearLayout) findViewById(R.id.back);
        finish=(ImageButton)findViewById(R.id.finish);
        edit1=(EditText)findViewById(R.id.title);
        edit2=(EditText)findViewById(R.id.place);
        lext=(TextView)findViewById(R.id.info);
        edit4=(EditText)findViewById(R.id.mName);
        mselect=(Button)findViewById(R.id.mChoose);
        mStuList=(MyListView) findViewById(R.id.mStuInfo);
    }
    private void Clinck() {
        layout.setOnClickListener(this);
        lext.setOnClickListener(this);
        choose1.setOnClickListener(this);
        choose2.setOnClickListener(this);
        finish.setOnClickListener(this);
        mselect.setOnClickListener(this);
    }
    private void GetActivityInfo() {
        title=edit1.getText().toString();
        place=edit2.getText().toString();
        info=lext.getText().toString();
        daybegin=dateDisplay1.getText().toString();
        timebegin=TimeDisplay2.getText().toString();
        name=edit4.getText().toString();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.info:
                Intent intent2 = new Intent(AddActivity.this, EditActivity.class);
                startActivityForResult(intent2, 1);
                break;
            case R.id.begind:
                new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateDisplay1.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, date).show();
                break;
            case R.id.begint:
                new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TimeDisplay2.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                    //0,0指的是时间，true表示是否为24小时，true为24小时制
                }, hour, minute, true).show();
                break;
            case R.id.mChoose:
                Intent intent3 = new Intent(AddActivity.this, ChooseActivity.class);
                startActivityForResult(intent3, 0);
                mGMC.setVisibility(View.VISIBLE);
                break;
            case R.id.finish:
                GetActivityInfo();
                UpActivityInfo();
                AddActivity.this.finish();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                Bundle bundle1 = data.getExtras();
                mInfo = bundle1.getString("param");
                mGrade = bundle1.getString("mGrade");
                Grade.add(mGrade);
                mMajor = bundle1.getString("mMajor");
                Major.add(mMajor);
                mClass = bundle1.getString("mClass");
                Cla.add(mClass);
                test = bundle1.getString("test");
                NewSimpleAdapter();
                break;
            case 1:
                Bundle bundle = data.getExtras();
                String name = bundle.getString("name");
                lext.setText(name);
                break;
            case 2:
                Bundle bundle2 = data.getExtras();
                Stu=StuInfoActivity.ids;
                break;
        }
    }
    private void UpActivityInfo() {//上传活动的基本信息
        LL = new JSONObject();
        String iCount=Integer.toString(Stu.size());
        String StuData="";
        try {
            LL.put("title", title);
            LL.put("place", place);
            LL.put("info", info);
            LL.put("daytime", daybegin);
            LL.put("name", name);
            LL.put("StuCount",iCount);
            for(int i=0;i<Stu.size();i++)
            {
                StuData=StuData+Stu.get(i)+"~";
            }
            LL.put("StuId",StuData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String str = httpUrlConnection(pathAPI + "GetActivityInfo", LL.toString());//toString();把JSONArray对象转换为json格式的字符串
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

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

class MyListView  extends ListView {

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
