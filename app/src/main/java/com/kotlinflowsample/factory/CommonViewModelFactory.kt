package com.kotlinflowsample.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlinflowsample.viewmodel.CommonViewModel

class CommonViewModelFactory (val baseActivity: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CommonViewModel::class.java)) {
            CommonViewModel(baseActivity) as T
        } else {
            throw IllegalArgumentException("Unknown class name")
        }

    }
}