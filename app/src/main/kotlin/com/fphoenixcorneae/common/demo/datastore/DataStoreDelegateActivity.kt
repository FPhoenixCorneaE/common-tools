package com.fphoenixcorneae.common.demo.datastore

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.common.demo.databinding.ActivityDataStoreDelegateBinding

class DataStoreDelegateActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityDataStoreDelegateBinding

    private val mViewModel by viewModels<DataStoreViewModel> {
        DataStoreViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityDataStoreDelegateBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@DataStoreDelegateActivity
            viewModel = mViewModel
        }.also {
            setContentView(it.root)
        }
    }
}