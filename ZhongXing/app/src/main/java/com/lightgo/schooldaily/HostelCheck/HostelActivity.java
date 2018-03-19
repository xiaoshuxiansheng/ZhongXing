package com.lightgo.schooldaily.HostelCheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lightgo.schooldaily.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HostelActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private Button mSelect;
    private ListView mCheckList;
    private String mInfo;
    private List<String> mdata=new ArrayList<String>();
    private List<String> m1data=new ArrayList<String>();
    private TextView textView=null;
    int year;
    int month;
    int date;
    int hour;
    int minute;
    String a;
    String d;
    private String[] b1=null;
    String b;
    String c;
    private  String text=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hostel);

        mSelect=(Button)findViewById(R.id.mSelect);
        mCheckList=(ListView) findViewById(R.id.mCheckList);
        textView=(TextView)this.findViewById(R.id.timer);
        mSelect.setOnClickListener(this);
        mCheckList.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(HostelActivity.this, HSelectActivity.class);
        startActivityForResult(intent, 0);
    }
    private List<Map<String, Object>> getData() {
        mdata.add(mInfo);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0;i<mdata.size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String mTime = sDateFormat.format(new java.util.Date());
            map.put("mInfo", mdata.get(i));
            map.put("textView",mTime);
            list.add(map);
        }
        return  list;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        a = bundle.getString("a");
        b = bundle.getString("b");
        c = bundle.getString("c");
        d = bundle.getString("d");
        m1data.add(b);
        mInfo = a + b + c + d;
        if(a != null && b != null &&c != null && d != null ){
            NewSimpleAdapter();
        }
    }

    private void NewSimpleAdapter()
    {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.hcheck_list,
                new String[]{"mInfo","textView"},
                new int[]{R.id.mInfo,R.id.timer});
        mCheckList.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),"111",Toast.LENGTH_LONG).show();
        String s = mdata.get(i);
        if(m1data.get(i).compareTo("早寝") == 0){
            Intent intent1 = new Intent(HostelActivity.this, HCheck1Activity.class);
            intent1.putExtra("info", mInfo);
            startActivity(intent1);
        }
        else{
            Intent intent = new Intent(HostelActivity.this, HCheckActivity.class);

            intent.putExtra("a", a);
            intent.putExtra("b", b);
            intent.putExtra("c", c);
            intent.putExtra("d", d);
            startActivity(intent);
        }
    }
}
