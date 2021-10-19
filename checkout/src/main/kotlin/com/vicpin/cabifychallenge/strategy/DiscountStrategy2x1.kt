package com.vicpin.cabifychallenge.strategy

import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.ItemPrice

class DiscountStrategy2x1<T: ItemPrice>: DiscountStrategy<T> {

    /**
     * Apply discount only to odd items in your list
     *
     * @param items items of the same type to apply this discount
     * @param index current index position within the items param list
     * @return true if the discount is applied to the given item index, false otherwise
     */
    override fun canApplyDiscount(items: List<CheckoutItem<T>>, index: Int): Boolean {
        return index % 2 != 0
    }

    /**
     * When this discount is applied, the final price of the item is 0
     *
     * @param originalPrice original price that this items has
     * @return the same amount than the original price, so the final price is 0
     */
    override fun discountAmount(originalPrice: Double): Double {
        return originalPrice
    }

    override fun toString(): String {
        return "2x1 discount"
    }
}