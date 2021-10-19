package com.vicpin.cabifychallenge.adapter.item

import android.view.View
import android.widget.Toast
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.ItemAddedListener
import com.vicpin.kpresenteradapter.ViewHolder
import kotlinx.android.synthetic.main.viewholder_item.*

/**
 * Viewholder that binds item objects to the layout
 */
class ItemViewHolder(itemView: View): ViewHolder<Item>(itemView),
    ItemViewHolderPresenter.View {

    override val presenter = ItemViewHolderPresenter()

    init {
        addButton.setOnClickListener { presenter.onAddButtonClicked() }
    }

    override fun setName(text: String) {
        name.text = text
    }

    override fun setPrice(text: String) {
        price.text = text
    }

    override fun addItemToList(data: Item) {
        (parentView as? ItemAddedListener)?.onItemAdded(data)
        Toast.makeText(context, context.getString(R.string.item_added_message, data.name), Toast.LENGTH_SHORT).show()
    }
}