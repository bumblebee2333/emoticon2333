package com.example.emoticon.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.cyflowlayoutlibrary.FlowLayout;
import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.example.common.RetroClient;
import com.example.common.app.Config;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.EmoticonType;
import com.example.common.bean.StatusResult;
import com.example.common.particlesmaster.ParticleSmasher;
import com.example.common.particlesmaster.SmashAnimator;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.retrofit.EmoticonTypeProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.UserManager;
import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAddImageAdapter;
import com.example.emoticon.adapter.LabelAdapter;
import com.example.emoticon.service.OssService;
import com.example.emoticon.utils.OSSHttpUtils;
import com.example.emotion.user.activity.LoginActivity;
import com.google.gson.Gson;
import com.skit.imagepicker.imageengine.GlideImageEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmoticonAddActivity extends BaseActivity implements View.OnClickListener {
    //RecyclerView recyclerView;
    TextView type;
    LabelAdapter labelAdapter;
    List<String> tagList = new ArrayList<>();
    List<String> imgList = new ArrayList<>();
    private FlowLayoutAdapter<String> flowLayoutAdapter;
    Toolbar toolbar;
    ImageView imageSelect;
    int typeId = 0;
    String type_title;
    ProgressDialog progressDialog;
    boolean addType = false;
    private FlowLayout flowLayout;
    private ParticleSmasher smasher;
    private RecyclerView recyclerView;
    EmoticonAddImageAdapter addImageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoticon_add);
        initViews();
    }

    private void initViews() {

        labelAdapter = new LabelAdapter(tagList);
        type = findViewById(R.id.type);
        toolbar = findViewById(R.id.toolbar);
        imageSelect = findViewById(R.id.image);
        flowLayout = findViewById(R.id.flowlayout);
        imageSelect.setOnClickListener(this);
        toolbar.right1.setVisibility(View.VISIBLE);
        toolbar.right1.setImageResource(R.drawable.right_ok);
        toolbar.right1.setOnClickListener(this);
        type.setOnClickListener(this);
        tagList.add("点击添加标签");
        flowLayoutAdapter = flowLayoutAdapter();
        flowLayout.setAdapter(flowLayoutAdapter);
        smasher = new ParticleSmasher(this);
        imgList.add(null);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        addImageAdapter = new EmoticonAddImageAdapter(imgList, gridLayoutManager);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(addImageAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        addImageAdapter.setOnDeleteOnClickListener(new EmoticonAddImageAdapter.OnDeleteOnClickListener() {
            @Override
            public void onClick(@NotNull View view, int p0) {
                imgList.remove(p0);
                if(imgList.size() == 8 && !TextUtils.isEmpty(imgList.get(0))){
                    imgList.add(0,null);
                }
                addImageAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * FlowLayoutAdapter
     * @return FlowLayoutAdapter
     */
    private FlowLayoutAdapter<String> flowLayoutAdapter() {
       return new FlowLayoutAdapter<String>(tagList) {
            @Override
            public void bindDataToView(ViewHolder viewHolder, final int i, String s) {
                viewHolder.setText(R.id.tv, "# "+s+" #");
                viewHolder.setOnLongClickListener(R.id.tv, flowLayoutOnLongClick(i));
                viewHolder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (i == 0){
                            addLabel();
                        }
                    }
                });
            }

            @Override
            public void onItemClick(int i, String s) {

            }

            @Override
            public int getItemLayoutID(int i, String s) {
                return R.layout.flowlayout_item;
            }

           private View.OnLongClickListener flowLayoutOnLongClick(final int i) {
               return new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       if (i != 0) {
                           smasher.with(v).setStyle(SmashAnimator.STYLE_DROP).addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                               @Override
                               public void onAnimatorEnd() {
                                   super.onAnimatorEnd();
                                   tagList.remove(i);
                                   flowLayoutAdapter.notifyDataSetChanged();
                               }
                           }).start();
                       }else {
                           ToastUtils.showToast(getString(R.string.topic_toast_hint));
                       }
                       return true;
                   }
               };
           }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    setType(data);
                    break;
                case 2:
