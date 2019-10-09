package com.example.common.app;

public class Config {
    public static final String CALLBACK_ADDRESS = "http://oss-demo.aliyuncs.com:23450";//回调链接
    public static final String END_POINT = "http://oss-cn-beijing.aliyuncs.com";//oss区域链接
//    public static final String BASE_HOST = "http://10.102.2.62:8080";//oss区域链接
    public static final String BASE_HOST = "http://192.168.1.158:8080";//oss区域链接
    public static final String STS_SERVER = BASE_HOST +"/sts";//oss Token获取链接（服务端）
    public static final String ROOT_URL = BASE_HOST ;//服务器地址
    public static final String BUCKET_NAME = "shuike";//oss Token获取链接（服务端）
    public static final String IMG_HOST = "https://shuike.oss-cn-beijing.aliyuncs.com/";//图片服务器
/*
     public static final String emoticonImgHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/images/";//表情
     public static final String userIconHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/usericon/";//用户头像
     public static final String materialHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/material/";//素材
     public static final String bannerHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/banner/";//Banner
*/

    public class OssFolder {
        public static final String EMOTICONS = "emoticon/images/";
        public static final String USERICON = "emoticon/usericon/";
        public static final String BANNER = "emoticon/banner/";
    }
}
