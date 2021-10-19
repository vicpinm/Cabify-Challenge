package com.vicpin.cabifychallenge.model

import com.vicpin.cabifychallenge.domain.model.Item

interface ItemRemovedListener {

    fun onItemRemoved(item: Item)

}