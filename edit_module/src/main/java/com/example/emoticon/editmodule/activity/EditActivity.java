package com.example.emoticon.editmodule.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.utils.ToastUtils;
import com.example.emoticon.editmodule.Graffiti.Doodle;
import com.example.emoticon.editmodule.Graffiti.Eraser;
import com.example.emoticon.editmodule.Operate.OperateUtils;
import com.example.emoticon.editmodule.Operate.OperateView;
import com.example.emoticon.editmodule.Operate.TextObject;
import com.example.emoticon.editmodule.R;
import com.example.emoticon.editmodule.widget.ColorPickerDialog;
import com.example.emoticon.editmodule.widget.DrawBitmap;
import com.example.emoticon.editmodule.widget.QuitMakeDialog;
import com.example.emoticon.editmodule.widget.TextEditBox;
import com.example.emoticon.editmodule.widget.TextInputDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EditActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "EditActivity";
    public static final String filePath = Environment.getExternalStorageState() + "/PictureTest/";
    private LinearLayout content_layout;//画布内容
    private ImageButton btn_back;
    private TextView btn_next;
    private ImageView images;
    private Toolbar toolbar_edit;
    private BottomNavigationView mBottomNavigation;
    private TextEditBox mTextEditBox;
    private RelativeLayout mRContainer;
    //文字集合
    private List<TextEditBox> mTextList = new ArrayList<>();
    //文本贴图输入框
    private EditText mInputText;
    //确定键
    private TextView confirm;
    private TextInputDialog dialog;

    private Doodle mDoodle;

    private Eraser mEraser;

    private ColorPickerDialog mColorDialg;

    private QuitMakeDialog mQuitMakeDialog;

    private TextView mNext;

    private DrawBitmap mDrawBitmap;

