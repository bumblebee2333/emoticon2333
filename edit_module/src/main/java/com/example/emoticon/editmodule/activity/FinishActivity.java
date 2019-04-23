package com.example.emoticon.editmodule.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.emoticon.editmodule.R;
import com.example.emoticon.editmodule.widget.EditToolbar;

public class FinishActivity extends Activity {
    private EditToolbar mEditToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_save);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
    }

    private void init(){
        mEditToolbar = new EditToolbar(this);
        mEditToolbar = findViewById(R.id.edit_toolbar);
        mEditToolbar.setTextViewText("完成");
    }
}
