package com.fphoenixcorneae.common.demo

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.common.annotation.GradientType
import com.fphoenixcorneae.common.annotation.Shape
import com.fphoenixcorneae.common.demo.cache.CacheActivity
import com.fphoenixcorneae.common.demo.databinding.ActivityMainBinding
import com.fphoenixcorneae.common.demo.datastore.DataStoreDelegateActivity
import com.fphoenixcorneae.common.demo.location.LocationActivity
import com.fphoenixcorneae.common.demo.mmkv.MmkvDelegateActivity
import com.fphoenixcorneae.common.demo.shortcut.ShortcutActivity
import com.fphoenixcorneae.common.demo.spannable.SpannableStringActivity
import com.fphoenixcorneae.common.drawable.*
import com.fphoenixcorneae.common.dsl.layout.TextView
import com.fphoenixcorneae.common.ext.*
import com.fphoenixcorneae.common.ext.algorithm.*
import com.fphoenixcorneae.common.ext.view.queryTextListener
import com.fphoenixcorneae.common.ext.view.setOnSeekBarChangeListener
import com.fphoenixcorneae.common.ext.view.setUnderLine
import com.fphoenixcorneae.common.ext.view.textAction
import com.fphoenixcorneae.permissions.requestPhonePermission
import com.fphoenixcorneae.permissions.requestWritePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityMainBinding
    private val appViewModel by applicationViewModel<AppViewModel>()

    @SuppressLint("UseCompatLoadingForDrawables", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        mViewBinding.lifecycleOwner = this
        setContentView(mViewBinding.root)
        initObserver()

        mViewBinding.btnTest.setOnClickListener {
            //            toast("测试测试测试！！！")
            toastAliPayStyle("测试测试测试！！！")
            appViewModel.setGlobalString("测试 Global.")
        }
        mViewBinding.btnTest.setUnderLine()

        val job: Job = lifecycleScope.launch { }
        job.action({
            "notNull".logd("action====")
        }) {
            "null".logd("action====")
        }

        TextView {}.textAction({
            "notNull".logd("textAction====")
        }, {
            "null".logd("textAction====")
        })

        mViewBinding.svSearch.queryTextListener {
            onQueryTextChange {
                toastQQStyle(it)
            }
            onQueryTextSubmit {
                toastQQStyle(it)
            }
        }

        // 计算md5、sha值
        val origin2Md5 = "a123456"
        ("md5: ${origin2Md5.md5()}\n\n" +
                "sha1: ${origin2Md5.sha1()}\n\n" +
                "sha224: ${origin2Md5.sha224()}\n\n" +
                "sha256: ${origin2Md5.sha256()}\n\n" +
                "sha384: ${origin2Md5.sha384()}\n\n" +
                "sha512: ${origin2Md5.sha512()}").also {
            "hash: $it".logd()
        }

        val fruit = Fruit(1, Watermalon(2f, 30f))
        val clone = fruit.copy()
        val deepClone = fruit.deepClone(Fruit::class.java)
        // 判断 Watermalon 对象是否相同，预期值：true
        (fruit.watermalon === clone.watermalon).toString().logd("clone")
        // 判断 Watermalon 对象是否相同，预期值：false
        (fruit.watermalon === deepClone?.watermalon).toString().logd("deepClone")


        // 申请权限
        mViewBinding.btnRequestPermissions.setOnClickListener {
            //            requestPermissions()
            callPhone("10010")
        }
        mViewBinding.btnRequestPermissions.background = codeConstructsGradientDrawable()
        //        mViewBinding.btnRequestPermissions.background = codeConstructsStateListDrawable()

        // 设置亮度
        mViewBinding.sbBrightness.setOnSeekBarChangeListener(
            onProgressChanged = { _, progress, _ ->
                setScreenBrightness(progress)
            }
        )

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            // 生成位图并保存
            val filename = "${System.currentTimeMillis()}.jpg"
            writeFileFromIS(
                filesDir.absolutePath + "/$filename",
                createBitmap().toBytes().toInputStream()
            ).also {
                if (it) {
                    File(filesDir.absolutePath + "/$filename").zip(filesDir.absolutePath + "/压缩.zip")
                        .also {
                            it.unZip(filesDir.absolutePath + "/解压")
                        }
                }
            }
        }
    }

    private fun initObserver() {
        appViewModel.global.observe(this) {
            "MainActivity: $it".logd("GlobalAndroid")
        }
    }

    /**
     * 代码构造StateListDrawable
     */
    private fun codeConstructsStateListDrawable(): Drawable {
        return stateListDrawable {
            item {
                drawable(ColorDrawable(Color.GRAY))
                minusState(StatePressed)
            }
            item {
                drawable(ColorDrawable(Color.GREEN))
                state(StatePressed)
            }
        }
    }

    /**
     * 代码构造GradientDrawable
     */
    private fun codeConstructsGradientDrawable(): Drawable {
        return gradientDrawable(this) {
            shape(Shape.RECTANGLE)
            //            solidColor(Color.GRAY)
            solidColor {
                item {
                    color(Color.RED)
                    state(StatePressed)
                }
                item {
                    color(Color.BLUE)
                    minusState(StatePressed)
                }
            }
            corner {
                //                radius(20f)
                radii(
                    topLeftRadius = 5f,
                    topRightRadius = 10f,
                    bottomLeftRadius = 15f,
                    bottomRightRadius = 20f
                )
            }
            stroke {
                width(3f)
                dashWidth(8f)
                dashGap(3f)
                color {
                    item {
                        color(Color.BLUE)
                        state(StatePressed)
                    }
                    item {
                        color(Color.RED)
                        minusState(StatePressed)
                    }
                }
            }
            padding {
                setPadding(left = 8f, top = 8f, right = 8f, bottom = 8f)
            }
            size(width = 100, height = 20)
            gradient {
                gradientCenter(0.5f, 0.5f)
                useLevel(false)
                gradientType(GradientType.LINEAR_GRADIENT)
                orientation(GradientDrawable.Orientation.LEFT_RIGHT)
                gradientRadius(10f)
                gradientColors(intArrayOf(Color.TRANSPARENT, Color.BLACK))
            }
        }
    }

    private fun requestPermissions() {
        requestPhonePermission(shouldShowRationale = true) {
            onGranted {
                // TODO
            }
            onDenied {
                // ignore
            }
            onShowRationale { permissions, positive, negative ->
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("权限申请")
                    .setMessage("需要申请电话权限")
                    .setCancelable(false)
                    .setNegativeButton("取消") { dialog, which ->
                        negative.invoke()
                        dialog.dismiss()
                    }
                    .setPositiveButton("确定") { dialog, which ->
                        positive.invoke()
                        dialog.dismiss()
                    }
                    .show()
            }
            onNeverAsk {
                openApplicationDetailsSettings()
            }
        }
        requestWritePermission {

        }
    }

    fun go2CacheActivity(view: View) {
        startKtxActivity<CacheActivity>()
    }

    fun throwsException(view: View) {
        throw RuntimeException("测试抛出异常，然后进行处理！")
    }

    fun shortcut(view: View) {
        startKtxActivity<ShortcutActivity>()
    }

    fun location(view: View) {
        startKtxActivity<LocationActivity>()
    }

    fun spannable(view: View) {
        startKtxActivity<SpannableStringActivity>()
    }

    fun mmkv(view: View) {
        startKtxActivity<MmkvDelegateActivity>()
    }

    fun dataStore(view: View) {
        startKtxActivity<DataStoreDelegateActivity>()
    }
}

data class Fruit(
    val type: Int,
    val watermalon: Watermalon,
) : Cloneable

data class Watermalon(
    val weight: Float,
    val size: Float,
)