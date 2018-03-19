package com.lightgo.schooldaily.AdminUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lightgo.schooldaily.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassRankActivity extends Activity {

    private ListView mClassRank_ListView = null;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> lists = null;
    private Map<String,Object> map = null;

    private String[]jj = {"15级","15级","16级","16级","17级","17级"};
    private String[]zy = {"计科","计科","计科","电信","电信","电信"};
    private String[]bj = {"一班","二班","一班","二班","一班","二班",};
    private String[]pm = {"1","2","3","4","5","6",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_class_rank);

        if(lists == null){
            lists = new ArrayList<Map<String, Object>>();
        }

        mClassRank_ListView = (ListView)findViewById(R.id.mClassRank_ListView);
        simpleAdapter = new SimpleAdapter(this,GetData(),R.layout.activity_class_rankex,
                new String[]{"jj","zy","bj","pm"},new int[]{R.id.jj,R.id.zy,R.id.bj,R.id.pm});
        mClassRank_ListView.setAdapter(simpleAdapter);
    }
    private List<Map<String,Object>> GetData(){
        for(int i = 0; i < jj.length; i++){
            map = new HashMap<String,Object>();
            map.put("jj",jj[i]);
            map.put("zy",zy[i]);
            map.put("bj",bj[i]);
            map.put("pm",pm[i]);
            lists.add(map);
        }
        return lists;
    }
}
