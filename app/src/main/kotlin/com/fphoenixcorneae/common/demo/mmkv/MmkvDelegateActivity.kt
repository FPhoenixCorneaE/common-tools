package com.fphoenixcorneae.common.demo.mmkv

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.common.demo.databinding.ActivityMmkvDelegateBinding

class MmkvDelegateActivity : AppCompatActivity() {
    private lateinit var mViewBinding: ActivityMmkvDelegateBinding
    private val mViewModel by viewModels<MmkvViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMmkvDelegateBinding.inflate(layoutInflater)
        mViewBinding.lifecycleOwner = this
        mViewBinding.viewModel = mViewModel
        setContentView(mViewBinding.root)
    }
}