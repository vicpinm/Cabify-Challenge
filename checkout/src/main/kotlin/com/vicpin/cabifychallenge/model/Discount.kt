package com.vicpin.cabifychallenge.model

import com.vicpin.cabifychallenge.strategy.DiscountStrategy

/**
 * Class that represents a discount applied to a specific kind of ItemPrice
 *
 * @param item to apply this discount
 * @param strategy DiscountStrategy which describes how to apply the discount
 */
data class Discount<T: ItemPrice>(val item: ItemPrice, val strategy: DiscountStrategy<T>) {

    /**
     * Return the amount of money this discount represents
     */
    fun getDiscountAmount() = strategy.discountAmount(originalPrice = item.price)

    /**
     * Given a list of CheckoutItem, add this discount to the corresponding items
     *
     * @param items all items available in the checkout process
     */
    fun applyTo(items: List<CheckoutItem<T>>) {
        val aplicableItems = items.filter { it.item == item }
        aplicableItems.forEachIndexed { index, item ->
            if(strategy.canApplyDiscount(aplicableItems, index)) {
                item.discounts.add(this@Discount)
            }
        }
    }

    override fun toString(): String {
        return "$strategy on ${item.code}, discount: ${getDiscountAmount()}"
    }

}
