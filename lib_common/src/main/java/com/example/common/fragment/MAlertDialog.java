package com.example.common.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.common.R;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/7/25.
 * PS:
 */
public class MAlertDialog extends DialogFragment {
    private String message = "";
    private String negativeButtonText;
    private View.OnClickListener negativeButtonListener;
    private String positiveButtonText;
    private View.OnClickListener positiveButtonListener;

    public static MAlertDialog newInstance() {
        Bundle args = new Bundle();
        MAlertDialog fragment = new MAlertDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.m_alert_dialog, container, false);
        TextView content = rootView.findViewById(R.id.content);
        Button bt1 = rootView.findViewById(R.id.bt1);
        Button bt2 = rootView.findViewById(R.id.bt2);
        if (negativeButtonText == null || negativeButtonText.isEmpty()) {
            bt1.setVisibility(View.GONE);
        } else {
            bt1.setText(negativeButtonText);
        }
        if (positiveButtonText == null || positiveButtonText.isEmpty()) {
            bt2.setVisibility(View.GONE);
        } else {
            bt2.setText(positiveButtonText);
        }
        if (negativeButtonListener != null) {
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeButtonListener.onClick(v);
                    dismiss();
                }
            });
        } else {
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (positiveButtonListener != null) {
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveButtonListener.onClick(v);
                    dismiss();
                }
            });
        } else {
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        content.setText(message);
        return rootView;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setNegativeButton(CharSequence text, View.OnClickListener onClickListener) {
        this.negativeButtonText = text.toString();
        this.negativeButtonListener = onClickListener;
    }

    public void setPositiveButton(CharSequence text, View.OnClickListener onClickListener) {
        this.positiveButtonText = text.toString();
        this.positiveButtonListener = onClickListener;
    }
}
