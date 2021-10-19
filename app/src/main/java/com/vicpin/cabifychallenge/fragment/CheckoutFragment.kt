package com.vicpin.cabifychallenge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.adapter.checkoutItem.CheckoutItemViewHolder
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.ItemRemovedListener
import com.vicpin.cabifychallenge.observe
import com.vicpin.cabifychallenge.setExpandStateListener
import com.vicpin.cabifychallenge.toggle
import com.vicpin.cabifychallenge.viewmodel.CheckoutModel
import com.vicpin.kpresenteradapter.PresenterAdapter
import com.vicpin.kpresenteradapter.SimplePresenterAdapter
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that contains the items added to cart and the checkout total amount
 */
class CheckoutFragment : Fragment(), ItemRemovedListener {

    private var adapter: PresenterAdapter<CheckoutItem<Item>>? = null
    private val viewModel by sharedViewModel<CheckoutModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        observeData()
        initBotomSheetListener()
    }

    private fun initAdapter() {
        adapter = SimplePresenterAdapter(CheckoutItemViewHolder::class, R.layout.viewholder_checkoutitem)
        adapter?.parentView = this
        shoppingList.layoutManager = LinearLayoutManager(context)
        shoppingList.adapter = adapter
        total.text = getString(com.vicpin.cabifychallenge.R.string.total, "0")
        itemsInCart.text = getString(com.vicpin.cabifychallenge.R.string.items_in__cart, 0)
    }

    private fun observeData() {
        observe(viewModel.checkoutItems) {
            adapter?.setData(it)
            itemsInCart.text = getString(com.vicpin.cabifychallenge.R.string.items_in__cart, it.size)
        }

        observe(viewModel.total) {
            total.text = getString(com.vicpin.cabifychallenge.R.string.total, it.toString())
        }
    }

    /**
     * Listener to toggle bottom sheet when header is clicked and rotate arrow when panel is expanded or collapsed
     */
    private fun initBotomSheetListener() {
        activity?.findViewById<View>(com.vicpin.cabifychallenge.R.id.bottomSheet)?.let {

            val behavior = BottomSheetBehavior.from(it)
            header.setOnClickListener { behavior.toggle() }

            behavior.setExpandStateListener { isExpanded ->
                if (adapter?.itemCount ?: 0 == 0) return@setExpandStateListener

                if (isExpanded) {
                    arrow.rotation = 0f
                } else {
                    arrow.rotation = 180f
                }
            }
        }
    }


    override fun onItemRemoved(item: Item) {
        viewModel.removeItem(item)
    }
}
