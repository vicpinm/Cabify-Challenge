package com.vicpin.cabifychallenge.model

/**
 * This class represents an association between a specific item and
 * all the discounts applied to the item in the checkout process
 */
data class CheckoutItem<T: ItemPrice>(val item: T, val discounts: MutableList<Discount<T>> = mutableListOf()) {

    /**
     * @return final price of this item, taking into account all available discount to be applied
     */
    fun finalPrice() = item.price - discounts.sumByDouble { it.getDiscountAmount() }
}