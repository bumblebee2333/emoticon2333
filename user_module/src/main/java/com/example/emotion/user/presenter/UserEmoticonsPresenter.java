package com.example.emotion.user.presenter;

import com.example.common.RetroClient;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.utils.HttpUtils;
import com.example.emotion.user.contract.UserEmoticonsContract;
import com.example.emotion.user.retrofit.UserProtocol;

import java.util.List;

import retrofit2.Call;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/18.
 * PS:
 */
public class UserEmoticonsPresenter implements UserEmoticonsContract.Presenter {
    private UserEmoticonsContract.EmoticonView emoticonView;

    public UserEmoticonsPresenter(UserEmoticonsContract.EmoticonView emoticonView) {
        this.emoticonView = emoticonView;
        emoticonView.setPresenter(this);
    }

    /**
     * 获取用户表情数据
     * @param userId 用户ID
     * @param skip 跳过几个数据
     */
    @Override
    public void loadData(int userId, int skip) {
        UserProtocol userProtocol = RetroClient.getServices(UserProtocol.class);
        Call<StatusResult<List<Emoticon>>> emoticonCall = userProtocol.getEmoticonList(userId, 40, skip);
        HttpUtils.doRequest(emoticonCall, new HttpUtils.RequestFinishCallback<List<Emoticon>>() {
            @Override
            public void getRequest(StatusResult<List<Emoticon>> result) {
                if (result == null) return;
                if (!result.isSuccess()) {
                    emoticonView.onError(new Exception(result.getMsg()));
                    return;
                }
                if (result.getData() != null) {
                    List<Emoticon> data = result.getData();
                    emoticonView.onFinish(data);
                }
            }
        });
    }
}
