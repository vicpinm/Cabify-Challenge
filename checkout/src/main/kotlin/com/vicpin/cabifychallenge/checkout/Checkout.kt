package com.vicpin.cabifychallenge.checkout

import com.vicpin.cabifychallenge.model.CheckoutItem
import com.vicpin.cabifychallenge.model.Discount
import com.vicpin.cabifychallenge.model.ItemPrice

/**
 * Class that contains the checkout process logic
 */
class  Checkout<T: ItemPrice>(val configuration: DiscountConfig<T>? = null) {

    /**
     * Items collection currently available in the checkout process
     */
    var items = listOf<T>()
        set(value) {
            field = value
            updateDiscounts(value)
        }

    /**
     * Discounts collection currently available in the checkout process
     */
    var discounts = listOf<Discount<T>>()

    /**
     * Given a list of items, update discounts list with a given configuration defined in DiscountConfig field
     *Disco
     * @param items list of all available items
     */
    private fun updateDiscounts(items: List<T>) {
        if(configuration != null) {
            discounts = items.distinctBy { it.code }.filter { configuration.config.containsKey(it.code) }
                .map { Discount(it, configuration.config.getValue(it.code)) }
        }
    }

    /**
     * Given a list of items with no discounts, applies all the available
     * discounts to the corresponding items in the collection
     *
     * @param items collection of items with no discounts applied
     * @return collection of CheckoutItem with the discounts applied
     */
    private fun applyDiscounts(items: List<T>): List<CheckoutItem<T>> {
        val checkoutItems = items.map { CheckoutItem(it) }
        discounts.forEach { it.applyTo(checkoutItems) }
        return checkoutItems
    }

    /**
     * @return the total of the checkout process with discounts applied
     */
    fun getTotal(): Double {
        return getItemsWithDiscounts().sumByDouble { it.finalPrice() }
    }

    /**
     * @return collection of CheckoutItem
     */
    fun getItemsWithDiscounts() = applyDiscounts(items)


}
