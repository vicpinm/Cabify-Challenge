package com.vicpin.cabifychallenge.model

import com.vicpin.cabifychallenge.domain.model.Item

interface ItemAddedListener {

    fun onItemAdded(item: Item)


}