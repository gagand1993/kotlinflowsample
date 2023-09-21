package com.kotlinflowsample.flow.api

import kotlinx.coroutines.flow.flow

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override fun fetchDogBread() = flow { emit(apiService.fetchDogBread()) }




}