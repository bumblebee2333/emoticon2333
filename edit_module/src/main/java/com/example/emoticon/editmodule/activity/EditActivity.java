package com.example.emoticon.editmodule.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.base.BaseActivity;
import com.example.common.utils.ToastUtils;
import com.example.emoticon.editmodule.R;
import com.example.emoticon.editmodule.widget.TextEditBox;
import com.example.emoticon.editmodule.widget.TextInputDialog;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends BaseActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar();
        initView();
        loadingImage(this);
        //localImage(this);
        setToolBarListener();

        new TextEditBox(this).setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
            @Override
            public void onTextEditClick(TextEditBox textEditBox) {
                showInputDialog(mTextEditBox);
            }
        });
    }

    private void initView(){
        images = findViewById(R.id.photo);
        toolbar_edit = findViewById(R.id.toolbar_edit);
        mBottomNavigation = findViewById(R.id.bottomNavigation_paint);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null);//删除默认的选中效果
        mRContainer = findViewById(R.id.relative_layout_img);
        mInputText = findViewById(R.id.dialog_text);
        mTextEditBox = findViewById(R.id.text_edit_box);
        confirm = findViewById(R.id.confirm);
    }

    private void loadingImage(Activity activity){
        String img = activity.getIntent().getStringExtra("picture");
        Glide.with(this).load(img).into(images);
    }

    private void localImage(Activity activity){
        String imageUri = activity.getIntent().getStringExtra("imageUri");
        Uri uri = Uri.parse(imageUri);
        images.setImageURI(uri);
    }

   @SuppressLint("NewApi")
   public void setToolBarListener(){
        toolbar_edit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
   }

    private View addTextEditBox(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_textedit_box,null);
        view.setLayoutParams(layoutParams);
        return view;
    }

    private void createTextEditBox(){
        final TextEditBox mTextEditBox = new TextEditBox(this,null);
        mTextList.add(mTextEditBox);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mTextEditBox.setLayoutParams(layoutParams);
        mRContainer.addView(mTextEditBox);

       mTextEditBox.setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
           @Override
           public void onTextEditClick(TextEditBox textEditBox) {
               showInputDialog(mTextEditBox);
           }
       });

        //从底部弹出文本输入编辑器
        //showInputDialog(mTextEditBox);
    }
    private void showInputDialog(final TextEditBox mTextEditBox){
        dialog = new TextInputDialog(this);
        //获取dialog中的文字
        mInputText = dialog.getEditInput();
        //文本贴图
        mTextEditBox.setmEditText(mInputText);
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
//                if(TextUtils.isEmpty(text)){
//                    mInputText.setText(getResources().getText(R.string.input_hint));
//                }
                mTextEditBox.setText(text);
                //给一个特殊标识 防止TextWatcher监听不到
                String flag = text + "$";
                //避免出现text为空格
                if(!isEquals(flag,"$")){
                    mTextEditBox.setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
                        @Override
                        public void onTextEditClick(TextEditBox textEditBox) {
                            //点中编辑框
                            //showInputDialog(mTextEditBox);
                            mInputText.setText(mTextEditBox.getmText());
                        }
                    });
                }else {
                    mTextEditBox.setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
                        @Override
                        public void onTextEditClick(TextEditBox textEditBox) {
                            mInputText.setText(getResources().getText(R.string.input_hint));
                        }
                    });
                }
            }
        });

        //如果用户未输入任何字符 则TextWatch监听不到 防止点击无反应
        if(mTextEditBox.getmText().equals(getResources().getString(R.string.input_hint))){
            mTextEditBox.setOnEditClickListener(new TextEditBox.OnTextEditClickListener() {
                @Override
                public void onTextEditClick(TextEditBox textEditBox) {
                    //点中编辑框
                    showInputDialog(mTextEditBox);
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
                createTextEditBox();

            } else if (i == R.id.bottom_brush) {
                item.setIcon(R.drawable.painter_copy);

            } else if (i == R.id.bottom_eraser) {
                item.setIcon(R.drawable.earser_copy);

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
}
