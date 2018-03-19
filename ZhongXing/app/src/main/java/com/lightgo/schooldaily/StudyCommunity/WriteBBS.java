package com.lightgo.schooldaily.StudyCommunity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lightgo.schooldaily.R;
import com.lightgo.schooldaily.StudyCommunity.Sq_util.DateUtils;
import com.lightgo.schooldaily.StudyCommunity.Sq_util.ImageUtils;
import com.lightgo.schooldaily.StudyCommunity.Sq_util.SDCardUtil;
import com.lightgo.schooldaily.StudyCommunity.Sq_util.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static me.iwf.photopicker.PhotoPicker.REQUEST_CODE;

/**
 * 新建笔记
 */
public class WriteBBS extends AppCompatActivity {

    private EditText et_new_title;
    private RichTextEditor et_new_content;
    private TextView tv_new_time;
    private TextView tv_new_group;

    private String myTitle;
    private String myContent;
    private String myGroupName;
    private String myNoteTime;
    private int flag;//区分是新建笔记还是编辑笔记

    private static final int cutTitleLength = 20;//截取的标题长度

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private int screenWidth;
    private int screenHeight;
    private Subscription subsLoading;
    private Subscription subsInsert;

    private String pathAPI = "http://120.77.40.249:2017/BBS/";

    private JSONObject LL=null;
    private  final int MIN_CLICK_DELAY_TIME = 1000;
    private  long lastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sq_write_bbs);

        initView();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回箭头
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        et_new_title = (EditText) findViewById(R.id.et_new_title);
        et_new_content = (RichTextEditor) findViewById(R.id.et_new_content);
        tv_new_time = (TextView) findViewById(R.id.tv_new_time);

            setTitle("发帖");
            myNoteTime = DateUtils.date2string(new Date());
            tv_new_time.setText(myNoteTime);

    }

    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        int i = 0;
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr).append("$^");
            } else if (itemData.imageurl != null) {
                //    content.append("<img src=\""+itemData.imageurl+"\"/>");
                content.append(itemData.imageurl).append("$&");
            }
        }
        return content.toString();
    }

    private void saveNoteData(boolean isBackground) {
        String noteTitle = et_new_title.getText().toString();
        String noteContent = getEditData();
        String noteTime = tv_new_time.getText().toString();
            if (flag == 0 ) {//新建笔记
                if (noteTitle.length() == 0 && noteContent.length() == 0) {
                    if (!isBackground){
                        Toast.makeText(WriteBBS.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    flag = 1;//插入以后只能是编辑
                    if (!isBackground){
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
    }


    public void Upload() {
        LL = new JSONObject();
        try {
            LL.put("TextTime", tv_new_time.getText().toString());
            LL.put("UserID", "1");
            LL.put("TextTitle", et_new_title.getText().toString());
            LL.put("TextInfo", getEditData());
            LL.put("TextLength", getEditData().length()+"");
//            LL.put("TravelImg",jsonimg);
//            LL.put("TravelTxt",jsontxt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                String s = httpUrlConnection(pathAPI + "AndroidUpload", LL.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = s;
            }

        };
        thread.start();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_image:
                callGallery();
                break;
            case R.id.action_new_post:
                saveNoteData(false);
                long curClickTime = System.currentTimeMillis();
                if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    Upload();}
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 调用图库选择
     */
    private void callGallery(){
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //已经禁止提示了
                Toast.makeText(WriteBBS.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            }

        } else {
        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, REQUEST_CODE);
    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    PhotoPicker.builder()
                            .setPhotoCount(5)//可选择图片数量
                            .setShowCamera(true)//是否显示拍照按钮
                            .setShowGif(true)//是否显示动态图
                            .setPreviewEnabled(true)//是否可以预览
                            .start(this, REQUEST_CODE);
                }else{
                    //用户拒绝授权
                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     * @param data
     */
    private void insertImagesSync(final Intent data){
        insertDialog.show();

        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    et_new_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(WriteBBS.this);
                    int height = ScreenUtils.getScreenHeight(WriteBBS.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        //Log.i("NewActivity", "###imagePath="+imagePath);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), " ");
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_new_content.insertImage(imagePath, et_new_content.getMeasuredWidth());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则启用密码锁
//        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
//                CommonUtil.isLockScreeen(getApplicationContext())){
//            saveNoteData(true);//处于后台时保存数据
//        }
    }

    /**
     * 退出处理
     */
    private void dealwithExit(){
//        String noteTitle = et_new_title.getText().toString();
//        String noteContent = getEditData();
//        String groupName = tv_new_group.getText().toString();
//        String noteTime = tv_new_time.getText().toString();
//        if (flag == 0) {//新建笔记
//            if (noteTitle.length() > 0 || noteContent.length() > 0) {
//                saveNoteData(false);
//            }
//        }else if (flag == 1) {//编辑笔记
//            if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                    || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                saveNoteData(false);
//            }
//        }
        finish();
    }
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        dealwithExit();
    }
}
