package com.vicpin.cabifychallenge.api

import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.http.GET

class CheckoutService(val retrofit: Retrofit) {

    val api by lazy { retrofit.create(Api::class.java) }


    suspend fun getData(): List<DataItem> {
        return api.getItems().await().products
    }

    interface Api {
        @GET("bins/4bwec")
        fun getItems() : Deferred<ServerResponse>
    }

    data class ServerResponse(val products: List<DataItem>)

    data class DataItem(val code: String, val name: String, val price: Double)
}