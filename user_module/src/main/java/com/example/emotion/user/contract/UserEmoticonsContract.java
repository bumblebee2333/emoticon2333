package com.example.emotion.user.contract;

import androidx.annotation.NonNull;

import com.example.common.base.BasePresenter;
import com.example.common.base.BaseView;
import com.example.common.bean.Emoticon;

import java.util.List;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/18.
 * PS:
 */
public interface UserEmoticonsContract {
    interface EmoticonView extends BaseView<UserEmoticonsContract.Presenter> {
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
    }

    interface Presenter extends BasePresenter<Emoticon> {
        /**
         * 获取数据
         *
         * @param userId 用户ID
         * @param skip   跳过几个数据
         */
        void loadData(int userId, int skip);
    }
}
