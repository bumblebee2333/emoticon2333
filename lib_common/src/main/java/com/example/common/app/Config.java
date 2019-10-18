package com.example.common.app;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import it.sauronsoftware.base64.Base64;

public class Config {
    public static final String CALLBACK_ADDRESS = "http://oss-demo.aliyuncs.com:23450";//回调链接
    public static final String END_POINT = "http://oss-cn-beijing.aliyuncs.com";//oss区域链接
    public static final String BASE_HOST = "http://47.97.204.14:8080";//oss区域链接
//    public static final String BASE_HOST = "http://192.168.1.158:8080";//oss区域链接
    public static final String STS_SERVER = BASE_HOST +"/sts";//oss Token获取链接（服务端）
    public static final String ROOT_URL = BASE_HOST ;//服务器地址
    public static final String BUCKET_NAME = "shuike";//oss Token获取链接（服务端）
    public static final String IMG_HOST = "https://shuike.oss-cn-beijing.aliyuncs.com/";//图片服务器

    //公钥
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkDtr9lR2GLoGeJWLmXHbf1HSEvOzH+Wgz7+Dc9nTEKNXnSq1g8c5UmG4YbXwUtuFwJMgR06GWkMCX3yqXBeXKM/wibQLRr/5UGVNJgUo8jpajwScpuzqb1QLohluyhRd0TKp7JJDqpOIWsIKrc6c4qx/MeDZYJg2cXwH829T7HQIDAQAB";

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


    /**
     * 使用公钥加密
     * @param p 要加密内容
     * @return
     */
    public static String getSessionKey(String p) {
        String sessionKey = "";
        try {
            //公钥加密、私钥解密 ---- 加密
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(it.sauronsoftware.base64.Base64.decode(PUBLIC_KEY.getBytes()));
            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
            PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] result2 = cipher2.doFinal(p.getBytes());
            sessionKey = new String(Base64.encode(result2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionKey;
    }
}
