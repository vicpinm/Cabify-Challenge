package com.vicpin.cabifychallenge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.adapter.item.ItemViewHolder
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.model.ItemAddedListener
import com.vicpin.cabifychallenge.observe
import com.vicpin.cabifychallenge.viewmodel.CheckoutModel
import com.vicpin.kpresenteradapter.PresenterAdapter
import com.vicpin.kpresenteradapter.SimplePresenterAdapter
import kotlinx.android.synthetic.main.fragment_item_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that contains the list of available items returned by the server
 */
class ItemListFragment: Fragment(), ItemAddedListener {

    private val viewModel by sharedViewModel<CheckoutModel>()
    private var adapter: PresenterAdapter<Item>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        adapter = SimplePresenterAdapter(ItemViewHolder::class, R.layout.viewholder_item)
        adapter?.parentView = this
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observeData() {
        observe(viewModel.items) {
            adapter?.setData(it)
            progressBar.visibility = View.GONE
        }
    }

    override fun onItemAdded(item: Item) {
        viewModel.addItem(item)
    }
}