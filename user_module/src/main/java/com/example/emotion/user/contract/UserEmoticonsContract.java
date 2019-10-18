package com.example.emotion.user.contract;

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
    interface EmoticonView extends BaseView<UserEmoticonsContract.Presenter>{
        void onError(Exception e);
        void onFinish(List<Emoticon> emoticonList);
    }

    interface Presenter extends BasePresenter{
        void loadData(int userId, int skip);
    }
}
