package com.vicpin.cabifychallenge.checkout

import com.vicpin.cabifychallenge.model.ItemPrice
import com.vicpin.cabifychallenge.strategy.DiscountStrategy

/**
 * Interface which contains a list of pairs <Item_code, discount_strategy>
 */

interface DiscountConfig<T: ItemPrice> {
    val config: Map<String, DiscountStrategy<T>>
}