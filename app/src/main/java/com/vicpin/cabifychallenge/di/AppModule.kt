package com.vicpin.cabifychallenge.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vicpin.cabifychallenge.CabifyDiscountConfiguration
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.api.CheckoutService
import com.vicpin.cabifychallenge.checkout.Checkout
import com.vicpin.cabifychallenge.checkout.DiscountConfig
import com.vicpin.cabifychallenge.domain.usecase.GetDataCase
import com.vicpin.cabifychallenge.domain.repository.ICheckoutRepository
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.repository.CheckoutRepository
import com.vicpin.cabifychallenge.viewmodel.CheckoutModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getAppModule(context: Context) = module {

    //ViewModel
    viewModel { CheckoutModel(get(), get()) }

    //Checkout
    single { Checkout<Item>(get()) }
    single<DiscountConfig<Item>> { CabifyDiscountConfiguration() }

    //Use cases
    single { GetDataCase(get()) }

    //Data
    single<ICheckoutRepository> { CheckoutRepository(get()) }
    single { CheckoutService(get()) }
    single {
        Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Resources
    single { context.resources }
}