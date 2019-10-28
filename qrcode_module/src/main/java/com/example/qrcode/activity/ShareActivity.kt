package com.example.qrcode.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.common.base.BaseActivity
import com.example.qrcode.R
import kotlinx.android.synthetic.main.share_activity.*
import java.io.File

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/24.
 * PS: 二维码分享页面
 */
class ShareActivity : BaseActivity() {
    private var path: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_activity)
        path = intent.getStringExtra("path")//获取临时文件地址
        if (path != null) {
            Glide.with(this).load(path).into(img)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val file = File(path!!)
        file.delete()//删除图片临时文件
    }

    companion object {
//        fun startActivity(context: Context, bitmap: Bitmap) {
//            val intent: Intent = Intent()
//            intent.setClass(context, ShareActivity::class.java)
//            intent.putExtra("bitmap", bitmap)
//            context.startActivity(intent)
//        }

        fun startActivity(context: Context, path: String) {
            val intent = Intent(context, ShareActivity::class.java)
            intent.putExtra("path", path)
            context.startActivity(intent)
        }
    }
}