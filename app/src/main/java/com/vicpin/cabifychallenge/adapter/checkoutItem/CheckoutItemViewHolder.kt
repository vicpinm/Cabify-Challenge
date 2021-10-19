package com.vicpin.cabifychallenge.adapter.checkoutItem

import android.view.View
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.ItemRemovedListener
import com.vicpin.kpresenteradapter.ViewHolder
import kotlinx.android.synthetic.main.viewholder_checkoutitem.*

/**
 * Viewholder that binds checkoutItem objects to the layout
 */
class CheckoutItemViewHolder(itemView: View): ViewHolder<CheckoutItem<Item>>(itemView),
    CheckoutitemViewHolderPresenter.View {

    override val presenter = CheckoutitemViewHolderPresenter()

    init {
        removeButton.setOnClickListener { presenter.onRemoveButtonClicked() }
    }

    override fun setName(text: String) {
        name.text = text
    }

    override fun setOriginalPrice(text: String) {
        originalPrice.text = text
    }

    override fun setFinalPrice(text: String) {
        reducedPrice.text = text
    }

    override fun setDiscount(text: String) {
        discount.text = text
    }

    override fun removeItemFromCheckout(data: Item) {
        (parentView as? ItemRemovedListener)?.onItemRemoved(data)
    }

}