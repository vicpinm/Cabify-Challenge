package com.vicpin.cabifychallenge

import com.vicpin.cabifychallenge.checkout.DiscountConfig
import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.strategy.DiscountStrategy2x1
import com.vicpin.cabifychallenge.strategy.DiscountStrategyBulk

class CabifyDiscountConfiguration: DiscountConfig<Item> {

    //Item codes
    val TSHIRT = "TSHIRT"
    val VOUCHER = "VOUCHER"

    override val config = mapOf(
        TSHIRT to DiscountStrategyBulk<Item>(minAmount = 3),
        VOUCHER to DiscountStrategy2x1<Item>()
    )
}

