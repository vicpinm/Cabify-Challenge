package com.vicpin.cabifychallenge.strategy

import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.ItemPrice

class DiscountStrategyBulk<T: ItemPrice>(val minAmount: Int): DiscountStrategy<T> {

    /**
     * Apply discount whenever the amount of items purchased is same or greater than minAmount value
     *
     * @param items items of the same type to apply this discount
     * @param index current index position within the items param list
     * @return true if the discount is applied to the given item index, false otherwise
     */
    override fun canApplyDiscount(items: List<CheckoutItem<T>>, index: Int): Boolean {
        return items.size >= minAmount
    }

    /**
     * The amount of this discount is only 1 eur
     *
     * @param originalPrice original price that this items has
     * @return Always return 1 eur to be descounted from the original price
     */
    override fun discountAmount(originalPrice: Double): Double {
        return 1.0
    }

    override fun toString(): String {
        return "bulk discount (minimum $minAmount)"
    }
}