package com.example.emoticon.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cy.cyflowlayoutlibrary.FlowLayout;
import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.example.common.particlesmaster.ParticleSmasher;
import com.example.common.particlesmaster.SmashAnimator;
import com.example.emoticon.R;
import com.example.emoticon.RetroClient;
import com.example.emoticon.adapter.LabelAdapter;
import com.example.emoticon.app.Config;
import com.example.common.base.BaseActivity;
import com.example.emoticon.model.EmoticonType;
import com.example.emoticon.model.Status;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.emoticon.retrofit.EmoticonTypeProtocol;
import com.example.emoticon.service.OssService;
import com.example.emoticon.utils.HttpUtils;
import com.example.emoticon.utils.UserManager;
import com.example.common.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmoticonAddActivity extends BaseActivity implements View.OnClickListener {
    //RecyclerView recyclerView;
    TextView type;
    LabelAdapter labelAdapter;
    List<String> list = new ArrayList<>();
    private FlowLayoutAdapter<String> flowLayoutAdapter;
    Toolbar toolbar;
    ImageView imageSelect;
    int type_id = 0;
    String imgPath, type_title;
    ProgressDialog progressDialog;
    boolean addType = false;
    private FlowLayout flowLayout;
    private ParticleSmasher smasher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoticon_add);
        initViews();
    }

    private void initViews() {

        labelAdapter = new LabelAdapter(list);
        type = findViewById(R.id.type);
        toolbar = findViewById(R.id.toolbar);
        imageSelect = findViewById(R.id.image);
        flowLayout = findViewById(R.id.flowlayout);
        imageSelect.setOnClickListener(this);
        toolbar.right1.setVisibility(View.VISIBLE);
        toolbar.right1.setImageResource(R.drawable.right_ok);
        toolbar.right1.setOnClickListener(this);
        type.setOnClickListener(this);
        list.add("点击添加标签");
        flowLayoutAdapter = flowLayoutAdapter();
        flowLayout.setAdapter(flowLayoutAdapter);
        smasher = new ParticleSmasher(this);
    }

    private FlowLayoutAdapter<String> flowLayoutAdapter() {
       return new FlowLayoutAdapter<String>(list) {
            @Override
            public void bindDataToView(ViewHolder viewHolder, final int i, String s) {
                viewHolder.setText(R.id.tv, "# "+s+" #");
                viewHolder.setOnLongClickListener(R.id.tv, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (i != 0) {
                            smasher.with(v).setStyle(SmashAnimator.STYLE_DROP).addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                                @Override
                                public void onAnimatorEnd() {
                                    super.onAnimatorEnd();
                                    list.remove(i);
                                    flowLayoutAdapter.notifyDataSetChanged();
                                }
                            }).start();
                        }else {
                            Toast.makeText(EmoticonAddActivity.this, getString(R.string.topic_toast_hint), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
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
                    Uri selectImg = data.getData();
                    String[] filepath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectImg, filepath, null, null, null);
                    c.moveToFirst();
                    int cul = c.getColumnIndex(filepath[0]);
                    String imgPath = c.getString(cul);
                    this.imgPath = imgPath;
                    Glide.with(this).load(imgPath).into(imageSelect);
                    break;
            }
        }
    }

    private void setType(Intent data) {
        type_title = data.getStringExtra("title");
        type_id = data.getIntExtra("id", 0);
        addType = data.getBooleanExtra("addtype",false);
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.image:
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 2);
                break;
            case R.id.right1:
                if (new UserManager(this).getUser() == null) {
                    Toast.makeText(this, "请登陆后再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadToOss();

                break;
        }
    }

    //上传到Oss
    private void uploadToOss() {
        if (null != imgPath) {
            if (type_id == 0) {
                Toast.makeText(EmoticonAddActivity.this, "分类不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (list.size() == 0) {
                Toast.makeText(this, "标签不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(imgPath);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.upload_img));
            progressDialog.setCancelable(false);
            progressDialog.show();

            HttpUtils.upLoadImage(this, Config.OssFolder.EMOTICON,file,new OssService.ProgressCallback() {
                @Override
                public void onProgressCallback(final double progress, String url) {
                    progressDialog.setProgress((int) progress);
                    if (progress == 100) {
                        if (addType) {
                            addType(url);
                        }else {
                            submit(url);
                        }
                    }
                }
            });

            /*
            Long time = System.currentTimeMillis();//获取时间戳
            final String url = Config.emoticonImgHost + time + file.getName().substring(file.getName().lastIndexOf("."));//拼接图片链接，之后提交时会用到
            String name = "emoticon/images/" + time + file.getName().substring(file.getName().lastIndexOf("."));//拼接对象名
            //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是阿里云oss中参数）
            OssService ossService = new OssService(this, Config.endpoint, Config.bucketName);
            //初始化OSSClient
            ossService.initOSSClient();
            //开始上传，参数分别为content，上传的文件名，上传的文件路径
            ossService.imageUpload(this, name, imgPath);
            //上传的进度回调
            ossService.setProgressCallback(new OssService.ProgressCallback() {
                @Override
                public void onProgressCallback(final double progress) {
                    progressDialog.setProgress((int) progress);
                    if (progress == 100) {
                        if (addType) {
                            addType(url);
                        }else {
                            submit(url);
                        }
                    }
                }
            });*/
        } else if (null == imgPath) {
            Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void addType(final String url) {
        progressDialog.setMessage("正在创建分类");
        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonTypeProtocol emoticonTypeProtocol = retrofit.create(EmoticonTypeProtocol.class);
        Call<EmoticonType.Data> call = emoticonTypeProtocol.addEmoticonType(new UserManager(this).getUser().getToken(), type_title, url);
        call.enqueue(new Callback<EmoticonType.Data>() {
            @Override
            public void onResponse(@NonNull Call<EmoticonType.Data> call, @NonNull Response<EmoticonType.Data> response) {
                if (response.body().getStatus() == 200){
                    type_id = response.body().getData().getId();
                    submit(url);
                }
            }

            @Override
            public void onFailure(Call<EmoticonType.Data> call, Throwable t) {
                Toast.makeText(EmoticonAddActivity.this, "创建分类失败", Toast.LENGTH_SHORT).show();
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
                if (!TextUtils.isEmpty(edittext.getText())) list.add(edittext.getText().toString());
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
            Retrofit retrofit = RetroClient.getRetroClient();
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("imgfile", file.getName(), imageBody);
            UploadProtocol uploadProtocol = retrofit.create(UploadProtocol.class);
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
        } else if (list.size() == 0) {
            Toast.makeText(this, "标签不能为空", Toast.LENGTH_SHORT).show();
        } else if (type_id == 0) {
            Toast.makeText(this, "分类不能为空", Toast.LENGTH_SHORT).show();
        }
    }
*/


    //提交表情信息
    private void submit(String url) {
        progressDialog.setMessage("正在提交");
        String title = type_title.replace("表情包", "");//去除分类名中的表情包字样

        //由于服务器数据库中的label字段中标签以","隔开所以将List中的字符串遍历到一起
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title);//将整理后的分类名添加到StringBuilder
        stringBuilder.append(",");
        for (String s : list) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
        //12是用户id，我先写死了，之后登陆加上了就把12换成登陆的用户id
        Call<Status> call = emoticonProtocol.addEmoticon(new UserManager(this).getUser().getToken(), type_id, url, stringBuilder.toString());
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(EmoticonAddActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(EmoticonAddActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(EmoticonAddActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EmoticonAddActivity.class);
        context.startActivity(intent);
    }

}
