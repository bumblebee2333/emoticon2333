package com.example.emoticon.app;

public class Config {
    public static final String callbackAddress = "http://oss-demo.aliyuncs.com:23450";//回调链接
    public static final String endpoint = "http://oss-cn-beijing.aliyuncs.com";//oss区域链接
    public static final String stsServer = "http://140.143.154.18:7080/sts.php";//oss Token获取链接（服务端）
    public static final String bucketName = "shuike";//oss Token获取链接（服务端）
    public static final String IMGHOST = "https://shuike.oss-cn-beijing.aliyuncs.com/";//图片服务器
/*
     public static final String emoticonImgHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/images/";//表情
     public static final String userIconHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/usericon/";//用户头像
     public static final String materialHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/material/";//素材
     public static final String bannerHost = "https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/banner/";//Banner
*/

    public class OssFolder {
        public static final String EMOTICON = "emoticon/images/";
        public static final String USERICON = "emoticon/usericon/";
        public static final String BANNER = "emoticon/banner/";
    }
}
