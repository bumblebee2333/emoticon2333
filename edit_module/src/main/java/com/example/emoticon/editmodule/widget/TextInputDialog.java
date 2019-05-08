package com.example.emoticon.editmodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.emoticon.editmodule.R;
public class TextInputDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private EditText et_input;
    private TextView confirm;
    private OnModifyTextClick mListener;
    //修改文字的接口
    public interface OnModifyTextClick{
        void onModifyClick(View v);
    }

    public TextInputDialog(@NonNull Context context) {
        super(context,R.style.dialog_text);
        this.context = context;
        View view = getLayoutInflater().inflate(R.layout.dialog_input_text,null);
        et_input = view.findViewById(R.id.dialog_text);
        confirm = view.findViewById(R.id.confirm);
        super.setContentView(view);
    }

    public TextInputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected TextInputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //点击Dialog外部 不消失
        setCanceledOnTouchOutside(false);
        //返回键 设置dialog物理退出
        setCancelable(true);
        getWindow().setGravity(Gravity.BOTTOM);



        WindowManager wm = getWindow().getWindowManager();
        //Display display = wm.getDefaultDisplay();
        //diaplay类 提供关于屏幕尺寸和分辨率的信息
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    public void setOnModifyTextClickListener(OnModifyTextClick mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onModifyClick(v);
        }
    }

    public EditText getEditInput(){
        return et_input;
    }

    public TextView getTextView(){
        return confirm;
    }
}
