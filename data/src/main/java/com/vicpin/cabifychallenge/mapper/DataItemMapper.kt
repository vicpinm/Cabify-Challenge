package com.vicpin.cabifychallenge.mapper

import com.vicpin.cabifychallenge.api.CheckoutService
import com.vicpin.cabifychallenge.domain.model.Item

object DataItemMapper {

    fun map(dataItem: CheckoutService.DataItem): Item {
        return Item(dataItem.code, dataItem.name, dataItem.price)
    }
}