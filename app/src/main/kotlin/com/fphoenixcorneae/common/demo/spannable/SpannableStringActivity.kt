package com.fphoenixcorneae.common.demo.spannable

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.common.demo.databinding.ActivitySpannableStringBinding
import com.fphoenixcorneae.common.ext.appendForegroundColorIntSpan
import com.fphoenixcorneae.common.ext.sizeSpan
import com.fphoenixcorneae.common.ext.strikethroughSpan
import com.fphoenixcorneae.common.ext.typeSpan

class SpannableStringActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivitySpannableStringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivitySpannableStringBinding.inflate(layoutInflater)
        mViewBinding.lifecycleOwner = this
        setContentView(mViewBinding.root)

        mViewBinding.tvSpannable.sizeSpan(
            src = "设置目标文字大小",
            target = "目标",
            textSize = 20f
        ).typeSpan(target = "文字", style = Typeface.BOLD) // textView已经设置过内容，可以不用初始化src
            .strikethroughSpan(target = "目标") // 对同一部分文字做多个效果
            .appendForegroundColorIntSpan("蓝色", Color.BLUE) // 拼接内容
    }
}