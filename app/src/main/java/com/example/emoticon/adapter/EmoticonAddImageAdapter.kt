package com.example.emoticon.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.common.utils.ToastUtils
import com.example.emoticon.R
import com.skit.imagepicker.imageengine.GlideImageEngine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/8/23.
 * PS: 表情添加页面RecyclerView适配器
 */

class EmoticonAddImageAdapter(private val imageList: List<String?>, private val gridLayoutManager: androidx.recyclerview.widget.GridLayoutManager) : androidx.recyclerview.widget.RecyclerView.Adapter<EmoticonAddImageAdapter.ViewHolder>() {
    //    val imageList: List<String?> = ArrayList()
    var onDeleteOnClickListener: OnDeleteOnClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.emoticon_add_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (!TextUtils.isEmpty(imageList[p1])) {
            Glide.with(p0.image.context).load(imageList[p1]).into(p0.image)
            p0.delete.visibility = View.VISIBLE
            p0.image.setOnClickListener {

            }
            p0.delete.setOnClickListener { v -> onDeleteOnClickListener?.onClick(v, p1) }
        } else {
            p0.image.setImageResource(R.drawable.add_the_pic)
            p0.delete.visibility = View.GONE
            p0.image.setOnClickListener {
                val maxSelectableCount = 10 - imageList.size
                if (maxSelectableCount == 0) {
                    ToastUtils.showToast("不能选更多图片")
                } else {
                    Matisse.from(p0.image.context as AppCompatActivity)
                            .choose(MimeType.ofImage())//图片类型
                            .countable(true)//true:选中后显示数字;false:选中后显示对号
                            .maxSelectable(maxSelectableCount)//可选的最大数
                            .capture(false)//选择照片时，是否显示拍照
                            .captureStrategy(CaptureStrategy(true, "PhotoPicker"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                            .imageEngine(GlideImageEngine())//图片加载引擎
                            .theme(R.style.Matisse_Zhihu)
                            .forResult(2)
                }
            }
        }
        val param = p0.image.layoutParams //获取图片的的LayoutParams
        param.height = (gridLayoutManager.width / gridLayoutManager.spanCount - 2 * p0.image.paddingLeft - 2 * (param as ViewGroup.MarginLayoutParams).leftMargin)//设置图片高度等于宽度

    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val delete: ImageView = itemView.findViewById(R.id.delete)
    }

    interface OnDeleteOnClickListener {
        fun onClick(view: View, p0: Int)
    }

}