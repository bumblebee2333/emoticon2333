package com.example.emoticon.retrofit;

import com.example.emoticon.model.Upload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadProtocol {
    /**
     * 上传图片
     * 现在弃用
     */
    @Multipart
    @POST("/upload")
    Call<Upload> uploadMemberIcon(@Part MultipartBody.Part part);
}
