package com.example.emoticon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.emoticon.model.User;

public class UserManager {
    Context context;
    SharedPreferences sharedPreferences;
    User.DataBean user;
    public UserManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        this.user = new User.DataBean();
    }
    public User.DataBean getUser(){
        String  name = sharedPreferences.getString("name","");
        String  email = sharedPreferences.getString("email","");
        String token = sharedPreferences.getString("token","");
        String icon = sharedPreferences.getString("icon","");
        int id = sharedPreferences.getInt("id", 0);
        if (id == 0){
            return null;
        }
        user.setEmail(email);
        user.setName(name);
        user.setToken(token);
        user.setId(id);
        user.setIcon(icon);
        return user;
    }
    public Boolean saveUser(User.DataBean user){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("name", user.getName());
        edit.putString("email", user.getEmail());
        edit.putString("token", user.getToken());
        edit.putString("icon",user.getIcon());
        edit.putInt("id", user.getId());
        return edit.commit();
    }

    public Boolean logout(){
        return sharedPreferences.edit().clear().commit();
    }
}
