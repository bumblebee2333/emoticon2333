package com.example.emoticon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.common.RetroClient;
import com.example.common.bean.Emoticon;
import com.example.common.bean.EmoticonType;
import com.example.common.retrofit.EmoticonTypeProtocol;
import com.example.common.utils.ToastUtils;
import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.emoticon.adapter.MainPageAdapter;
import com.example.emoticon.editmodule.activity.EditActivity;
import com.example.emoticon.utils.ScreenUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaintFragment extends Fragment implements View.OnClickListener {
    private List<Emoticon> list = new ArrayList<>();
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    private Uri uri;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainPageAdapter adapter;

    public static PaintFragment newInstance(String title){
        PaintFragment fragment = new PaintFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paint_fragment,container,false);

        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager_paint);
        //mViewPager.setOffscreenPageLimit(4);//设置页面缓存的个数
        adapter = new MainPageAdapter(getChildFragmentManager());

        getType();
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setRightButtonOneShow(true);
        toolbar.right1.setImageResource(R.drawable.cam);
        toolbar.right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopuWindow();
            }
        });
        toolbar.setTitle("涂鸦");

        return view;
    }

    private void getType() {


        EmoticonTypeProtocol emoticonProtocol = RetroClient.getServices(EmoticonTypeProtocol.class);
        final Call<EmoticonType.EmoticonTypeList> emoticonCall = emoticonProtocol.getEmoticonTypeList(30, 0);
        emoticonCall.enqueue(new Callback<EmoticonType.EmoticonTypeList>() {
            @Override
            public void onResponse(@NonNull Call<EmoticonType.EmoticonTypeList> call, @NonNull Response<EmoticonType.EmoticonTypeList> response) {
                list.clear();
                for (EmoticonType.DataBean dataBean : response.body().getDataList()) {
                    adapter.addFragment(DetailsFragment.newInstance(dataBean.getTitle(),dataBean.getId()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<EmoticonType.EmoticonTypeList> call, Throwable t) {
                ToastUtils.showToast(t.getMessage());
//                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showPopuWindow(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_populsindow,null);
        PopupWindow mPopuWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT, true);
        mPopuWindow.setContentView(contentView);
        backgroundAlpha(0.3f);

        //设置各个空间的点击响应事件
        TextView tv1 = contentView.findViewById(R.id.shooting);
        TextView tv2= contentView.findViewById(R.id.album);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        //显示PopuWindow
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.paint_fragment,null);
        mPopuWindow.showAtLocation(rootView,Gravity.CENTER,0,0);

        //popuwindow返回背景不透明
        mPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(0x1.0p0f);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.shooting:
                openCamera();
            break;
            case R.id.album:{
                openLocalAblum();
                Intent intent = new Intent(getActivity(),EditActivity.class);
                intent.putExtra("imageUri",getUri(uri.toString()));
                startActivity(intent);
            }
            break;
        }
    }

    //设置背景半透明
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;//0.0~1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /* 从此相册获取 */
    private void openLocalAblum(){
        //激活系统图库
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        //开启一个带返回值的Activity 请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
    }
    //从相机获取
    public void openCamera(){
        String state = Environment.getExternalStorageState();//获取内存卡可用状态
        if(state.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent,PHOTO_REQUEST_CAREMA);
        }
        else {
            ToastUtils.showToast("内存不可用");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //获取图片路径
        if(requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null){
            uri = data.getData();
            getUri(uri.toString());
//            String[] filePathColumns = {MediaStore.Images.Media.DATA};
//            Cursor c = getContext().getContentResolver().query(selectedImage,filePathColumns,
//                    null,null,null);
//            c.moveToFirst();
//            int columnIndex = c.getColumnIndex(filePathColumns[0]);
//            String imagePath = c.getString(columnIndex);
//            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            showImage(imagePath);
//            c.close();
        }
    }
    //加载图片
    private void showImage(String imagePath){
        Bitmap bitmap;
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        //这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
//        try {
//            InputStream in = getContext().getContentResolver().openInputStream(Uri.parse(imagePath));
//            BitmapFactory.decodeStream(in,null,opt);
//        }catch (FileNotFoundException e){
//            Toast.makeText(getActivity(),"文件未找到！",Toast.LENGTH_SHORT);
//        }
        BitmapFactory.decodeFile(imagePath,opt);
        //图片内存太大会溢出
        //获取这个图像的原始宽度和高度
        final int picWidth = opt.outWidth;
        final int picHeight = opt.outHeight;
        //获取屏幕的宽度和高度
        //Display display = getWindowManager().getDefaultDisplay();
        //getWindowManager无法被识别是因为他只能在Activity中被使用 所以新建一个获取屏幕宽和高的工具类
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int screeHeight = ScreenUtils.getScreenHeight(getActivity());
        //isSampleSize是表示对图片的缩放成度 比如值为2图片的宽度和高度都变为原来的1/2
        opt.inSampleSize = 2;
        //根据屏的大小和图片大小计算出缩放比例
        if(picWidth > picHeight){
            if(picWidth > screenWidth){
                opt.inSampleSize = picWidth/screenWidth;
            }
        }else {
            if(picHeight>screeHeight)
                opt.inSampleSize = picHeight/screeHeight;
        }
        //这次在生成一个有像素的 经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath);

        if(bitmap !=null){
            //向下个Activity传递数据
            Intent intent = new Intent(getActivity(),EditActivity.class);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
            byte[] bitmapByte = bos.toByteArray();
            intent.putExtra("bitmap",bitmapByte);
            startActivity(intent);
        }else {
            ToastUtils.showToast("图片加载失败！！！");
        }
    }

    public String getUri(String Uri){
        return Uri;
    }


}
