package com.example.emoticon.editmodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.emoticon.editmodule.R;

public class ColorPickerDialog extends Dialog {
    public Context context;

    public ColorPickerDialog(Context context) {
        super(context,R.style.dialog_colorpicker);
        this.context = context;
        View view = View.inflate(context,R.layout.view_colorbutton,null);
        super.setContentView(view);
    }

    public ColorPickerDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public ColorPickerDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.x = 0;
        lp.y = 174;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 120;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
}
