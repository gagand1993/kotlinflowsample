package com.kotlinflowsample.flow.api

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiHelper {

    fun fetchDogBread(): Flow<JsonObject>



}