package com.vicpin.cabifychallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.vicpin.cabifychallenge.checkout.Checkout
import com.vicpin.cabifychallenge.domain.usecase.GetDataCase
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.CheckoutItem


class CheckoutModel(val getDataCase: GetDataCase, val checkout: Checkout<Item>) : ViewModel() {

    //Available items in our catalog
    val items = MutableLiveData<List<Item>>()

    //Items added to cart
    val shoppingList = mutableListOf<Item>()

    //CheckoutItem list which contains shoopingList items and its discounts
    val checkoutItems = MutableLiveData<List<CheckoutItem<Item>>>()
    val total = MutableLiveData<Double>()

    init {
        loadItems()
    }

    private fun loadItems() {
        getDataCase {
            this.items.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        getDataCase.cancel()
    }

    private fun updateCheckout() {
        checkout.items = shoppingList
        checkoutItems.value = checkout.getItemsWithDiscounts()
        total.value = checkout.getTotal()
    }

    fun addItem(item: Item) {
        shoppingList.add(item)
        updateCheckout()
    }

    fun removeItem(item: Item) {
        shoppingList.remove(item)
        updateCheckout()
    }

}