package com.lightgo.schooldaily.StudentJar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lightgo.schooldaily.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuodongInformation extends Activity implements View.OnClickListener {

    private ListView  listView2 = null;
    private LinearLayout hline = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_huodong_information);
        listadpter();
        hline = (LinearLayout) findViewById(R.id.huoreturn);
        hline.setOnClickListener(this);
    }
    private void listadpter()
    {

        listView2 = (ListView)findViewById(R.id.list2);


        SimpleAdapter sadapter = new SimpleAdapter(this, getData1(), R.layout.personhuodong,
                new String[]{"title", "time"}, new int[]{R.id.title,R.id.time1});
        listView2.setAdapter(sadapter);

    }
    private List<Map<String, Object>> getData1() {
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "活动标题");
        map.put("time", "2017.06.21");
        list1.add(map);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("title", "活动标题");
        map1.put("time", "2017.06.21");
        list1.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("title", "活动标题");
        map2.put("time", "2017.06.21");
        list1.add(map2);

        return list1;
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
