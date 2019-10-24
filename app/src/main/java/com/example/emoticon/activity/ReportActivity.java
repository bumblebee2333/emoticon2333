package com.example.emoticon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.common.RetroClient;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.ReportProtocol;
import com.example.common.utils.ToastUtils;
import com.example.common.widget.Toolbar;
import com.example.emoticon.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/5.
 * PS:
 */
public class ReportActivity extends BaseActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initViews();
        initData();
    }

    private void initViews() {
        imageView = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.edit_text);
        toolbar.setRightButtonOneShow(true);
        toolbar.right1.setImageResource(R.drawable.right_ok);
    }

    private void initData() {
        Intent intent = getIntent();
        final Emoticon emoticon = (Emoticon) intent.getSerializableExtra("emoticon");
        final ReportProtocol.TYPE reportType = (ReportProtocol.TYPE) intent.getSerializableExtra("type");
        if (emoticon == null) return;
        Glide.with(this).load(emoticon.getImgUrl()).into(imageView);

        toolbar.right1.setOnClickListener(v -> {
            if (editText.getText().toString().isEmpty()) {
                ToastUtils.showToast("内容不能为空");
                return;
            }
            ReportProtocol reportProtocol = RetroClient.getServices(ReportProtocol.class);
            Call<StatusResult> call = reportProtocol.reportSubmit(emoticon.getId(), reportType, editText.getText().toString());
            call.enqueue(new Callback<StatusResult>() {
                @Override
                public void onResponse(@NonNull Call<StatusResult> call, @NonNull Response<StatusResult> response) {
                    if (response.body() != null) {
                        ToastUtils.showToast(response.body().getMsg());
                        if (response.body().isSuccess()) {
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StatusResult> call, @NonNull Throwable t) {
                    ToastUtils.showToast(t.getMessage());
                }
            });
        });
    }

    public static void startActivity(Context context, Emoticon emoticon, ReportProtocol.TYPE type) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra("emoticon", emoticon);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