//                    Uri selectImg = data.getData();
//                    String[] filepath = {MediaStore.Images.Media.DATA};
//                    Cursor c = getContentResolver().query(selectImg, filepath, null, null, null);
//                    c.moveToFirst();
//                    int cul = c.getColumnIndex(filepath[0]);
//                    String imgPath = c.getString(cul);
//                    this.imgPath = imgPath;
//                    Glide.with(this).load(imgPath).into(imageSelect);

                    List<String> mSelected = Matisse.obtainPathResult(data);
                    imgList.addAll(mSelected);
                    //添加按钮清除，条件为有十个数据并且第一个是空的
                    if(imgList.size() == 10 && TextUtils.isEmpty(imgList.get(0))){
                        imgList.remove(0);
                    }
//                    this.imgPath = mSelected.get(0);
//                    Glide.with(this).load(imgPath).into(imageSelect);
                    addImageAdapter.notifyDataSetChanged();

                    break;
            }
        }
    }

    //这是分类
    private void setType(Intent data) {
        type_title = data.getStringExtra("title");
        typeId = data.getIntExtra("id", 0);
        addType = data.getBooleanExtra("addType",false);
        type.setText(type_title);
        type.setTextColor(Color.WHITE);
        type.setBackgroundResource(R.drawable.typeselect_select);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                Intent intent = new Intent(EmoticonAddActivity.this, SearchActivity.class);
                intent.putExtra("type", SearchActivity.EMOTICON_TYPE);
                startActivityForResult(intent, 1);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.image:
                int maxSelectableCount = 9-imgList.size();
                if (maxSelectableCount == 0) {
                    ToastUtils.showToast("不能选更多图片");
                    return;
                }
                Matisse.from(EmoticonAddActivity.this)
                        .choose(MimeType.ofImage())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(maxSelectableCount)//可选的最大数
                        .capture(false)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "PhotoPicker"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideImageEngine())//图片加载引擎
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(2);
                break;
            case R.id.right1:
                if (UserManager.getUser() == null) {
                    ToastUtils.showToast("请登录后再提交");
                    return;
                }
                uploadToOss();
                break;
        }
    }

    //上传到Oss
    private void uploadToOss() {
        if (imgList.size()>=1) {
            if (typeId == 0) {
                ToastUtils.showToast("分类不能为空");
                return;
            }
            if (tagList.size() == 0) {
                ToastUtils.showToast("标签不能为空");
                return;
            }
//            File file = new File(imgPath);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.upload_img));
            progressDialog.setCancelable(false);
            progressDialog.show();

            //图片上传
            OSSHttpUtils.upLoadImages(this, Config.OssFolder.EMOTICONS,imgList,new OssService.FinishCallback()  {
                @Override
                public void onFinish(List<String> urls) {
                    if(urls.isEmpty()) {
                        progressDialog.dismiss();
                        ToastUtils.showToast("图片不能为空");
                        return;
                    }
                    if (addType) {
                        addType(urls);
                    }else {
                        submit(urls);
                    }
                }

                @Override
                public void onProgressCallback(double progress, int position) {
                    progressDialog.setMessage(getString(R.string.upload_img)+": "+position+"/"+(imgList.size()));
                    progressDialog.setProgress((int) progress);
                }
            });
        } else {
            ToastUtils.showToast("图片不能为空");
        }
    }

    /**
     * 创建分类
     * @param urls 图片链接
     */
    private void addType(final List<String> urls) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                progressDialog.setMessage("正在创建分类");
            }
        });

        EmoticonTypeProtocol emoticonTypeProtocol = RetroClient.getServices(EmoticonTypeProtocol.class);
        Call<EmoticonType.Data> call = emoticonTypeProtocol.addEmoticonType(UserManager.getUser().getToken(), type_title, urls.get(0));
        call.enqueue(new Callback<EmoticonType.Data>() {
            @Override
            public void onResponse(@NonNull Call<EmoticonType.Data> call, @NonNull Response<EmoticonType.Data> response) {
                if (response.body().getStatus() == 200){
                    typeId = response.body().getData().getId();
                    submit(urls);
                }
            }

            @Override
            public void onFailure(Call<EmoticonType.Data> call, Throwable t) {
                ToastUtils.showToast("创建分类失败");
                progressDialog.dismiss();
            }
        });
    }

    //添加标签
    private void addLabel() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.edittext, null);
        final EditText edittext = view.findViewById(R.id.edit_text);
        edittext.setHint("请输入标签");
        edittext.setLines(1);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(view);
        alertDialog.setTitle("添加标签");
        alertDialog.setPositiveButton("取消", null);
        alertDialog.setNegativeButton(" 确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(edittext.getText())) tagList.add(edittext.getText().toString());
                //labelAdapter.notifyDataSetChanged();
                flowLayoutAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.show();
    }


    /*
     *图片上传：弃用
     */
    /*
    //上传图片
    private void upLoad() {
        if (null != imgPath) {
            File file = new File(imgPath);

            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("imgfile", file.getName(), imageBody);
            UploadProtocol uploadProtocol = RetroClient.getServices(UploadProtocol.class);
            Call<Upload> l = uploadProtocol.uploadMemberIcon(imageBodyPart);
            l.enqueue(new Callback<Upload>() {
                @Override
                public void onResponse(Call<Upload> call, Response<Upload> response) {
                    if (response.body().getStatus() == 200) {
                        submit(response.body().getUrl());
                    }
                }

                @Override
                public void onFailure(Call<Upload> call, Throwable t) {
                    Toast.makeText(EmoticonAddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        } else if (null == imgPath) {
            Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
        } else if (tagList.size() == 0) {
            Toast.makeText(this, "标签不能为空", Toast.LENGTH_SHORT).show();
        } else if (typeId == 0) {
            Toast.makeText(this, "分类不能为空", Toast.LENGTH_SHORT).show();
        }
    }
*/


    //提交表情信息
    private void submit(List<String> urls) {
        if(urls.size()<=0) {
            ToastUtils.showToast("图片不能为空");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                progressDialog.setMessage("正在提交");
            }
        });
        String title = type_title.replace("表情包", "");//去除分类名中的表情包字样

        //由于服务器数据库中的label字段中标签以","隔开所以将List中的字符串遍历到一起
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title);//将整理后的分类名添加到StringBuilder
        stringBuilder.append(",");
        tagList.remove(0);
        for (String s : tagList) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("typeId",typeId);
        map.put("urls",urls);
        map.put("label",stringBuilder.toString());

        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);

        System.out.println(new Gson().toJson(map));
        Call<StatusResult<String>> call = emoticonProtocol.addEmoticon(UserManager.getUser().getToken(), new Gson().toJson(map));
        HttpUtils.doRequest(call, new HttpUtils.RequestFinishCallback<String>() {
            @Override
            public void getRequest(StatusResult result) {
                progressDialog.dismiss();
                if (result == null) return;
                if (!result.isSuccess()) {
                    String str = ResourcesManager.getRes().getString(com.example.emotion.user.R.string.request_error, result.getMsg());
                    ToastUtils.showToast(str);
                    return;
                }
                ToastUtils.showToast("提交成功");
                finish();
            }
        });
 /*       call.enqueue(new Callback<StatusResult>() {
            @Override
            public void onResponse(@NonNull Call<StatusResult> call, @NonNull Response<StatusResult> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        ToastUtils.showToast("提交成功");
                        progressDialog.dismiss();
                        finish();
                    } else {
                        ToastUtils.showToast(response.body().getMsg());
                        progressDialog.dismiss();
                    }
                }else{
                    ToastUtils.showToast("失败");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<StatusResult> call, Throwable t) {
                ToastUtils.showToast("提交失败");
                progressDialog.dismiss();
            }
        });
*/

//         Call<StatusResult> call = emoticonProtocol.addEmoticon(UserManager.getUser().getToken(), typeId, url, stringBuilder.toString());
//        call.enqueue(new Callback<StatusResult>() {
//            @Override
//            public void onResponse(Call<StatusResult> call, Response<StatusResult> response) {
//                if (response.body().getStatus() == 200) {
//                    Toast.makeText(EmoticonAddActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    finish();
//                } else {
//                    Toast.makeText(EmoticonAddActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StatusResult> call, Throwable t) {
//                Toast.makeText(EmoticonAddActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
//

    }


    public static void startActivity(Context context) {
        if (UserManager.getUser() ==null){
            LoginActivity.startActivity(context);
            return;
        }
        Intent intent = new Intent(context, EmoticonAddActivity.class);
        context.startActivity(intent);
    }

}
