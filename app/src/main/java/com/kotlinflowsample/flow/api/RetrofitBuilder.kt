package com.kotlinflowsample.flow.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    fun getApiService(): ApiService {
        return  getRetrofit().create(ApiService::class.java)
    }


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient())
            .build()
    }




    private fun httpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(provideHeaderInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS).build()
    }

    private fun provideHeaderInterceptor(): Interceptor {

        return Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)

        }
    }


    private const val MESSAGE_KEY = "message"
    private const val ERROR_KEY = "error"

    fun getErrorHandling(e: Throwable):String{
         when(e){
            is HttpException -> {
                val body = e.response()?.errorBody()
                /*404 = when your logged in another device*/
                if(e.code()== 440){
                    return  "UNAUTHORIZED"
                }else{


                    try {
                        val jsonObject = JSONObject(body!!.string())
                        when {
                            jsonObject.has(MESSAGE_KEY) ->  return jsonObject.getString(MESSAGE_KEY)
                            jsonObject.has(ERROR_KEY) ->  return jsonObject.getString(ERROR_KEY)

                            else -> { return "Something wrong happened"}
                        }
                    } catch (e: Exception) {
                        return e.message.toString()
                    }


                }
            }
            is SocketTimeoutException -> return "TIMEOUT"
            is ConnectException , is IOException ->{
                return e.toString()
            }
            else -> return "SERVER ERROR"
        }

    }



}