package com.kotlinflowsample.flow.api

import com.google.gson.JsonObject

import retrofit2.http.*

interface ApiService {

    @GET("breeds/list/all")
    suspend fun fetchDogBread(): JsonObject




}