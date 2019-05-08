package com.example.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;

import com.example.common.bean.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    Context context;
    SharedPreferences sharedPreferences;
    User.DataBean user;
    private String name = "name", email = "email", token = "token", icon = "icon", id = "id";
    public UserManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        this.user = new User.DataBean();
    }
    public User.DataBean getUser(){
        String  userName = sharedPreferences.getString(name,null);
        String  userEmail = sharedPreferences.getString(email,null);
        String userToken = sharedPreferences.getString(token,null);
        String userIcon = sharedPreferences.getString(icon,null);
        int id = sharedPreferences.getInt("id", 0);
        if (id == 0){
            return null;
        }
        user.setEmail(userEmail);
        user.setName(userName);
        user.setToken(userToken);
        user.setId(id);
        user.setIcon(userIcon);
        return user;
    }

    /**
     * 用户信息存储
     * @param user 传入用户模型User.DataBean
     * @return 返回存储是否成功
     */
    public Boolean saveUser(User.DataBean user){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(name, user.getName());
        edit.putString(email, user.getEmail());
        edit.putString(token, user.getToken());
        edit.putString(icon,user.getIcon());
        edit.putInt(id, user.getId());
        return edit.commit();
    }

    /**
     * 退出登录
     */
    public void logout(){
        sharedPreferences.edit().clear().apply();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            List<String> list = new ArrayList<>();
            list.add("person");
            list.add("addIcon");
            ShortcutManager shortcutsManager = context.getSystemService(ShortcutManager.class);
            shortcutsManager.removeDynamicShortcuts(list);
        }
    }
}
