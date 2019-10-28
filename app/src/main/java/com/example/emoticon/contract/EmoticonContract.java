package com.example.emoticon.contract;

import androidx.annotation.NonNull;

import com.example.common.base.BasePresenter;
import com.example.common.base.BaseView;
import com.example.common.bean.Emoticon;
import com.example.emotion.user.contract.UserEmoticonsContract;

import java.util.List;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/27.
 * PS:
 */
public interface EmoticonContract {
    interface DataView extends BaseView<UserEmoticonsContract.Presenter> {
        /**
         * 获取失败回调
         *
         * @param e 错误内容
         */
        void onError(@NonNull Exception e);

        /**
         * 获取成功回调
         *
         * @param emoticonList 获取的内容
         */
        void onFinish(@NonNull List<Emoticon> emoticonList);

        /**
         * 获取成功回调
         *
         * @param emoticon 获取的内容
         */
        void onFinish(@NonNull Emoticon emoticon);
    }

    interface Presenter extends BasePresenter {
        void loadDataByTypeId(int id, int skip);

        void loadDataByKeyWords(String word, int skip);

        void loadDataById(int id);
    }
}