//    private Bitmap mBitmap;
    private String mBitmapPath;

    public static final int MSG_BITMAP = 1;

    private String camera_path;

    private String mPath = null;

    private OperateView operateView;

    OperateUtils operateUtils;

    private Timer timer = new Timer();

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_BITMAP:
                    timer.cancel();
                    fillContent();
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.btn_next) {
            btnNext();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        mBitmapPath = this.getIntent().getStringExtra("bitmap");
        operateUtils = new OperateUtils(this);

        timer.schedule(timerTask,10,1000);

//        new TextEditBox(this).setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
//            @Override
//            public void onTextEditClick(TextEditBox textEditBox) {
//                showInputDialog(mTextEditBox);
//            }
//        });
    }

    private void initView(){
        //images = findViewById(R.id.photo);
        //toolbar_edit = findViewById(R.id.toolbar_edit);
        content_layout = findViewById(R.id.mainLayout);
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        mBottomNavigation = findViewById(R.id.bottomNavigation_paint);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null);//删除默认的选中效果
        //mRContainer = findViewById(R.id.relative_layout_img);
        mInputText = findViewById(R.id.dialog_text);
        mTextEditBox = findViewById(R.id.text_edit_box);
        confirm = findViewById(R.id.confirm);
        mDoodle = findViewById(R.id.surfaceview);
        mEraser = findViewById(R.id.eraser);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }
    /**
     * 编辑页面上 绘制添加文字的背景图片
     */
   private void fillContent(){
       Bitmap resizeBitmap = BitmapFactory.decodeFile(mBitmapPath);
       int width = resizeBitmap.getWidth();
       operateView = new OperateView(this,resizeBitmap);
       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
               resizeBitmap.getWidth(), resizeBitmap.getHeight());
       operateView.setLayoutParams(layoutParams);
       content_layout.addView(operateView);
       operateView.setMultiAdd(true);//设置此参数，可以添加多个文字
   }

   private void btnNext(){
       operateView.save();
       Bitmap bmp = getBitmapByView(operateView);
       Log.e(TAG,String.valueOf(bmp.getWidth()));
       Log.e(TAG,String.valueOf(bmp.getHeight()));
       if(bmp != null){
           mPath = saveBitmapToCacheDir(bmp);
           Intent intent = new Intent(EditActivity.this,FinishActivity.class);
           intent.putExtra("camera_path",mPath);
           startActivity(intent);
       }
   }

    /**
     * 将模板view的图片转换为Bitmap
     */
   public Bitmap getBitmapByView(View v){
       Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(), Bitmap.Config.ARGB_8888);
       Canvas canvas = new Canvas(bitmap);
       v.draw(canvas);
       return bitmap;
   }

    /**
     * 将生成的图片保存在内存中
     */
   public String saveBitmap(Bitmap bitmap,String name){
       if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
           File dirFile = new File(filePath);
           if(!dirFile.exists()){
               dirFile.mkdirs();
           }
           File file = new File(filePath + name + ".jpg");
           FileOutputStream out;
           try {
               out = new FileOutputStream(file);
               if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,out)){
                   out.flush();
                   out.close();
               }
               return file.getAbsolutePath();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }catch (IOException e){
               e.printStackTrace();
           }
       }
       return null;
   }

    public static String saveBitmapToCacheDir(Bitmap bitmap){
        File cacheDir = ResourcesManager.getAppContext().getExternalCacheDir();
        String name = "resultBitmap";
        File file = new File(cacheDir, name);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,fileOutputStream);
            fileOutputStream.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加文本框实现
     */
    private void addText(){
        TextObject textObj = operateUtils.getTextObject("点击编辑文本框",
                operateView,5,150,100);
        if(textObj != null){
            operateView.addItem(textObj);
            operateView.setOnClickListener(new OperateView.MyListener() {
                @Override
                public void onClick(TextObject textObject) {
                    showInputDialog(textObject);
                }
            });
        }
    }

   //旧版本添加文本框 被弃用
    private View addTextEditBox(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_textedit_box,null);
        view.setLayoutParams(layoutParams);
        return view;
    }

    //创建文本框 旧版本
    private void createTextEditBox(){
        final TextEditBox mTextEditBox = new TextEditBox(this,null);
        mTextList.add(mTextEditBox);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mTextEditBox.setLayoutParams(layoutParams);
        content_layout.addView(mTextEditBox);

       mTextEditBox.setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
           @Override
           public void onTextEditClick(TextEditBox textEditBox) {
               //showInputDialog(mTextEditBox);
           }
       });
    }

    //从底部弹出给文本框输入字体的方法
    private void showInputDialog(final TextObject textObject){
        dialog = new TextInputDialog(this);
        //获取dialog中的文字
        mInputText = dialog.getEditInput();
        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString().trim();
                textObject.setText(text);
                textObject.commit();
                //给一个特殊标识 防止TextWatcher监听不到
                String flag = text + "$";
                //避免出现text为空格
                if(!isEquals(flag,"$")){
                    operateView.setOnClickListener(new OperateView.MyListener() {
                        @Override
                        public void onClick(TextObject textObject) {
                            mInputText.setText(textObject.getText());
                            //textObject.setText(mInputText.getText().toString());
                            //textObject.commit();
                        }
                    });
                }else {
                    operateView.setOnClickListener(new OperateView.MyListener() {
                        @Override
                        public void onClick(TextObject textObject) {
                            textObject.setText("点击可编辑文字");
                        }
                    });
                }
            }
        });

        //如果用户未输入任何字符 则TextWatch监听不到 防止点击无反应
        if(textObject.getText().equals(getResources().getString(R.string.input_hint))){
            operateView.setOnClickListener(new OperateView.MyListener() {
                @Override
                public void onClick(TextObject textObject) {
                    //点中编辑框
                    showInputDialog(textObject);
                }
            });
        }
        dialog.setOnKeyListener(keyListener);//对系统键做监听
        dialog.show();

        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
             WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mInputText.getText())){
                    ToastUtils.showToast("输入不能为空");
                }else {
                    dialog.dismiss();
                }
            }
        });
    }

    //判断多个字符串是否相等
    public static boolean isEquals(String... args){
        String last = null;
        for(int i=0;i<args.length;i++){
            String str = args[i];
            if(TextUtils.isEmpty(str))
                return false;
            if(last!=null && !str.equalsIgnoreCase(last))
                return false;
            last = str;
        }
        return true;
    }

    DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                dialog.dismiss();
                return false;
            }
            else if(keyCode == KeyEvent.KEYCODE_DEL){//删除键
                return false;
            }else {
                return true;
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            refreshItemIcon();
            int i = item.getItemId();
            if (i == R.id.bottom_sticker) {/*选中时加载选中的代码*/
                item.setIcon(R.drawable.sticker_copy);
            } else if (i == R.id.bottom_text) {
                item.setIcon(R.drawable.text_copy);
                addText();
            } else if (i == R.id.bottom_brush) {
                item.setIcon(R.drawable.painter_copy);
                mColorDialg = new ColorPickerDialog(EditActivity.this);
                mColorDialg.show();
                //createDoodle();
            } else if (i == R.id.bottom_eraser) {
                item.setIcon(R.drawable.earser_copy);
                if (mDoodle!=null) {
                    mDoodle.clean();
                }
            } else if (i == R.id.bottom_pic) {
                item.setIcon(R.drawable.pic_copy);
            }
            return false;
        }
    };

    /*未选中时加载默认的图片*/
    public void refreshItemIcon(){
        MenuItem item1 = mBottomNavigation.getMenu().findItem(R.id.bottom_sticker);
        item1.setIcon(R.drawable.sticker);
        MenuItem item2 = mBottomNavigation.getMenu().findItem(R.id.bottom_text);
        item2.setIcon(R.drawable.text);
        MenuItem item3 = mBottomNavigation.getMenu().findItem(R.id.bottom_brush);
        item3.setIcon(R.drawable.painter);
        MenuItem item4 = mBottomNavigation.getMenu().findItem(R.id.bottom_eraser);
        item4.setIcon(R.drawable.eraser);
        MenuItem item5 = mBottomNavigation.getMenu().findItem(R.id.bottom_pic);
        item5.setIcon(R.drawable.pic);
    }

    //    private void loadingImage(Activity activity){
//        byte[] byteTemp = activity.getIntent().getByteArrayExtra("bitmap");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteTemp,0,byteTemp.length);
//        images.setImageBitmap(bitmap);
//    }

    private void createDoodle(){
        mDoodle = new Doodle(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        mDoodle.setLayoutParams(layoutParams);
        mRContainer.addView(mDoodle);
    }

    private void createEraser(){
        mEraser = new Eraser(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        mEraser.setLayoutParams(layoutParams);
        mRContainer.addView(mEraser);
    }

    private void showDialog(){
        mQuitMakeDialog = new QuitMakeDialog(EditActivity.this);
        mQuitMakeDialog.setClickListenerInterface(new QuitMakeDialog.OnClickListener() {
            @Override
            public void doConfirm() {
                EditActivity.this.finish();
            }

            @Override
            public void doCancel() {
                mQuitMakeDialog.dismiss();
            }
        });
        mQuitMakeDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)){
            showDialog();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
