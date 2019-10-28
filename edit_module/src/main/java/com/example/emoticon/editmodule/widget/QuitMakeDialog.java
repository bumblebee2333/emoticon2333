package com.example.emoticon.editmodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.emoticon.editmodule.R;

public class QuitMakeDialog extends Dialog {
    private TextView abandon;//放弃按钮
    private TextView make;//继续制作按钮
    private OnClickListener onClickListener;
    private Context mContext;

    public interface OnClickListener{
        void doConfirm();
        void doCancel();
    }

    public QuitMakeDialog(Context context){
        super(context,R.style.dialog_cancle);
        mContext = context;
        View view = View.inflate(context,R.layout.dialog_edit_back,null);
        abandon = view.findViewById(R.id.abandon);
        make = view.findViewById(R.id.make);
        super.setContentView(view);
    }

    public QuitMakeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public QuitMakeDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener onCancelListener){
        super(context,cancelable,onCancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);

        initEvent();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float density = dm.density;//屏幕密度（0.75/1.0/1.5）
        int desityDpi = dm.densityDpi;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(width * 0.9);
        lp.height = 600;
        getWindow().setAttributes(lp);
    }

    public void setClickListenerInterface(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void initEvent(){
        abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.doConfirm();
                }
            }
        });

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.doCancel();
                }
            }
        });
    }
}
