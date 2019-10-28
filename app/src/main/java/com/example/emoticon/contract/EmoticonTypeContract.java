package com.example.emoticon.contract;

import androidx.annotation.NonNull;

import com.example.common.base.BasePresenter;
import com.example.common.base.BaseView;
import com.example.common.bean.EmoticonType;
import com.example.emotion.user.contract.UserEmoticonsContract;

import java.util.List;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/27.
 * PS:
 */
public interface EmoticonTypeContract {
    interface View extends BaseView<UserEmoticonsContract.Presenter> {
        /**
         * 获取失败回调
         *
         * @param e 错误内容
         */
        void onError(@NonNull Exception e);

        /**
         * 获取成功回调
         *
         * @param emoticonTypeList 获取的内容
         */
        void onFinish(@NonNull List<EmoticonType> emoticonTypeList);
    }

    interface Presenter extends BasePresenter{

    }
}
