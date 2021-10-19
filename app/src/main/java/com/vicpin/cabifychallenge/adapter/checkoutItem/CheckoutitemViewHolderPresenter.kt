package com.vicpin.cabifychallenge.adapter.checkoutItem

import android.content.res.Resources
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.kpresenteradapter.ViewHolderPresenter
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Presenter for CheckoutItemViewHolder class
 */
class CheckoutitemViewHolderPresenter: ViewHolderPresenter<CheckoutItem<Item>, CheckoutitemViewHolderPresenter.View>(), KoinComponent {

    val resources by inject<Resources>()

    override fun onCreate() {
        view?.apply {
            setName(data.item.name)
            setOriginalPrice(resources.getString(R.string.original_price, data.item.price.toString()))
            setFinalPrice(resources.getString(R.string.final_price, data.finalPrice().toString()))
            setDiscount(getDiscount())

        }
    }

    private fun getDiscount(): String {
        return if(data.discounts.isNotEmpty()) {
            resources.getString(R.string.discounts_applied, data.discounts.joinToString { it.toString() })
        } else {
            resources.getString(R.string.no_discount)
        }
    }

    fun onRemoveButtonClicked() {
        view?.removeItemFromCheckout(data.item)
    }

    interface View {
        fun setName(text: String)
        fun setOriginalPrice(text: String)
        fun setFinalPrice(text: String)
        fun setDiscount(text: String)
        fun removeItemFromCheckout(data: Item)
    }
}