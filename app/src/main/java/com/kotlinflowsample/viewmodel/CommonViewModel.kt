package com.kotlinflowsample.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.kotlinflowsample.flow.api.ApiHelperImpl
import com.kotlinflowsample.flow.api.RetrofitBuilder
import com.kotlinflowsample.flow.api.RetrofitBuilder.getErrorHandling
import com.kotlinflowsample.util.Helper.isOnline
import com.mi.aiattorney.flow.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CommonViewModel(val mContext: Context): ViewModel() {

    private val _dogBreadResponse = MutableStateFlow<Resource<JsonObject>>(Resource.started())
    val dogBreadResponse: StateFlow<Resource<JsonObject>> = _dogBreadResponse



    fun fetchBread() {
        if (isOnline(mContext)) {
            viewModelScope.launch(Dispatchers.Main) {
                //loading show
                _dogBreadResponse.value = Resource.loading()

                ApiHelperImpl(RetrofitBuilder.getApiService()).fetchDogBread()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        //loading hide

                        Toast.makeText(mContext, getErrorHandling(e), Toast.LENGTH_SHORT).show()

                        _dogBreadResponse.value = Resource.error("")
                    }
                    .collect {
                        //loading hide
                        _dogBreadResponse.value = Resource.success(it)
                        _dogBreadResponse.value = Resource.started()
                    }


            }

        } else {
            Toast.makeText(mContext, "No Internet connection", Toast.LENGTH_SHORT).show()
        }

    }

}