package com.vicpin.cabifychallenge.strategy

import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.ItemPrice

interface DiscountStrategy<T: ItemPrice> {

    /**
     * @param items items of the same type to apply this discount
     * @param index current index position within the items param list
     * @return true if the discount is applied to the given item index, false otherwise
     */
    fun canApplyDiscount(items: List<CheckoutItem<T>>, index: Int): Boolean

    /**
     * @param originalPrice original price that this items has
     * @return the amount to be descounted from the original price
     */
    fun discountAmount(originalPrice: Double): Double

}
