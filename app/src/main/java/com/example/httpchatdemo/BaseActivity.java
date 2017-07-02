package com.example.httpchatdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.httpchatdemo.CustomView.ChatBottomView;
import com.example.httpchatdemo.CustomView.ExpandGridView;
import com.example.httpchatdemo.adapter.ChatRecyclerAdapter;
import com.example.httpchatdemo.adapter.ExpressionAdapter;
import com.example.httpchatdemo.adapter.ExpressionPagerAdapter;
import com.example.httpchatdemo.bean.ChatMessageBean;
import com.example.httpchatdemo.utils.FileSaveUtil;
import com.example.httpchatdemo.utils.ImageCheckoutUtil;
import com.example.httpchatdemo.utils.KeyBoardUtils;
import com.example.httpchatdemo.utils.PictureUtil;
import com.example.httpchatdemo.utils.SmileUtils;
import com.example.httpchatdemo.utils.ToastUtils;
import com.example.httpchatdemo.utils.Utils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/2.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, ChatBottomView.OnHeadIconClickListener{
    public RecyclerView mList;
    public RelativeLayout root_rl;
    public EditText mEditTextContent;
    public ImageView mess_iv;
    public ImageView emoji;
    public ImageView voiceIv;
    public ViewPager expressionViewpager;
    public LinearLayout emoji_group;
    public TextView send_emoji_icon;
    public ChatBottomView tbbv;
    public String camPicPath;
    public List<String> reslist;
    public List<ChatMessageBean> tblist = new ArrayList<ChatMessageBean>();
    public ChatRecyclerAdapter tbAdapter;
    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
    /**
     * 发送文本消息
     */
    protected abstract void sendMessage();

    /**
     * 发送图片文件
     *
     * @param filePath
     */
    protected abstract void sendImage(String filePath);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
    }
    private void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mList = (RecyclerView)findViewById(R.id.content_lv);
        root_rl = (RelativeLayout)findViewById(R.id.layout_root_rl);
        mEditTextContent = (EditText) findViewById(R.id.mess_et);
        mess_iv = (ImageView) findViewById(R.id.mess_iv);
        emoji = (ImageView) findViewById(R.id.emoji);
        voiceIv = (ImageView) findViewById(R.id.voice_iv);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emoji_group = (LinearLayout) findViewById(R.id.emoji_group);
        send_emoji_icon = (TextView) findViewById(R.id.send_emoji_icon);
        tbbv = (ChatBottomView) findViewById(R.id.other_lv);
    }
    private void initData() {
        mEditTextContent.setOnKeyListener(onKeyListener);
        mess_iv.setOnClickListener(this);
        send_emoji_icon.setOnClickListener(this);
        mList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyBoardUtils.hideKeyBoard(BaseActivity.this,mEditTextContent);
                emoji_group.setVisibility(View.GONE);
                tbbv.setVisibility(View.GONE);
                mess_iv.setBackgroundResource(R.mipmap.tb_more);
                return false;
            }
        });
        tbbv.setOnHeadIconClickListener(this);
        emoji.setOnClickListener(this);
        mEditTextContent.setOnClickListener(this);
        setEmojiExpression();
        setRecyclerList();
    }
    /**
     * 设置聊天列表
     */
    private void setRecyclerList() {
        tbAdapter = new ChatRecyclerAdapter(this, tblist);
        LinearLayoutManager lm = new LinearLayoutManager(this);
//        lm.setStackFromEnd(true);
        mList.setLayoutManager(lm);
        mList.setAdapter(tbAdapter);

    }

    private void setEmojiExpression() {
        // 表情list
        reslist = Utils.getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    /**
     * 底部菜单点击事件
     * @param from
     */
    @Override
    public void onClick(int from) {
        switch (from) {
            case ChatBottomView.FROM_CAMERA:
                RxPermissions.getInstance(this)
                        .request(Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) { // 在android 6.0之前会默认返回true
                                    // 已经获取权限
                                    final String state = Environment.getExternalStorageState();
                                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                                        camPicPath = getSavePicPath();
                                        Intent openCameraIntent = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        Uri uri = Uri.fromFile(new File(camPicPath));
                                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(openCameraIntent,
                                                ChatBottomView.FROM_CAMERA);
                                    } else {
                                        ToastUtils.showToast("请检查内存卡");
                                    }
                                } else {
                                    // 未获取权限
                                    ToastUtils.showToast( "权限未开通\n请到设置中开通相册权限");
                                }
                            }
                        });

                break;
            case ChatBottomView.FROM_GALLERY:
                RxPermissions.getInstance(this)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if(granted){
                                    String status = Environment.getExternalStorageState();
                                    if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                        Intent intent = new Intent();
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                        } else {
                                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            intent.putExtra("crop", "true");
                                            intent.putExtra("scale", "true");
                                            intent.putExtra("scaleUpIfNeeded", true);
                                        }
                                        intent.setType("image/*");
                                        startActivityForResult(intent,
                                                ChatBottomView.FROM_GALLERY);
                                    } else {
                                        ToastUtils.showToast("没有SD卡");
                                    }
                                }else{
                                    ToastUtils.showToast("权限未开通\n请到设置中开通相册权限");
                                }
                            }
                        });
                break;

        }
    }
    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情

                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("com.example.httpchatdemo.utils.SmileUtils");
                        Field field = clz.getField(filename);
                        mEditTextContent.append(SmileUtils.getSmiledText(BaseActivity.this,
                                (String) field.get(null)));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                            int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = mEditTextContent.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        mEditTextContent.getEditableText().delete(i, selectionStart);
                                    else
                                        mEditTextContent.getEditableText().delete(selectionStart - 1,
                                                selectionStart);
                                } else {
                                    mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tbbv.setVisibility(View.GONE);
            mess_iv.setBackgroundResource(R.mipmap.tb_more);
            switch (requestCode) {
                case ChatBottomView.FROM_CAMERA:

                    break;
                case ChatBottomView.FROM_GALLERY:
                    Uri uri = data.getData();
                    String path = FileSaveUtil.getPath(getApplicationContext(), uri);
                    File mCurrentPhotoFile = new File(path); // 图片文件路径
                    if (mCurrentPhotoFile.exists()) {
                        int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(path));
                        if (size > IMAGE_SIZE) {
                            showDialog(path);
                        } else {
                            sendImage(path);
                        }
                    } else {
                        ToastUtils.showToast("该文件不存在!");
                    }

                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDialog(final String path) {
        // // TODO Auto-generated method stub
        try {
            String GalPicPath = getSavePicPath();
            Bitmap bitmap = PictureUtil.compressSizeImage(path);
            boolean isSave = FileSaveUtil.saveBitmap(
                    PictureUtil.reviewPicRotate(bitmap, GalPicPath),
                    GalPicPath);
            File file = new File(GalPicPath);
            if (file.exists() && isSave) {
                sendImage(GalPicPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        return dir + fileName;
    }

}
