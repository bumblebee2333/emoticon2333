package com.example.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.common.R;
import com.example.common.RetroClient;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.bean.User;
import com.example.common.manager.UserManager;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.retrofit.ReportProtocol;
import com.example.common.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/7/23.
 * PS:
 */
public class BottomMenuFragmentDialog extends DialogFragment {
    public static BottomMenuFragmentDialog newInstance(Emoticon dataBean) {
        Bundle args = new Bundle();
        args.putSerializable("emoticon", dataBean);
        BottomMenuFragmentDialog fragment = new BottomMenuFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackListener {
        void onDelete(boolean value);

        void onReport(boolean value);
    }

    private CallbackListener callbackListener;
    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mContext == null) mContext = getContext();
        View rootView = inflater.inflate(R.layout.bottom_menu_fragment_dialog, container, false);
        initViews(rootView);
        slideToUp(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        LinearLayout horizontalScrollView1 = rootView.findViewById(R.id.horizontalScrollView1);
        LinearLayout horizontalScrollView2 = rootView.findViewById(R.id.horizontalScrollView2);

        horizontalScrollView1.addView(setItemView("QQ", R.drawable.qq_white, "#63B8FF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            }
        }));
        horizontalScrollView1.addView(setItemView("微信", R.drawable.wechat, "#62b900", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            }
        }));


        horizontalScrollView2.addView(setItemView("保存本地", R.drawable.download_line, "#E0E0E0", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "123", Toast.LENGTH_SHORT).show();
            }
        }));

        if (!getTag().equals("user")) {
            horizontalScrollView2.addView(setItemView("举报", R.drawable.report, "#E0E0E0", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getArguments() == null) return;
                    Intent intent = new Intent();
                    intent.setClassName(mContext, "com.example.emoticon.activity.ReportActivity");
                    intent.putExtra("emoticon", getArguments().getSerializable("emoticon"));
                    intent.putExtra("type", ReportProtocol.TYPE.EMOTICON);
                    mContext.startActivity(intent);
                }
            }));
        } else {
            horizontalScrollView2.addView(setItemView("删除", R.drawable.delete_line, "#E0E0E0", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MAlertDialog mAlertDialog = MAlertDialog.newInstance();
                            mAlertDialog.setMessage("确认删除该表情？");
                            mAlertDialog.setCancelable(true);
                            mAlertDialog.setNegativeButton("取消", null);
                            mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    emoticonDelete(mContext);
                                }
                            });
                            AppCompatActivity activity = (AppCompatActivity) mContext;
                            mAlertDialog.show(activity.getSupportFragmentManager(), "delEmocticon");
                        }
                    });
                }
            }));
        }

        TextView close = rootView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void emoticonDelete(final Context context) {
        if (getArguments() == null) return;
        User user = UserManager.getUser();
        if (user == null) return;
        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        Emoticon emoticon = (Emoticon) getArguments().getSerializable("emoticon");
        if (emoticon == null) return;
        Call<StatusResult> call = emoticonProtocol.deleteEmoticon(user.getToken(), emoticon.getId());
/*        HttpUtils.doRequest(call, new HttpUtils.RequestFinishCallback() {
            @Override
            public void getRequest(StatusResult result) {
                if (result == null) return;
                if (!result.isSuccess()) {
                    String str = ResourcesManager.getRes().getString(R.string.request_error, result.getMsg());
                    ToastUtils.showToast(str);
                    return;
                }
                ToastUtils.showToast(result.getMsg());
                if (callbackListener != null) callbackListener.onDelete(true);
            }
        });*/
        call.enqueue(new Callback<StatusResult>() {
            @Override
            public void onResponse(Call<StatusResult> call, Response<StatusResult> response) {
                if (response.body() != null) {
                    ToastUtils.showToast(response.body().getMsg());
                    if (response.body().isSuccess()) {
                        if (callbackListener != null) callbackListener.onDelete(true);
                    }
                }
                dismiss();
            }

            @Override
            public void onFailure(Call<StatusResult> call, Throwable t) {
                ToastUtils.showToast(t.getMessage());
                dismiss();
            }
        });
    }

    private View setItemView(String title, int icon, String color, final View.OnClickListener onClickListener) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_menu_item, null);
        LinearLayout iconBg = v.findViewById(R.id.icon_bg);
        ImageView imageView = v.findViewById(R.id.icon);
        TextView textView = v.findViewById(R.id.title);
        textView.setText(title);
        imageView.setImageResource(icon);
        iconBg.setBackground(getIconDrawable(Color.parseColor(color), 100));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClickListener.onClick(v);
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
//        Toast.makeText(getActivity(), getTag(), Toast.LENGTH_SHORT).show();
    }


    public GradientDrawable getIconDrawable(int solidColor, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private void slideToUp(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(200);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
