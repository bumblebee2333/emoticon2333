package com.example.emoticon.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.particlesmaster.ParticleSmasher;
import com.example.common.particlesmaster.SmashAnimator;
import com.example.common.widget.SearchTopView;
import com.example.emoticon.R;
import com.example.common.base.BaseActivity;
import com.example.emoticon.utils.SharedPreferencesUtil;
import com.example.emoticon.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    public static int EMOTICON_TYPE = 0;
    public static int EMOTICON = 1;

    private List<String> hotWords = new ArrayList<>();//热门搜索
    private List<String> historyWords = new ArrayList<>();//历史记录
    private FlowLayout historyFlowLayout;
    private SearchTopView searchView;
    private String title = null;

    public static final String SEARCH_HISTORY = "search_hisroty";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SharedPreferencesUtil.getInstance(this, SEARCH_HISTORY);
        initIntent();
        setHotWords();
        initSearchView();
        initHotWordViews();
        initHistoryWordViews();
    }

    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", EMOTICON);
    }

    //初始化热门搜索的集合
    private void setHotWords() {
        hotWords.add("加菲猫");
        hotWords.add("权律二");
        hotWords.add("金馆长");
        hotWords.add("皮卡丘");
        hotWords.add("徐锦江");
        hotWords.add("三国演义");
        hotWords.add("猫咪");
        hotWords.add("假笑男孩");
        hotWords.add("盘他");
        hotWords.add("蜡笔小新");
    }

    //初始化SearchTopView
    private void initSearchView() {
        searchView = findViewById(R.id.search_top);
        ImageView imageView = findViewById(R.id.delete);
        imageView.setOnClickListener(this);
        searchView.cancel.setOnClickListener(this);
        searchView.editText.setOnEditorActionListener(this);
        if(type == EMOTICON_TYPE){
            searchView.editText.setHint("请输入分类名：");
        }
    }

    //初始化流式布局里的textview
    @SuppressLint("NewApi")
    private void initHotWordViews() {
        //初始化FlowLayout
        FlowLayout hotFlowLayout = findViewById(R.id.flowlayout);
        hotFlowLayout.initData(hotWords);
        hotFlowLayout.setOnTabClickListener(new FlowLayout.OnTabClickListener() {
            @Override
            public void onTabClick(int position, TextView textView) {
                title = textView.getText().toString();
                saveList(title);
                search(title);
                //IntentUtil.get().goActivityPassing(SearchActivity.this, SpecificActivity.class, title);
            }
        });
        hotFlowLayout.setOnTabLongClickListener(new FlowLayout.OnTabLongClickListener() {
            @Override
            public void onTabLongClick(int position, TextView textView) {

            }
        });
    }

    private void search(String text) {
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "关键词不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == EMOTICON_TYPE){
            SpecificActivity.startActivity(this, text, EMOTICON_TYPE);
            //finish();
        }else {
            SpecificActivity.startActivity(this, text, EMOTICON);
        }
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel://取消与搜索按钮
                if (searchView.editText.getText().length() > 0) {//内容大于0调用搜索方法
                    saveList(searchView.editText.getText().toString());
                    search(searchView.editText.getText().toString());
                } else {//否则退出
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//渐变退出动画
                }
                break;
            case R.id.delete:
                SharedPreferencesUtil.clear();
                historyFlowLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //软键盘搜索按钮
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            title = searchView.editText.getText().toString();
            saveList(title);
            search(title);
            return true;
        }
        return false;
    }

    //初始化历史记录集合
    public void setHistoryWords(String words) {
        if (checkSameWords(words)) {
            historyWords.add(words);
        }
    }

    //查找历史记录里是否有重复的词
    public boolean checkSameWords(String words) {
        boolean flag = false;
        if (historyWords.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < historyWords.size(); i++) {
                String title = historyWords.get(i);
                if (words.equals(title)) {
                    flag = true;
                }
            }
        }
        if (flag == true)
            return false;
        else
            return true;
    }

    //初始化历史记录里流式布局里的textview
    @SuppressLint("NewApi")
    private void initHistoryWordViews() {
        historyFlowLayout = findViewById(R.id.flowlayout_history_record);
        historyWords = SharedPreferencesUtil.getListData(SEARCH_HISTORY, String.class);
        historyFlowLayout.initData(historyWords);
        historyFlowLayout.setOnTabClickListener(new FlowLayout.OnTabClickListener() {
            @Override
            public void onTabClick(int position, TextView textView) {
                title = textView.getText().toString();
                saveList(title);
                search(title);
                //IntentUtil.get().goActivityPassing(SearchActivity.this, SpecificActivity.class, title);
            }
        });
        historyFlowLayout.setOnTabLongClickListener(new FlowLayout.OnTabLongClickListener() {
            @Override
            public void onTabLongClick(final int position, TextView textView) {
                ParticleSmasher particleSmasher = new ParticleSmasher(SearchActivity.this);
                particleSmasher.with(textView).setStyle(SmashAnimator.STYLE_DROP).addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                    @Override
                    public void onAnimatorEnd() {
                        super.onAnimatorEnd();
                        historyWords.remove(position);
                        historyFlowLayout.initData(historyWords);
                        if (historyWords.size() == 0) {
                            SharedPreferencesUtil.clear();
                        } else {
                            SharedPreferencesUtil.putListData(SEARCH_HISTORY, historyWords);
                        }
                    }
                }).start();
            }
        });
    }

    //保存搜索记录
    public void saveList(String title) {
        if (SharedPreferencesUtil.getListData(SEARCH_HISTORY, String.class).isEmpty()) {
            historyWords.clear();
            historyWords.add(title);
            SharedPreferencesUtil.putListData(SEARCH_HISTORY, historyWords);
        } else {
            historyWords = SharedPreferencesUtil.getListData(SEARCH_HISTORY, String.class);
            if (checkSame(title, historyWords)) {
                historyWords.add(title);
                SharedPreferencesUtil.clear();
                SharedPreferencesUtil.putListData(SEARCH_HISTORY, historyWords);
            }
        }
    }

    public boolean checkSame(String title, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (title.equals(list.get(i)))
                return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyWords.clear();
        historyWords = SharedPreferencesUtil.getListData(SEARCH_HISTORY, String.class);
        historyFlowLayout.initData(historyWords);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    setResult(-1,data);
                    finish();
                    break;

            }
        }
    }
}
