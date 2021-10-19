package com.vicpin.cabifychallenge.adapter.item

import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.kpresenteradapter.ViewHolderPresenter

/**
 * Presenter for ItemViewHolderPresenter class
 */
class ItemViewHolderPresenter: ViewHolderPresenter<Item, ItemViewHolderPresenter.View>() {

    override fun onCreate() {
        view?.apply {
            setName(data.name)
            setPrice(data.price.toString())
        }
    }

    fun onAddButtonClicked() {
        view?.addItemToList(data)
    }

    interface View {
        fun setName(text: String)
        fun setPrice(text: String)
        fun addItemToList(data: Item)
    }
}