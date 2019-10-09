package com.example.emoticon;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.common.RetroClient;
import com.example.common.bean.StatusResult;
import com.example.common.bean.User;
import com.example.common.fragment.MAlertDialog;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.UserManager;
import com.example.emoticon.activity.EmoticonAddActivity;
import com.example.emoticon.activity.SearchActivity;
import com.example.emoticon.fragment.CreativeFragment;
import com.example.emoticon.fragment.EmoticonFragment;
import com.example.emoticon.fragment.PaintFragment;
import com.example.emoticon.fragment.PersonFragment;
import com.example.emotion.user.retrofit.UserProtocol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/3/24.
 * PS:
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.
        OnNavigationItemSelectedListener {
    private static final int PERMISSION_REQUEST_CODE = 1; //权限请求码
    private List<Fragment> fragments = new ArrayList<>();//fragment数组
    private int lastShowFragment;//表示最后一个显示的Fragment
    int lastIndex = 0;
    private long pressTime = 0;
    private boolean userHintIsShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//23表示5.0
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= 22) {
            getWindow().setStatusBarColor(getResources().getColor(com.example.common.R.color.colorBlack));
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        //顶部添加状态栏高度的padding
//        setTopPadding(view);
        //设置布局
        setContentView(R.layout.activity_main);
        /*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(Color.parseColor("#FFFF00"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
        View addView = findViewById(R.id.add_view);
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmoticonAddActivity.startActivity(MainActivity.this);
            }
        });
        addView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                SearchActivity.startActivity(MainActivity.this,SearchActivity.EMOTICON);
                return false;
            }
        });
        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        //底部导航栏的点击事件
        navigation.setOnNavigationItemSelectedListener(this);
        initPermission();
        initFragments();//初始化Fragment数组
        setFragmentPosition(0);//显示第一个Fragment
    }

    private void setTopPadding(View view) {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            view.setPadding(0, result, 0, 0);
        }
    }

    //权限
    private void initPermission() {
        storagePermission();
    }

    public void initFragments() {
        fragments.add(EmoticonFragment.newInstance("表情包"));
        fragments.add(CreativeFragment.newInstance("创意工坊"));
        fragments.add(PaintFragment.newInstance("涂鸦"));
        fragments.add(PersonFragment.newInstance("我的"));
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(position);
        Fragment lastFragment = fragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.main_container, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    //底部导航栏点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_emoticon:
                setFragmentPosition(0);
//                findViewById(R.id.bg).setFitsSystemWindows(true);
                return true;
            case R.id.navigation_diy:
//                findViewById(R.id.bg).setFitsSystemWindows(true);
                setFragmentPosition(1);
                return true;
            case R.id.navigation_paint:
//                findViewById(R.id.bg).setFitsSystemWindows(true);
                setFragmentPosition(2);
                return true;
            case R.id.navigation_my:
//                findViewById(R.id.bg).setFitsSystemWindows(false);
                setFragmentPosition(3);
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        final User user = UserManager.getUser();
        if (user != null) {
            UserProtocol userProtocol = RetroClient.getServices(UserProtocol.class);
            Call<StatusResult<User>> userInfo = userProtocol.getUserInfo(user.getId(), user.getToken());
            HttpUtils.doRequest(userInfo, new HttpUtils.RequestFinishCallback<User>() {
                @Override
                public void getRequest(StatusResult<User> result) {
//                    System.out.println(new Gson().toJson(result));
                    if (result == null) return;
                    if (result.isSuccess()) return;
                    if(result.getData()!=null){
                        if (userHintIsShow) return;
                        String device = result.getData().getDevice();
                        String time = result.getData().getUpdateTime();
                        MAlertDialog mAlertDialog = MAlertDialog.newInstance();
                        mAlertDialog.setMessage("你的账号于("+time+")在另一台设备("+device+")登录。如非本人操作，则密码可能已泄露，请尽快修改密码");
                        mAlertDialog.setCancelable(false);
                        mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserManager.logout();
                                userHintIsShow = false;
                            }
                        });
                        mAlertDialog.show(getSupportFragmentManager(), "logoutHint");
                        userHintIsShow = true;
                    }
                }
            });
/*            userInfo.enqueue(new Callback<StatusResult<User>>() {
                @Override
                public void onResponse(@NonNull Call<StatusResult<User>> call, @NonNull Response<StatusResult<User>> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
//                            Toast.makeText(MainActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (userHintIsShow) return;
                            String device = response.body().getData().getDevice();
                            String time = response.body().getData().getUpdateTime();
                            MAlertDialog mAlertDialog = MAlertDialog.newInstance();
                            mAlertDialog.setMessage("你的账号于("+time+")在另一台设备("+device+")登录。如非本人操作，则密码可能已泄露，请尽快修改密码");
                            mAlertDialog.setCancelable(false);
//                            mAlertDialog.setNegativeButton("取消", null);
                            mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserManager.logout();
                                    userHintIsShow = false;
                                }
                            });
                            mAlertDialog.show(getSupportFragmentManager(), "logoutHint");
                            userHintIsShow = true;
                            //Toast.makeText(MainActivity.this, "该账户已在其他设备登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResult<User>> call, Throwable t) {

                }
            });*/
        }
        super.onResume();
    }

    //双击返回键退出
    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if ((time - pressTime) > 2000) {
            ToastUtils.showToast("再按一次退出程序");
            pressTime = time;
        } else {
            finish();
            System.exit(0);
        }
    }

    //Fragment重叠
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }


    private void storagePermission() {
        if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //获取权限后的操作。读取文件
        } else {
//            //请求权限
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST_CODE);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ToastUtils.showToast("需要开启存储权限");
                showRequestPermissionDialog(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void showRequestPermissionDialog(final String[] permissions, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("使用表情包存储及发布需要使用SD卡权限\n是否再次开启权限");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
            }
        });
        builder.setNegativeButton("否", null);
        builder.setCancelable(true);
        builder.show();
    }

    /**
     * 检测权限是否授权
     *
     * @return
     */
    private boolean checkPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //得到了授权
                    ToastUtils.showToast("授权成功");
                } else {
                    //未授权
                    ToastUtils.showToast("授权失败");
                }
                break;
            default:
                break;
        }
    }
}

